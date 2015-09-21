/*
 * Copyright (C) 2013 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakthi Vriksha application.
 */

package com.nrjd.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

import com.google.inject.Inject;
import jedi.option.None;
import jedi.option.Option;

import net.nightwhistler.pageturner.Configuration;
import net.nightwhistler.pageturner.library.ImportCallback;
import net.nightwhistler.pageturner.library.LibraryService;
import net.nightwhistler.pageturner.scheduling.QueueableAsyncTask;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.service.MediatypeService;
import roboguice.RoboGuice;

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
import java.nio.charset.Charset;
import java.util.*;

import static jedi.functional.FunctionalPrimitives.isEmpty;

public class NetDownloadTask extends QueueableAsyncTask<File, Long, Void> implements OnCancelListener {

    private static final Logger LOG = LoggerFactory.getLogger(NetDownloadTask.class);
    private static final boolean COPY_TO_LIBRARY = true;
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

        List<String> bookUrls = AppConstants.getBookUrls();
        this.totalBooks = bookUrls.size();
        for (int bookIndex = 0; bookIndex < this.totalBooks; bookIndex++) {
            if (StringUtils.isNullOrEmpty(this.downloadFailedMessage)) {
                this.currentBookNumber = bookIndex + 1;
                File downloadedFile = downloadBook(bookUrls.get(bookIndex));
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

    private File downloadBook(String bookUrl) {
        // Validations.
        // If bookUrl specified is null or empty, then return from here.
        if (StringUtils.isNullOrEmpty(bookUrl)) {
            return null;
        }
        // Download the book.
        String fileName = null;
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

                // Default Charset for android is UTF-8*
                String charsetName = Charset.defaultCharset().name();
                if (!Charset.isSupported(charsetName)) {
                    LOG.warn("{} is not a supported Charset. Will fall back to UTF-8", charsetName);
                    charsetName = AppConstants.UTF8;
                }

                File destFile = null;
                try {
                    destFile = new File(destFolder, URLDecoder.decode(fileName, charsetName));
                } catch (UnsupportedEncodingException e) {
                    // Won't ever reach here
                    throw new AssertionError(e);
                }

                if (destFile.exists()) {
                    destFile.delete();
                }

                downloadBook(response, destFile);
                return destFile;
            } else {
                // TODO: To fix download errors different from import errors. Fix with user errors.
                this.downloadFailedMessage = "Download failed: " + fileName + ": " + response.getStatusLine().getReasonPhrase();
                LOG.error("Download failed: " + fileName + ": " + response.getStatusLine().getReasonPhrase());
            }
        } catch (Exception e) {
            // TODO: To fix download errors different from import errors. Fix with user errors.
            this.downloadFailedMessage = "Download failed: " + fileName;
            LOG.error("Download failed: " + fileName, e);
        }
        return null;
    }

    private void downloadBook(HttpResponse response, File destFile) throws Exception {
        // fileSize is used for calculating download progress.
        long fileSize = response.getEntity().getContentLength();
        Header type = response.getEntity().getContentType();
        long downloadedSize = 0;
        InputStream in = null;
        FileOutputStream f = null;
        try {
            in = response.getEntity().getContent();
            f = new FileOutputStream(destFile);
            byte[] buffer = new byte[4098];
            int readSize = 0;
            while ((readSize = in.read(buffer)) > 0 && !isCancelled()) {
                // Make sure the user can cancel the download.
                if (isCancelled()) {
                    return;
                }
                f.write(buffer, 0, readSize);
                downloadedSize += readSize;
                final long MAX_PROGRESS = 98; // Remaining 2% is for import book.
                long progress = (downloadedSize * MAX_PROGRESS) / fileSize;
                publishProgress(fileSize, downloadedSize, progress);
            }
        } finally {
            CommonUtils.closeQuietly(f);
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
                publishProgress(file.length(), file.length(), (long) 100);
                CommonUtils.sleep("DownloadCheck", 5);
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
        long fileSize = values[0];
        long downloadedSize = values[1];
        long progress = values[2];
        // String label = context.getString(R.string.download_file_progress);
        // String label = "Downloading %1$d of %2$d: Progress %3$d%\nTotal size %4$d download size %5$d";
        // String message = String.format(label, this.totalBooks, this.currentBookNumber, progress, fileSize, downloadedSize);
        String message = "Downloading " + this.currentBookNumber + " of " + this.totalBooks + " books" +
                "\nProgress: " + progress +
                "\nDownloaded " + downloadedSize + " of " + fileSize + " bytes";
        LOG.debug(message);
        callBack.importStatusUpdate(message, silent);
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
