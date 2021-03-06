/*
 * Copyright (C) 2012 Alex Kuiper
 *
 * This file is part of PageTurner
 *
 * PageTurner is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PageTurner is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PageTurner.  If not, see <http://www.gnu.org/licenses/>.*
 */
package net.nightwhistler.pageturner.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import com.google.inject.Inject;

import org.nrjd.bv.app.activity.MenuActionHandler;
import org.nrjd.bv.app.activity.ViewUtils;
import org.nrjd.bv.app.reg.BookDataUtils;
import org.nrjd.bv.app.util.AppUtils;
import org.nrjd.bv.app.net.NetDownloadTask;

import jedi.option.Option;
import net.nightwhistler.htmlspanner.HtmlSpanner;
import net.nightwhistler.pageturner.Configuration;
import net.nightwhistler.pageturner.Configuration.ColourProfile;
import net.nightwhistler.pageturner.Configuration.LibrarySelection;
import net.nightwhistler.pageturner.Configuration.LibraryView;
import net.nightwhistler.pageturner.PlatformUtil;
import org.nrjd.bv.app.R;
import net.nightwhistler.pageturner.activity.*;
import net.nightwhistler.ui.DialogFactory;
import net.nightwhistler.ui.UiUtils;
import net.nightwhistler.pageturner.library.*;
import net.nightwhistler.pageturner.scheduling.QueueableAsyncTask;
import net.nightwhistler.pageturner.scheduling.TaskQueue;
import net.nightwhistler.pageturner.view.BookCaseView;
import net.nightwhistler.pageturner.view.FastBitmapDrawable;

import org.nrjd.bv.app.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import roboguice.inject.InjectView;

import java.io.File;
import java.text.DateFormat;
import java.util.*;

import static java.lang.Character.toUpperCase;
import static jedi.functional.FunctionalPrimitives.isEmpty;
import static jedi.option.Options.none;
import static jedi.option.Options.option;
import static jedi.option.Options.some;
import static net.nightwhistler.ui.UiUtils.onCollapse;
import static net.nightwhistler.ui.UiUtils.onMenuPress;
import static net.nightwhistler.pageturner.PlatformUtil.isIntentAvailable;

public class LibraryFragment extends RoboSherlockFragment implements ImportCallback {

    protected static final int REQUEST_CODE_GET_CONTENT = 2;
	
	@Inject 
	private LibraryService libraryService;

    @Inject
    private DialogFactory dialogFactory;
	
	@InjectView(R.id.libraryList)
	private ListView listView;
	
	@InjectView(R.id.bookCaseView)
	private BookCaseView bookCaseView;
		
	@InjectView(R.id.alphabetList)
	private ListView alphabetBar;
	
	private AlphabetAdapter alphabetAdapter;
	
	@InjectView(R.id.alphabetDivider)
	private ImageView alphabetDivider;
	
	@InjectView(R.id.libHolder)
	private ViewSwitcher switcher;

    @Inject
    private Context context;
	
	@Inject
	private Configuration config;

    @Inject
    private TaskQueue taskQueue;

	private Drawable backupCover;
	private Handler handler;
		
	private KeyedResultAdapter bookAdapter;
		
	private static final DateFormat DATE_FORMAT = DateFormat.getDateInstance(DateFormat.LONG);
	private static final int ALPHABET_THRESHOLD = 20;
	
	private ProgressDialog waitDialog;
	private ProgressDialog importDialog;
	// BVApp-Comment: 04/Oct/2015: This flag indicates if a previous import dialog got dismissed or not.
	// This flag is used to recreate the import dialog once it gets dismissed.
	private boolean isImportDialogDismissed = false;
	
	private AlertDialog importQuestion;
	// BVApp-Comment: 21/Sep/2015: Added download question dialog.
	private AlertDialog downloadQuestion;
	
	private boolean askedUserToImport;
	private boolean oldKeepScreenOn;
	
	private static final Logger LOG = LoggerFactory.getLogger("LibraryActivity");

	private IntentCallBack intentCallBack;
	private List<CoverCallback> callbacks = new ArrayList<>();
	private Map<String, FastBitmapDrawable> coverCache = new HashMap<>();

    private MenuItem searchMenuItem;

	private interface IntentCallBack {
		void onResult( int resultCode, Intent data );
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {			
		super.onCreate(savedInstanceState);

        LOG.debug("onCreate()");
		
		Bitmap backupBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.unknown_cover );
		this.backupCover = new FastBitmapDrawable(backupBitmap);
		
		this.handler = new Handler();
				
		if ( savedInstanceState != null ) {
			this.askedUserToImport = savedInstanceState.getBoolean("import_q", false);
		}

        this.taskQueue.setTaskQueueListener(this::onTaskQueueEmpty);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_library, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setHasOptionsMenu(true);
		this.bookCaseView.setOnScrollListener( new CoverScrollListener() );
		this.listView.setOnScrollListener( new CoverScrollListener() );
		
		if ( config.getLibraryView() == LibraryView.BOOKCASE ) {
			
			this.bookAdapter = new BookCaseAdapter();
			this.bookCaseView.setAdapter(bookAdapter);			
			
			if ( switcher.getDisplayedChild() == 0 ) {
				switcher.showNext();
			}
		} else {		
			this.bookAdapter = new BookListAdapter(context);
			this.listView.setAdapter(bookAdapter);
		}

		this.waitDialog = new ProgressDialog(context);
		this.waitDialog.setOwnerActivity(getActivity());

