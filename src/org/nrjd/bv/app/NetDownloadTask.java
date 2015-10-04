/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */
package org.nrjd.bv.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

import com.google.inject.Inject;
import org.nrjd.bv.app.reg.BookEntry;
import org.nrjd.bv.app.reg.RegistryData;
import org.nrjd.bv.app.reg.RegistryDataUtils;
import org.nrjd.bv.app.reg.RegistryHandler;
import org.nrjd.bv.app.util.CommonUtils;
import org.nrjd.bv.app.util.FileUtils;
import org.nrjd.bv.app.util.StringUtils;

import net.nightwhistler.pageturner.Configuration;
import net.nightwhistler.pageturner.library.ImportCallback;
import net.nightwhistler.pageturner.library.LibraryService;
import net.nightwhistler.pageturner.scheduling.QueueableAsyncTask;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jedi.option.None;
import jedi.option.Option;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.service.MediatypeService;
import roboguice.RoboGuice;

import static jedi.functional.FunctionalPrimitives.isEmpty;


public class NetDownloadTask extends QueueableAsyncTask<File, Long, Void> implements OnCancelListener {
    private static final Logger LOG = LoggerFactory.getLogger(NetDownloadTask.class);

    private static final boolean COPY_TO_LIBRARY = true;
    private static final int DEFAULT_BUFFER_SIZE = 4098;

    private long DOWNLOAD_REGISTRY = 1;
    private long DOWNLOAD_BOOK = 2;
    private long IMPORT_BOOK = 3;
    private Context context;
    private LibraryService libraryService;
    private ImportCallback callBack;
    private Configuration config;
    private boolean copyToLibrary = true;
    private List<String> errors = new ArrayList<>();
    private int booksImported = 0;

    private boolean emptyLibrary;
    private boolean silent;

    private long totalBooks = 0;
    private long currentBookNumber = 0;
    private String currentBookName = null;
    private String downloadFailedMessage = null;

    @Inject
    private HttpClient http;

    public NetDownloadTask(Context context, LibraryService libraryService,
                           ImportCallback callBack, Configuration config, boolean copyToLibrary,
                           boolean silent) {

        this.context = context;
        this.libraryService = libraryService;
        this.callBack = callBack;
        this.copyToLibrary = copyToLibrary;
        this.config = config;
        this.silent = silent;
        RoboGuice.injectMembers(context, this);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        LOG.debug("User aborted download.");
        requestCancellation();
    }

    public boolean isSilent() {
        return this.silent;
    }

    public void setCallBack(ImportCallback callBack) {
        this.callBack = callBack;
    }

    @Override
    public Option<Void> doInBackground(File... params) {
        doInBackground();
        return new None();
    }

    private void doInBackground() {
        LOG.debug("Started downloading the files..");

        this.emptyLibrary = this.libraryService.findAllByTitle(null).getSize() == 0;

        /* Hack: don't run automated import on an empty database, since we explicitly ask the user to download. */
        if (silent && this.emptyLibrary) {
            return;
        }

        // Download registry data.
        RegistryHandler registryHandler = new RegistryHandler();
        String registryUrl = registryHandler.getRegistryUrl();
        File registryFile = null;
        RegistryData registryData = null;
        try {
            // TODO: Now null "downloadFile" is used as the machanism to check the success of download and then delete the file.
            // But even if the download goes incomplete, then also we need to cleanup the file.
            registryFile = downloadBook(registryUrl, null, DOWNLOAD_REGISTRY);
            if (registryFile != null) {
                String registryFileData = FileUtils.getFileData(registryFile);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Got registry file data:\n" + registryFileData);
                }
                if (registryFileData != null) {
                    registryData = RegistryDataUtils.parseRegistryXml(registryFileData);
                }
            }
        } catch (Exception e) {
            // TODO: Don't show internal exception messages to end users.
            this.downloadFailedMessage = "Error occurred while checking for book updates: " + e.getMessage();
            LOG.error(this.downloadFailedMessage, e);
            return;
        } finally {
            if (registryFile != null) {
                registryFile.delete();
            }
        }