		// BVApp-Comment: 11/Oct/2015: Creating the import dialog.
		this.importDialog = createImportDialog();

		registerForContextMenu(this.listView);	

        this.listView.setOnItemClickListener( this::onItemClick );
		this.listView.setOnItemLongClickListener(this::onItemLongClick );
		
		setAlphabetBarVisible(false);
	}

	// BVApp-Comment: 11/Oct/2015: Added utility method to create import dialog so that
	// this method can be used in the dialog dismiss case as well.
	private ProgressDialog createImportDialog() {
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setOwnerActivity(getActivity());
		dialog.setOnDismissListener(v -> { if (v == importDialog) isImportDialogDismissed = true; });
		// BVApp-Comment: 11/Oct/2015: Changed import progress dialog messages to indicate download books activity.
		//// importDialog.setTitle(R.string.importing_books);
		//// importDialog.setMessage(getString(R.string.scanning_epub));
		dialog.setTitle(R.string.downloading_books);
		dialog.setMessage(getString(R.string.checking_for_book_updates));
		// BVApp-Comment: 11/Oct/2015: Added progress bar capability to import progress dialog.
		dialog.setIndeterminate(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		// BVApp-Comment: 11/Oct/2015: Reset isImportDialogDismissed flag.
		this.isImportDialogDismissed = false;
		return dialog;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ActionBar actionBar = getSherlockActivity().getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		ArrayAdapter<String> adapter = new ArrayAdapter<>(actionBar.getThemedContext(),
				android.R.layout.simple_list_item_1,
				android.R.id.text1, getResources().getStringArray(R.array.libraryQueries));

		actionBar.setListNavigationCallbacks(adapter, this::onNavigationItemSelected );

        refreshView();

		Option<File> libraryFolder = config.getLibraryFolder();

		libraryFolder.match( folder -> {
			executeTask(new CleanFilesTask(libraryService, this::booksDeleted) );
			executeTask(new ImportTask(getActivity(), libraryService, this, config, config.getCopyToLibraryOnScan(),
					true), folder );
		}, () -> {
			LOG.error("No library folder present!");
			Toast.makeText( context, R.string.library_failed, Toast.LENGTH_LONG ).show();
		});

	}

    private <A,B,C> void executeTask( QueueableAsyncTask<A,B,C> task, A... parameters ) {
        setSupportProgressBarIndeterminateVisibility(true);
        this.taskQueue.executeTask(task, parameters);
    }

    /**
     * Triggered by the TaskQueue when all tasks are finished.
     */
    private void onTaskQueueEmpty() {
        LOG.debug( "Got onTaskQueueEmpty()" );
        setSupportProgressBarIndeterminateVisibility(false);
    }

    private void clearCoverCache() {
		for ( Map.Entry<String, FastBitmapDrawable> draw: coverCache.entrySet() ) {
			draw.getValue().destroy();
		}

		coverCache.clear();
	}

	private void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

        if ( config.getLongShortPressBehaviour() == Configuration.LongShortPressBehaviour.NORMAL ) {
            this.bookAdapter.getResultAt( position ).forEach( this::showBookDetails );
        } else {
            this.bookAdapter.getResultAt( position ).forEach( this::openBook );
        }
	}	

	private boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {

        if ( config.getLongShortPressBehaviour() == Configuration.LongShortPressBehaviour.NORMAL ) {
            this.bookAdapter.getResultAt( position ).forEach(this::openBook);
        } else {
            this.bookAdapter.getResultAt( position ).forEach( this::showBookDetails );
        }

		return true;
	}

	private Option<Drawable> getCover( LibraryBook book ) {

        try {
            if ( !coverCache.containsKey(book.getFileName() ) ) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(book.getCoverImage(), 0, book.getCoverImage().length );
                FastBitmapDrawable drawable = new FastBitmapDrawable(bitmap);
                coverCache.put( book.getFileName(), drawable );
            }

            return option(coverCache.get(book.getFileName()));

        } catch ( OutOfMemoryError outOfMemoryError ) {
            clearCoverCache();
            return none();
        }
	}
	
	private void showBookDetails( final LibraryBook libraryBook ) {

        if ( ! isAdded() || libraryBook == null ) {
            return;
        }
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.book_details);
		LayoutInflater inflater = PlatformUtil.getLayoutInflater(getActivity());
		
		View layout = inflater.inflate(R.layout.book_details, null);
		builder.setView( layout );
		
		ImageView coverView = (ImageView) layout.findViewById(R.id.coverImage );

		if ( libraryBook.getCoverImage() != null ) {
            Drawable coverDrawable = getCover(libraryBook).getOrElse(
                    getResources().getDrawable(R.drawable.unknown_cover) );

            coverView.setImageDrawable(coverDrawable);
        }

		TextView titleView = (TextView) layout.findViewById(R.id.titleField);
		TextView authorView = (TextView) layout.findViewById(R.id.authorField);
		TextView lastRead = (TextView) layout.findViewById(R.id.lastRead);
		TextView added = (TextView) layout.findViewById(R.id.addedToLibrary);
		// BVApp-Comment: 31/Jan/2016: Added processing for book content not available message.
		ViewGroup bookContentNotAvailableLayout = (ViewGroup) layout.findViewById(R.id.bookContentNotAvailableViewLayout);
		TextView bookContentNotAvailableTextView = (TextView) layout.findViewById(R.id.bookContentNotAvailableText);
		TextView bookContentAvailableTextView = (TextView) layout.findViewById(R.id.bookContentAvailableText);
		ViewGroup bookDescriptionViewLayout = (ViewGroup) layout.findViewById(R.id.bookDescriptionViewLayout);
		TextView descriptionView = (TextView) layout.findViewById(R.id.bookDescription);
		// BVApp-Comment: 31/Jan/2016: Commented out showing the file name.
		//// TextView fileName = (TextView) layout.findViewById(R.id.fileName);
		
		titleView.setText(libraryBook.getTitle());
		String authorText = String.format( getString(R.string.book_by),
				 libraryBook.getAuthor().getFirstName() + " " 
				 + libraryBook.getAuthor().getLastName() );
		authorView.setText( authorText );
		// BVApp-Comment: 31/Jan/2016: Commented out showing the file name.
		//// fileName.setText( libraryBook.getFileName() );

		if (libraryBook.getLastRead() != null && ! libraryBook.getLastRead().equals(new Date(0))) {
			String lastReadText = String.format(getString(R.string.last_read),
					DATE_FORMAT.format(libraryBook.getLastRead()));
			lastRead.setText( lastReadText );
		} else {
			String lastReadText = String.format(getString(R.string.last_read), getString(R.string.never_read));
			lastRead.setText( lastReadText );
		}

		// BVApp-Comment: 31/Jan/2016: Changed the label from "added_to_lib" to "downloaded_on_date".
		String addedText = String.format( getString(R.string.downloaded_on_date),
				DATE_FORMAT.format(libraryBook.getAddedToLibrary()));
		added.setText( addedText );

        HtmlSpanner spanner = new HtmlSpanner();
        spanner.unregisterHandler("img" ); //We don't want to render images

		// BVApp-Comment: 31/Jan/2016: Determining the book content availability.
		boolean isBookContentAvailable = BookDataUtils.isContentAvailable(libraryBook);
		if(isBookContentAvailable) {
			ViewUtils.removeView(bookContentNotAvailableLayout);
		} else {
			String bookContentNotAvailableText = getString(R.string.book_content_not_available);
			String bookContentAvailableText = getString(R.string.book_content_available);
			bookContentNotAvailableTextView.setText(bookContentNotAvailableText);
			bookContentAvailableTextView.setText(bookContentAvailableText);
		}

		// BVApp-Comment: 31/Jan/2016: Dynamic adjustment for the showing book description.
		String bookDescription = libraryBook.getDescription();
		if(StringUtils.isNotNullOrEmpty(bookDescription)) {
			descriptionView.setText(spanner.fromHtml(bookDescription));
		} else {
			ViewUtils.removeView(bookDescriptionViewLayout);
		}

        builder.setNeutralButton(R.string.delete, (dialog, which) -> {
			// BVApp-Comment: 31/Jan/2016: Added confirmation for book deletion.
			//// libraryService.deleteBook(libraryBook.getFileName());
			//// refreshView();
			//// dialog.dismiss();
			handleDelete(libraryBook);
		});

		if(isBookContentAvailable) {
			builder.setNegativeButton(android.R.string.cancel, null);
			builder.setPositiveButton(R.string.read, (dialog, which) -> openBook(libraryBook));
		} else {
			builder.setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss());
		}

		builder.show();
	}

	public void handleDelete(final LibraryBook libraryBook) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.delete_book);
		builder.setMessage(getString(R.string.delete_book_question));
		builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {
			libraryService.deleteBook(libraryBook.getFileName());
			refreshView();
			dialog.dismiss();
		});
		builder.setNegativeButton(android.R.string.no, (dialog, which) -> {
			dialog.dismiss();
		});
		builder.show();
	}

	private void openBook(LibraryBook libraryBook) {
		Intent intent = new Intent(getActivity(), ReadingActivity.class);
        config.setLastActivity( ReadingActivity.class );

		intent.setData( Uri.parse(libraryBook.getFileName()));
		getActivity().setResult(Activity.RESULT_OK, intent);
				
		getActivity().startActivityIfNeeded(intent, 99);		
	}
		
	private void startImport(File startFolder, boolean copy) {
		// BVApp-Comment: 21/Sep/2015: Downloading the books instead of scanning and importing the books from a local directory.
		//// ImportTask importTask = new ImportTask(context, libraryService, this, config, copy, false);
		NetDownloadTask importTask = new NetDownloadTask(context, libraryService, this, config, copy, false);
		// BVApp-Comment: 11/Oct/2015: If import dialog is dismissed already, then recreate it.
		if(this.isImportDialogDismissed) {
			this.importDialog = createImportDialog();
		}
		// BVApp-Comment: 11/Oct/2015: Reset the title and message for importDialog when download/import is starting again
		// so that it ensures to reset any previous title and messages set during the previous download activity.
		importDialog.setTitle(R.string.downloading_books);
		importDialog.setMessage(getString(R.string.checking_for_book_updates));
		// BVApp-Comment: 11/Oct/2015: Add cancel capability.
		//// importDialog.setCancelable(true);
		//// importDialog.setOnCancelListener(importTask);

		// BVApp-Comment: 03/Dec2016: Added explicit cancel button for cancelling the dialog.
		// Not using setCancelable(true) because it will make key press outside the dialog resulting into
		// automatically dismissing the progress dialog. Same with mobile screen saver or screen display off event
		// will result into dismissing the progress dialog.
		// So adding explicit cancel button and making cancellable to false, but however note thet
		// we are setting the onCancelListener event in case some other system even makes the window to cancel,
		// then the on onCancelListener event handler will ensure to cancel the background task.
		importDialog.setCancelable(false);
		importDialog.setCanceledOnTouchOutside(false);
		importDialog.setOnCancelListener(importTask);
		importDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), (dialog, which ) -> {
			importTask.onCancel(dialog);
			dialog.dismiss();
		});
		importDialog.show();
				
		this.oldKeepScreenOn = listView.getKeepScreenOn();
		listView.setKeepScreenOn(true);

        this.taskQueue.clear();
        executeTask(importTask, startFolder);
	}

    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ( this.intentCallBack != null ) {
			this.intentCallBack.onResult(resultCode, data);
		}
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {		
        inflater.inflate(R.menu.library_menu, menu);

        UiUtils.Action toggleListener = () -> {

            if ( switcher.getDisplayedChild() == 0 ) {
                bookAdapter = new BookCaseAdapter();
                bookCaseView.setAdapter(bookAdapter);
                config.setLibraryView(LibraryView.BOOKCASE);
            } else {
                bookAdapter = new BookListAdapter(getActivity());
                listView.setAdapter(bookAdapter);
                config.setLibraryView(LibraryView.LIST);
            }

            switcher.showNext();
            refreshView();
        };

		// BVApp-Comment: 22/jan/2016: Add BV app specific menu handlers
		onMenuPress( menu, R.id.logout_action ).thenDo(() -> MenuActionHandler.handleLogout(getActivity()));
		onMenuPress( menu, R.id.change_password_action ).thenDo(() -> MenuActionHandler.handleChangePassword(getActivity()));
		onMenuPress( menu, R.id.technical_support_action ).thenDo(() -> MenuActionHandler.handleTechnicalSupport(getActivity()));

        onMenuPress( menu, R.id.shelves_view ).thenDo( toggleListener );
        onMenuPress( menu, R.id.list_view ).thenDo( toggleListener );

		// BVApp-Comment: 21/Sep/2015: Scan books button converted into download books button.
		// Instead of scanning and importing books from a local directory, it provides the option to download the books.
        //// onMenuPress( menu, R.id.scan_books ).thenDo( this::showImportDialog );
		onMenuPress( menu, R.id.scan_books ).thenDo( this::buildDownloadQuestionDialogForBookUpdates );
        onMenuPress( menu, R.id.about ).thenDo( dialogFactory.buildAboutDialog()::show );

        onMenuPress( menu, R.id.profile_day ).thenDo(() -> switchToColourProfile(ColourProfile.DAY) );
        onMenuPress( menu, R.id.profile_night ).thenDo(() -> switchToColourProfile(ColourProfile.NIGHT) );

        this.searchMenuItem = menu.findItem(R.id.menu_search);

        if (searchMenuItem != null) {
            final SearchView searchView = (SearchView) searchMenuItem.getActionView();

            if (searchView != null) {

                searchView.setOnQueryTextListener( UiUtils.onQuery( this::performSearch ));
                searchMenuItem.setOnActionExpandListener( onCollapse(() -> performSearch("")));

            } else {
                searchMenuItem.setOnMenuItemClickListener( item -> {
                    dialogFactory.showSearchDialog(R.string.search_library, R.string.enter_query, this::performSearch);
                    return false;
                });
            }
        }

        // Only show open file item if we have a file manager installed
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (isIntentAvailable(getActivity(), intent)) {
            onMenuPress( menu, R.id.open_file ).thenDo( this::launchFileManager );
        } else {
            menu.findItem(R.id.open_file).setVisible(false);
        }

	}

    private void launchFileManager() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");

        intent.addCategory(Intent.CATEGORY_OPENABLE);

        this.intentCallBack = (int resultCode, Intent data)  -> {
            if ( resultCode == Activity.RESULT_OK && data != null ) {
                Intent readingIntent = new Intent( getActivity(), ReadingActivity.class);
                readingIntent.setData(data.getData());
                getActivity().setResult(Activity.RESULT_OK, readingIntent);

                getActivity().startActivityIfNeeded(readingIntent, 99);
            }
        };

        try {
            startActivityForResult(intent, REQUEST_CODE_GET_CONTENT);
        } catch (ActivityNotFoundException e) {
            // No compatible file manager was found.
            Toast.makeText(getActivity(), getString(R.string.install_oi),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void onSearchRequested() {
        if ( this.searchMenuItem != null && searchMenuItem.getActionView() != null ) {
            this.searchMenuItem.expandActionView();
            this.searchMenuItem.getActionView().requestFocus();
        } else {
            dialogFactory.showSearchDialog(R.string.search_library, R.string.enter_query, this::performSearch);
        }
    }

    private void performSearch(String query) {
        if ( query != null ) {
            setSupportProgressBarIndeterminateVisibility(true);
            this.taskQueue.jumpQueueExecuteTask(new LoadBooksTask(config.getLastLibraryQuery(), query));
        }
    }
	
	private void switchToColourProfile( ColourProfile profile ) {
		config.setColourProfile(profile);
		Intent intent = new Intent(getActivity(), LibraryActivity.class);
		startActivity(intent);
		onStop();
		getActivity().finish();
	}
	
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		boolean bookCaseActive = config.getLibraryView() == LibraryView.BOOKCASE;
		
		menu.findItem(R.id.shelves_view).setVisible(! bookCaseActive);
		menu.findItem(R.id.list_view).setVisible(bookCaseActive);
		menu.findItem(R.id.profile_day).setVisible(config.getColourProfile() == ColourProfile.NIGHT);
		menu.findItem(R.id.profile_night).setVisible(config.getColourProfile() == ColourProfile.DAY);
	}
	
	private void showImportDialog() {
		AlertDialog.Builder builder;		
		
		LayoutInflater inflater = PlatformUtil.getLayoutInflater(getActivity());
		final View layout = inflater.inflate(R.layout.import_dialog, null);
		final RadioButton scanSpecific = (RadioButton) layout.findViewById(R.id.radioScanFolder);
		final TextView folder = (TextView) layout.findViewById(R.id.folderToScan);
		final CheckBox copyToLibrary = (CheckBox) layout.findViewById(R.id.copyToLib);		
		final Button browseButton = (Button) layout.findViewById(R.id.browseButton);

		Option<File> storageBase = config.getStorageBase();
		if ( isEmpty(storageBase) ) {
			return;
		}

		folder.setOnClickListener( v ->	scanSpecific.setChecked(true) );

		// Copy scan settings from the prefs
		copyToLibrary.setChecked( config.getCopyToLibraryOnScan() );
		scanSpecific.setChecked( config.getUseCustomScanFolder() );
		folder.setText( config.getScanFolder() );

		builder = new AlertDialog.Builder(getActivity());
		builder.setView(layout);

        this.intentCallBack = (int resultCode, Intent data) -> {
            if ( resultCode == Activity.RESULT_OK && data != null ) {
                folder.setText(data.getData().getPath());
            }
        };
		
		browseButton.setOnClickListener(v -> {
            scanSpecific.setChecked(true);
            Intent intent = new Intent(getActivity(), FileBrowseActivity.class);
            intent.setData( Uri.parse(folder.getText().toString() ));
            startActivityForResult(intent, 0);
        });
		
		builder.setTitle(R.string.import_books);

        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            dialog.dismiss();

	    /* Update settings */
	    config.setUseCustomScanFolder( scanSpecific.isChecked() );
	    config.setCopyToLibraryOnScan( copyToLibrary.isChecked() );

            File folderToScan;
            if ( scanSpecific.isChecked() ) {
		String path = folder.getText().toString();
                folderToScan = new File(path);
		config.setScanFolder(path); /* update custom path only if used */
            } else {
                File default_storage = storageBase.unsafeGet();
                folderToScan = new File(default_storage.getAbsolutePath());
            }

            startImport(folderToScan, copyToLibrary.isChecked());
        });

		builder.setNegativeButton(android.R.string.cancel, null);
		
		builder.show();
	}	
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("import_q", askedUserToImport);
	}
	
	@Override
	public void onStop() {
		this.libraryService.close();	
		this.waitDialog.dismiss();
		this.importDialog.dismiss();
		super.onStop();
	}
	
	public void onBackPressed() {
		getActivity().finish();			
	}	
	
	@Override
	public void onPause() {
		
		this.bookAdapter.clear();
		//We clear the list to free up memory.

        this.taskQueue.clear();
		this.clearCoverCache();
		
		super.onPause();
	}
	
	
	@Override
	public void onResume() {
		super.onResume();				
		
		LibrarySelection lastSelection = config.getLastLibraryQuery();
		
		ActionBar actionBar = getSherlockActivity().getSupportActionBar();
		
		if (actionBar.getSelectedNavigationIndex() != lastSelection.ordinal() ) {
			actionBar.setSelectedNavigationItem(lastSelection.ordinal());
		} else {
            executeTask(new LoadBooksTask(lastSelection));
		}
	}

    @Override
    public void importCancelled(int booksImported, List<String> failures, boolean emptyLibrary, boolean silent) {
        LOG.debug("Got importCancelled() ");
        afterImport( booksImported, failures, emptyLibrary, silent, true );
    }

    @Override
	public void importComplete(int booksImported, List<String> errors, boolean emptyLibrary, boolean silent) {
        LOG.debug("Got importComplete() ");
        afterImport(booksImported, errors, emptyLibrary, silent, false);
	}

    private void afterImport(int booksImported, List<String> errors, boolean emptyLibrary, boolean silent,
                             boolean cancelledByUser ) {

        if ( !isAdded() || getActivity() == null ) {
            return;
        }

        if ( silent ) {
            if ( booksImported > 0 ) {
                //Schedule refresh without clearing the queue
                executeTask(new LoadBooksTask(config.getLastLibraryQuery()));
            }
            return;
        }

        importDialog.hide();

        //If the user cancelled the import, don't bug him/her with alerts.
        if ( (! errors.isEmpty()) ) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.import_errors);

            builder.setItems( errors.toArray(new String[errors.size()]), null );

            builder.setNeutralButton(android.R.string.ok, (dialog, which) -> dialog.dismiss() );

            builder.show();
        }

        listView.setKeepScreenOn(oldKeepScreenOn);

        if ( booksImported > 0 ) {

            //Switch to the "recently added" view.
            if (getSherlockActivity().getSupportActionBar().getSelectedNavigationIndex() == LibrarySelection.LAST_ADDED.ordinal() ) {
                loadView(LibrarySelection.LAST_ADDED, "importComplete()");
            } else {
                getSherlockActivity().getSupportActionBar().setSelectedNavigationItem(LibrarySelection.LAST_ADDED.ordinal());
            }
        } else if ( ! cancelledByUser ) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.no_books_found);

            if ( emptyLibrary ) {
				// BVApp-Comment: 11/Oct/2015: If no books found in download and no books in library, then don't give option to download, but just show the message.
                //// builder.setMessage( getString(R.string.no_bks_fnd_text2) );
				//// builder.setPositiveButton(android.R.string.yes, (dialogInterface, i) ->
				//// 		((PageTurnerActivity) getSherlockActivity()).launchActivity(CatalogActivity.class));
				//// builder.setNegativeButton(android.R.string.no, null);
				builder.setMessage(getString(R.string.could_not_download_try_again));
				builder.setNeutralButton(android.R.string.ok, ( dialog, which) -> dialog.dismiss() );
            } else {
				// BVApp-Comment: 11/Oct/2015: Updating the correct message.
                //// builder.setMessage( getString(R.string.no_new_books_found));
				builder.setMessage( getString(R.string.no_new_book_updates_found));
                builder.setNeutralButton(android.R.string.ok, ( dialog, which) -> dialog.dismiss() );
            }

            builder.show();

        }

    }
	
	
	@Override
	public void importFailed(String reason, boolean silent) {

        LOG.debug("Got importFailed()");
		
		if (silent || !isAdded() || getActivity() == null ) {
			return;
		}
		
		importDialog.hide();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// BVApp-Comment: 21/Sep/2015: Showing download failed error instead of import failed error.
		//// builder.setTitle(R.string.import_failed);
		// TODO: The title needs to be changed based on error context... Set title to import failed or download failed based on the error context.
		builder.setTitle(R.string.download_failed);
		builder.setMessage(reason);
		builder.setNeutralButton(android.R.string.ok, null);
		builder.show();
	}
	
	@Override
	public void importStatusUpdate(String update, int actualProgress, int maxProgress, boolean silent) {
		
		if (silent || !isAdded() || getActivity() == null ) {
			return;
		}
		
		importDialog.setMessage(update);
		// BVApp-Comment: 21/Sep/2015: Added progress bar capability to import progress dialog.
		importDialog.setMax(maxProgress);
		importDialog.setProgress(actualProgress);
	}
	
	public void onAlphabetBarClick( KeyedQueryResult<LibraryBook> result, Character c ) {

		result.getOffsetFor(toUpperCase(c)).forEach( index -> {
			if ( alphabetAdapter != null ) {
				alphabetAdapter.setHighlightChar(c);
			}

			if ( config.getLibraryView() == LibraryView.BOOKCASE ) {
				this.bookCaseView.setSelection(index);
			} else {
				this.listView.setSelection(index);
			}
		});
	}	
	
	
	/**
	 * Based on example found here:
	 * http://www.vogella.de/articles/AndroidListView/article.html
	 * 
	 * @author work
	 *
	 */
	private class BookListAdapter extends KeyedResultAdapter {	
		
		private Context context;		
		
		public BookListAdapter(Context context) {
			this.context = context;
		}		
		
		@Override
		public View getView(int index, final LibraryBook book, View convertView,
				ViewGroup parent) {
			
			View rowView;
			
			if ( convertView == null ) {			
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				rowView = inflater.inflate(R.layout.book_row, parent, false);
			} else {
				rowView = convertView;
			}			
			
			TextView titleView = (TextView) rowView.findViewById(R.id.bookTitle);
			TextView authorView = (TextView) rowView.findViewById(R.id.bookAuthor);
			TextView dateView = (TextView) rowView.findViewById(R.id.addedToLibrary);
			TextView progressView = (TextView) rowView.findViewById(R.id.readingProgress);
			
			final ImageView imageView = (ImageView) rowView.findViewById(R.id.bookCover);
						
			String authorText = String.format(getString(R.string.book_by),
					book.getAuthor().getFirstName() + " " + book.getAuthor().getLastName() );
			
			authorView.setText(authorText);
			titleView.setText(book.getTitle());
			
			if ( book.getProgress() > 0 ) {
				progressView.setText( "" + book.getProgress() + "%");
			} else {
				progressView.setText("");
			}

			// BVApp-Comment: 31/Jan/2016: Changed the label from "added_to_lib" to "downloaded_on_date".
			String dateText = String.format(getString(R.string.downloaded_on_date),
					DATE_FORMAT.format(book.getAddedToLibrary()));
			dateView.setText( dateText );
			
			loadCover(imageView, book, index);			
			
			return rowView;
		}	
	
	}

    private void loadView( LibrarySelection selection, String from ) {
        LOG.debug("Loading view: " + selection + " from " + from);
        this.taskQueue.clear();
        executeTask(new LoadBooksTask(selection));
    }

    private void refreshView() {
        LOG.debug("View refresh requested");
        loadView(config.getLastLibraryQuery(), "refreshView()");
    }

    /**
     * Called after books have been deleted.
     * @param numberOfDeletedBooks
     */
    private void booksDeleted(int numberOfDeletedBooks) {

        LOG.debug("Got " + numberOfDeletedBooks + " deleted books.");

        if ( numberOfDeletedBooks > 0 ) {

            //Schedule a refresh without clearing the task queue
            executeTask(new LoadBooksTask(config.getLastLibraryQuery()));
        }
    }

    private void loadCover( ImageView imageView, LibraryBook book, int index ) {
		Drawable draw = coverCache.get(book.getFileName());
		
		if ( draw != null ) {
			imageView.setImageDrawable(draw);
		} else {
			
			imageView.setImageDrawable(backupCover);
			
			if ( book.getCoverImage() != null ) {				
				callbacks.add( new CoverCallback(book, index, imageView ) );
			}
		}
	}	
	
	private class CoverScrollListener implements AbsListView.OnScrollListener {
		
		private Runnable lastRunnable;
		private Character lastCharacter;

        private Drawable holoDrawable;

        public  CoverScrollListener() {
            try {
                this.holoDrawable = getResources().getDrawable(R.drawable.list_activated_holo);
            } catch (IllegalStateException i) {
                //leave it null
            }
        }
		
		@Override
		public void onScroll(AbsListView view, final int firstVisibleItem,
				final int visibleItemCount, final int totalItemCount) {
			
			if ( visibleItemCount == 0  ) {
				return;
			}
			
			if ( this.lastRunnable != null ) {
				handler.removeCallbacks(lastRunnable);
			}

            this.lastRunnable = () -> {

                if ( bookAdapter.isKeyed() ) {

                    String key = bookAdapter.getKey(firstVisibleItem).getOrElse("");

                    if (key.length() > 0) {
                        Character keyChar = toUpperCase(key.charAt(0));

                        if (keyChar.equals(lastCharacter)) {

                            lastCharacter = keyChar;
                            List<Character> alphabet = bookAdapter.getAlphabet();

                            //If the highlight-char is already set, this means the
                            //user clicked the bar, so don't scroll it.
                            if (alphabetAdapter != null && !keyChar.equals(alphabetAdapter.getHighlightChar())) {
                                alphabetAdapter.setHighlightChar(keyChar);
                                alphabetBar.setSelection(alphabet.indexOf(keyChar));
                            }

                            for (int i = 0; i < alphabetBar.getChildCount(); i++) {
                                View child = alphabetBar.getChildAt(i);
                                if (child.getTag().equals(keyChar)) {
                                    child.setBackgroundDrawable(holoDrawable);
                                } else {
                                    child.setBackgroundDrawable(null);
                                }
                            }
                        }
                    }
                }

                List<CoverCallback> localList = new ArrayList<>( callbacks );
                callbacks.clear();

                int lastVisibleItem = firstVisibleItem + visibleItemCount - 1;

                LOG.debug( "Loading items " + firstVisibleItem + " to " + lastVisibleItem + " of " + totalItemCount );

                for ( CoverCallback callback: localList ) {
                    if ( callback.viewIndex >= firstVisibleItem && callback.viewIndex <= lastVisibleItem ) {
                        callback.run();
                    }
                }

            };
				
			handler.postDelayed(lastRunnable, 550);			
		}
		
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
					
		}

	}

	private class CoverCallback {
		protected LibraryBook book;
		protected int viewIndex;
		protected ImageView view;

		public CoverCallback(LibraryBook book, int viewIndex, ImageView view) {
			this.book = book;
			this.view = view;
			this.viewIndex = viewIndex;
		}

		public void run() {
			try {
                getCover(book).forEach( view::setImageDrawable );
            } catch (IllegalStateException i) {
                //Do nothing, happens when we're no longer attached.
            }
		}
	}


	private class BookCaseAdapter extends KeyedResultAdapter {
				
		@Override
		public View getView(final int index, final LibraryBook object, View convertView,
				ViewGroup parent) {
			
			View result;
		
			if ( convertView == null ) {				
				LayoutInflater inflater = PlatformUtil.getLayoutInflater(getActivity());
				result = inflater.inflate(R.layout.bookcase_row, parent, false);
				
			} else {
				result = convertView;
			}			
			
			result.setTag(index);
			
			result.setOnClickListener( v -> LibraryFragment.this.onItemClick(null, null, index, 0) );
			result.setOnLongClickListener( v -> LibraryFragment.this.onItemLongClick(null, null, index, 0));
			
			final ImageView image = (ImageView) result.findViewById(R.id.bookCover);
			image.setImageDrawable(backupCover);
			TextView text = (TextView) result.findViewById(R.id.bookLabel);
			text.setText( object.getTitle() );
			text.setBackgroundResource(R.drawable.alphabet_bar_bg_dark);			
			
			loadCover(image, object, index);		
			
			return result;
		}
		
	}
	
	private void buildImportQuestionDialog() {
		
		if ( importQuestion != null || ! isAdded() ) {
			return;
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.no_books_found);
		builder.setMessage( getString(R.string.scan_bks_question) );

        builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {
            dialog.dismiss();
            showImportDialog();
        });

        builder.setNegativeButton(android.R.string.no, (dialog, which ) -> {
            dialog.dismiss();
            importQuestion = null;
        });

        this.importQuestion = builder.create();
	}

	// BVApp-Comment: 21/Sep/2015: Added new method to build download question dialog at the application startup.
	private void buildDownloadQuestionDialogAtAppStart() {
		if ( this.downloadQuestion != null || ! isAdded() ) {
			return;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(getString(R.string.no_books_found));
		builder.setMessage(getString(R.string.download_bks_question));

		builder.setPositiveButton(android.R.string.yes, (dialog, which) -> {
			dialog.dismiss();
			startDownloadBooks();
		});

		builder.setNegativeButton(android.R.string.no, (dialog, which ) -> {
			dialog.dismiss();
			this.downloadQuestion = null;
		});

		this.downloadQuestion = builder.create();
	}

	// BVApp-Comment: 21/Sep/2015: Added new method to build download question dialog for book updates.
	private void buildDownloadQuestionDialogForBookUpdates() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		if(AppUtils.isEmptyLibrary(this.libraryService)) {
			builder.setTitle(getString(R.string.no_books_found));
			builder.setMessage(getString(R.string.download_bks_question));
		} else {
			builder.setTitle(getString(R.string.download_books));
			builder.setMessage(getString(R.string.download_update_bks_question));
		}

		builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
			dialog.dismiss();
			startDownloadBooks();
		});

		builder.setNegativeButton(android.R.string.cancel, (dialog, which ) -> {
			dialog.dismiss();
		});

		builder.show();
	}

	// BVApp-Comment: 21/Sep/2015: Added new method to start downloading the books.
	private void startDownloadBooks() {
		// BVApp-Comment: 21/Sep/2015: Just passing library folder for folderToScan and enabling copyToLibrary flag.
		// This is just dummy passing of values, anyways the NetDownloadTask has its own functionality to
		// download the books and copy them to the library.
		File folderToScan = config.getLibraryFolder().unsafeGet();
		boolean copyToLibrary = true;
		startImport(folderToScan, copyToLibrary);
	}
	
	private void setAlphabetBarVisible( boolean visible ) {
		
		int vis = visible ? View.VISIBLE : View.GONE; 
		
		alphabetBar.setVisibility(vis);
		alphabetDivider.setVisibility(vis);		
		listView.setFastScrollEnabled(visible);
	}

    private void setSupportProgressBarIndeterminateVisibility(boolean enable) {
        SherlockFragmentActivity activity = getSherlockActivity();
        if ( activity != null) {
            LOG.debug("Setting progress bar to " + enable );
            activity.setSupportProgressBarIndeterminateVisibility(enable);
        } else {
            LOG.debug("Got null activity.");
        }
    }

    private boolean onNavigationItemSelected(int pos, long arg1) {

        LibrarySelection newSelections = LibrarySelection.values()[pos];

        if ( newSelections != config.getLastLibraryQuery() ) {
            config.setLastLibraryQuery(newSelections);

            bookAdapter.clear();
            loadView(newSelections, "onNavigationItemSelected()");
        }

        return false;
    }

	private class AlphabetAdapter extends ArrayAdapter<Character> {
		
		private List<Character> data;
		
		private Character highlightChar;
		
		public AlphabetAdapter(Context context, int layout, int view, List<Character> input ) {
			super(context, layout, view, input);
			this.data = input;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			
			Character tag = data.get(position);
			view.setTag( tag );
			
			if ( tag.equals(highlightChar) ) {
				view.setBackgroundDrawable( getResources().getDrawable(R.drawable.list_activated_holo));
			} else {
				view.setBackgroundDrawable(null);
			}
			
			return view;
		}
		
		public void setHighlightChar(Character highlightChar) {
			this.highlightChar = highlightChar;
		}
		
		public Character getHighlightChar() {
			return highlightChar;
		}
	}

    private void loadQueryData( QueryResult<LibraryBook> result ) {
        if ( !isAdded() || getActivity() == null ) {
            return;
        }

        bookAdapter.setResult(result);

        if ( result instanceof KeyedQueryResult && result.getSize() >= ALPHABET_THRESHOLD ) {

            final KeyedQueryResult<LibraryBook> keyedResult = (KeyedQueryResult<LibraryBook>) result;

            alphabetAdapter = new AlphabetAdapter(getActivity(),
                    R.layout.alphabet_line, R.id.alphabetLabel,	keyedResult.getAlphabet() );

            alphabetBar.setAdapter(alphabetAdapter);

            alphabetBar.setOnItemClickListener( (a, b, index, c) ->
                    onAlphabetBarClick(keyedResult, keyedResult.getAlphabet().get(index) ));

            setAlphabetBarVisible(true);
        } else {
            alphabetAdapter = null;
            setAlphabetBarVisible(false);
        }
    }

	private class LoadBooksTask extends QueueableAsyncTask<String, Integer, QueryResult<LibraryBook>> {
		
		private Configuration.LibrarySelection sel;
        private String filter;

        public LoadBooksTask(LibrarySelection selection) {
            this.sel = selection;
        }

        public LoadBooksTask(LibrarySelection selection, String filter ) {
            this(selection);
            this.filter = filter;
        }

        @Override
        public void doOnPreExecute() {
            if ( this.filter == null )  {
			    coverCache.clear();
            }
		}
		
		@Override
		public Option<QueryResult<LibraryBook>> doInBackground(String... params) {
			
			Exception storedException = null;

            String query = this.filter;
			
			for ( int i=0; i < 3; i++ ) {

				try {

					switch ( sel ) {			
					case LAST_ADDED:
						return some(libraryService.findAllByLastAdded(query));
					case UNREAD:
						return some(libraryService.findUnread(query));
					case BY_TITLE:
						return some(libraryService.findAllByTitle(query));
					case BY_AUTHOR:
						return some(libraryService.findAllByAuthor(query));
					default:
						return some(libraryService.findAllByLastRead(query));
					}
				} catch (SQLiteException sql) {
					storedException = sql;
					try {
						//Sometimes the database is still locked.
						Thread.sleep(1000);
					} catch (InterruptedException in) {}
				}				
			}
			
			LOG.error( "Failed after 3 attempts", storedException );
            return none();
		}

        @Override
        public void doOnPostExecute(Option<QueryResult<LibraryBook>> result) {

            result.match(r -> {

                loadQueryData(r);

                if (filter == null && sel == Configuration.LibrarySelection.LAST_ADDED && r.getSize() == 0 && !askedUserToImport) {
                    askedUserToImport = true;
					// BVApp-Comment: 21/Sep/2015: When application starts, if no books in the library, then provide the option to to download the books.
					// Instead of scanning and importing books from a local directory, it provides the option to download the books.
					//// buildImportQuestionDialog();
					//// importQuestion.show();
					buildDownloadQuestionDialogAtAppStart();
					downloadQuestion.show();
                }
            }, () -> Toast.makeText(context, R.string.library_failed, Toast.LENGTH_SHORT).show());

		}
		
	}
}