        if (registryData == null) {
            // TODO: Need to modify this error message for end users.
            this.downloadFailedMessage = "No registry data available!";
            LOG.error(this.downloadFailedMessage);
            return;
        }

        List<BookEntry> bookEntries = registryData.getBookEntries();
        if ((bookEntries == null) || (bookEntries.size() < 1)) {
            // TODO: Need to modify this error message for end users.
            this.downloadFailedMessage = "No book entries available!";
            LOG.error(this.downloadFailedMessage);
            return;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Got registry data:\n" + registryData);
        }

        // Download books
        // TODO: Download only the new book updates.
        this.totalBooks = bookEntries.size();
        for (int bookIndex = 0; bookIndex < this.totalBooks; bookIndex++) {
            if (StringUtils.isNullOrEmpty(this.downloadFailedMessage)) {
                // TODO: Now null "downloadFile" is used as the machanism to check the success of download and then delete the file.
                // But even if the download goes incomplete, then also we need to cleanup the file.
                BookEntry bookEntry = bookEntries.get(bookIndex);
                String bookUrl = registryHandler.getBookUrl(bookEntry.getFileName());
                this.currentBookNumber = bookIndex + 1;
                this.currentBookName = bookEntry.getBookName();
                File downloadedFile = downloadBook(bookUrl, bookEntry, DOWNLOAD_BOOK);
                if (downloadedFile != null) {
                    try {
                        importBook(downloadedFile);
                    } finally {
                        // TODO: Delete temporary file.
                        downloadedFile.delete();
                    }
                }
            }
        }
    }

    private File downloadBook(String bookUrl, BookEntry bookEntry, long downloadFileType) {
        // Validations.
        // If bookUrl specified is null or empty, then return from here.
        if (StringUtils.isNullOrEmpty(bookUrl)) {
            return null;
        }
        // Download the book.
        String fileName = null;
        String bookName = ((bookEntry != null) ? bookEntry.getBookName() : fileName);
        try {
            LOG.debug("Downloading: " + bookUrl);

            fileName = bookUrl.substring(bookUrl.lastIndexOf('/') + 1);
            fileName = fileName.replaceAll("\\?|&|=", "_");

            if (fileName.trim().equals("")) {
                // As the file name is not mentioned in the url, we can't download this book. Return from here.
                LOG.error("Internal Error: Can't download: " + bookUrl + ": Book URL location doesn't contain valid file name.");
                return null;
            }

            HttpGet get = new HttpGet(bookUrl);
            get.setHeader("User-Agent", config.getUserAgent());
            HttpResponse response = this.http.execute(get);

            if (response.getStatusLine().getStatusCode() == 200) {
                Option<File> destFolderOption = config.getDownloadsFolder();
                if (isEmpty(destFolderOption)) {
                    throw new IllegalStateException("Could not get download folder!");
                }

                File destFolder = destFolderOption.unsafeGet();
                if (!destFolder.exists()) {
                    destFolder.mkdirs();
                }

                LOG.debug("Downloading books to destFolder: " + destFolder);

                File destFile = null;
                try {
                    destFile = new File(destFolder, URLDecoder.decode(fileName, AppConstants.UTF8));
                } catch (UnsupportedEncodingException e) {
                    // Won't ever reach here
                    throw new AssertionError(e);
                }

                if (destFile.exists()) {
                    destFile.delete();
                }

                downloadBook(response, destFile, bookEntry, downloadFileType);
                return destFile;
            } else {
                // TODO: Don't show internal exception messages to end users.
                this.downloadFailedMessage = "Failed to download the books: " + response.getStatusLine().getReasonPhrase();
                LOG.error(bookName + ": " + this.downloadFailedMessage);
            }
        } catch (Exception e) {
            // TODO: Don't show internal exception messages to end users.
            this.downloadFailedMessage = "Failed to download the books: " + e.getMessage();
            LOG.error(bookName + ": " + this.downloadFailedMessage, e);
        }
        return null;
    }

    private void downloadBook(HttpResponse response, File destFile, BookEntry bookEntry, long downloadFileType) throws Exception {
        // File size is used for calculating the download progress.
        // If we don't get proper content-length in http response, then assign it from book entry registry.
        long fileSize = response.getEntity().getContentLength();
        if ((fileSize < 0) && (bookEntry != null)) {
            LOG.debug("Got invalid content-length: " + fileSize + ": Assigning from book entry registry: " + bookEntry.getSize());
            fileSize = bookEntry.getSize();
        }
        Header type = response.getEntity().getContentType();
        long downloadedSize = 0;
        InputStream in = null;
        FileOutputStream fos = null;
        try {
            in = response.getEntity().getContent();
            fos = new FileOutputStream(destFile);
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int readSize = 0;
            while ((readSize = in.read(buffer)) > 0 && !isCancelled()) {
                // Make sure the user can cancel the download.
                if (isCancelled()) {
                    return;
                }
                fos.write(buffer, 0, readSize);
                downloadedSize += readSize;
                publishProgress(downloadFileType, fileSize, downloadedSize);
            }
        } finally {
            CommonUtils.closeQuietly(fos);
            CommonUtils.closeQuietly(in);
        }
        LOG.debug("Downloaded: " + destFile + ": type: " + type + ": " + downloadedSize + " bytes");
    }

    private boolean importBook(File file) {
        // TODO: Capture error message when file is null.
        if ((file != null) && (!isCancelled())) {
            try {
                EpubReader epubReader = new EpubReader();
                Book importedBook = epubReader.readEpubLazy(file.getAbsolutePath(), AppConstants.UTF8,
                        Arrays.asList(MediatypeService.mediatypes));
                libraryService.storeBook(file.getAbsolutePath(), importedBook, false, COPY_TO_LIBRARY);
                this.booksImported++;
                publishProgress(IMPORT_BOOK, file.length(), file.length());
                return true;
            } catch (Exception io) {
                if (!isCancelled()) {
                    errors.add(file + ": " + io.getMessage());
                    LOG.error("Error while reading book: " + file, io);
                } else {
                    LOG.info("Ignoring error since we were cancelled", io);
                }
            }
        }
        return false;
    }

    @Override
    public void doOnProgressUpdate(Long... values) {
        long step = values[0];
        long fileSize = values[1];
        long downloadedSize = values[2];
        long progress = (downloadedSize * 100) / fileSize;
        // String label = context.getString(R.string.download_file_progress);
        // String label = "Downloading %1$d of %2$d: Progress %3$d%\nTotal size %4$d download size %5$d";
        // String message = String.format(label, this.totalBooks, this.currentBookNumber, progress, fileSize, downloadedSize);
        String statusMessage = "Downloading " + this.currentBookNumber + " of " + this.totalBooks + " books";
        if (step == DOWNLOAD_REGISTRY) {
            statusMessage = "Checking for the book updates.."; // TODO: new label
        } else if (step == IMPORT_BOOK) {
            statusMessage = statusMessage + "\nImporting book " + this.currentBookNumber + ".."; // TODO: new label
        } else if (step == DOWNLOAD_BOOK) {
            if(StringUtils.isNotNullOrEmpty(this.currentBookName)) {
                statusMessage = statusMessage + "\n" + this.currentBookName;
            }
        }
        String message = statusMessage + "\nProgress: " + progress + "\nDownloaded " + downloadedSize + " of " + fileSize + " bytes";
        LOG.debug(message);
        callBack.importStatusUpdate(statusMessage, (int) downloadedSize, (int) fileSize, silent);
    }

    @Override
    public void doOnCancelled(Option<Void> none) {
        this.callBack.importCancelled(booksImported, errors, emptyLibrary, silent);
    }

    @Override
    public void doOnPostExecute(Option<Void> none) {
        LOG.debug("Import task completed, imported " + booksImported + " books.");
        if (downloadFailedMessage != null) {
            callBack.importFailed(downloadFailedMessage, silent);
        } else {
            this.callBack.importComplete(booksImported, errors, emptyLibrary, silent);
        }
    }
}
