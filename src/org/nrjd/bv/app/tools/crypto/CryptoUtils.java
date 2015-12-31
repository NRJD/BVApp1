/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.tools.crypto;

import java.util.HashSet;
import java.util.Set;

import org.nrjd.bv.app.util.ArrayUtils;
import org.nrjd.bv.app.util.StringUtils;


/**
 * Crypto utilities.
 */
public class CryptoUtils {
    //////// Constants for excluding the encryption for epub file entries.
    //// Do not encrypt the core epub control file such as container such as "META-INF/container.xml",
    //// table of contents such as "toc.ncx" and package resource files such as "content.opf".
    //// So exclude cryption for files under META-INF folder, and exclude encryption for
    //// files with extension ".ncx", ".opf", because the file names for table of contents and
    //// package resource files can be anything, but extensions remain ".ncx" and ".opf".
    //// Also font files, image files, audio/video files, css and javascript files needs to be
    //// excluded from the encryption because they are directly referred in html files and
    //// webview needs to be able to load them directly without having to decrypt them
    //// when it is refering to them from the html pages.

    // Exclude encryption for files in META-INF folder.
    private static final String META_INF_RESOURCE_URI = "META-INF/".toLowerCase();
    // Exclude encryption for common file names that are generally used for table of contents and package resource files.
    private static final Set<String> EXCLUDE_FILE_NAMES = constructCaseInsensitiveSet("mimetype", "toc.ncx", "book.opf", "content.opf");
    // Exclude list for file extensions for control files such as ncx and package resource files,
    // and exclude list for file extensions for font, image and audio/video files.
    private static final Set<String> CONTROL_FILE_EXTENSIONS = constructCaseInsensitiveSet(".ncx", ".opf");
    private static final Set<String> FONT_FILE_EXTENSIONS = constructCaseInsensitiveSet(".ttf", ".otf", ".woff");
    private static final Set<String> IMAGE_FILE_EXTENSIONS = constructCaseInsensitiveSet(".jpg", ".jpeg", ".png", ".gif", ".svg");
    private static final Set<String> AUDIO_VIDEO_FILE_EXTENSIONS = constructCaseInsensitiveSet(".mp3", ".mp4", ".ogg", ".smil", ".xpgt", ".pls");
    private static final Set<String> SCRIPT_FILE_EXTENSIONS = constructCaseInsensitiveSet(".css", ".js");
    //// File extensions set for excluding encryption.
    private static final Set<String> EXCLUDE_EXTENSIONS =
        concat(CONTROL_FILE_EXTENSIONS, FONT_FILE_EXTENSIONS, IMAGE_FILE_EXTENSIONS, AUDIO_VIDEO_FILE_EXTENSIONS, SCRIPT_FILE_EXTENSIONS);
    
    // Include set for encryption.
    // Baring about exclusions, we exclude any other file which doesn't cover this include list.
    // In this way, if any xml files exists in the epub archive, they will not be encrypted.
    // If any files without extension exists in the epub archive, they will not be encrypted in this way.
    private static final Set<String> HTML_FILE_EXTENSIONS = constructCaseInsensitiveSet(".htm", ".html", ".xhtml", ".epub", ".bk");
    private static final Set<String> INCLUDE_EXTENSIONS = concat(HTML_FILE_EXTENSIONS);

    private CryptoUtils() {
    }

    public static boolean isExcludeFromEncryption(String resourcePath) {
        if (StringUtils.isNotNullOrEmpty(resourcePath)) {
            String resourcePathLower = resourcePath.trim().toLowerCase();
            // Check if META-INF resource, then exclude from encryption.
            if (resourcePathLower.indexOf(META_INF_RESOURCE_URI) >= 0) {
                return true; // Exclude from encryption.
            }
            // Exclude the specified exclude file names from encryption.
            for (String excludeFileName : EXCLUDE_FILE_NAMES) {
                if (resourcePathLower.endsWith(excludeFileName)) {
                    return true; // Exclude from encryption.
                }
            }
            // Exclude the specified exclude file extensions from encryption.
            for (String excludeExtension : EXCLUDE_EXTENSIONS) {
                if (resourcePathLower.endsWith(excludeExtension)) {
                    return true; // Exclude from encryption.
                }
            }
            // Include the specified include file extensions into encryption.
            for (String includeExtension : INCLUDE_EXTENSIONS) {
                if (resourcePathLower.endsWith(includeExtension)) {
                    return false; // Include into encryption.
                }
            }
            // Control comes here for those files which do not corresponds to html files or main webview files.
            // For example, for any .xml files, or files with any other extensions or files without any extension.
            // These files should be excluded from the encryption.
            return true; // Exclude from encryption.
        }
        return false; // Exclude from encryption if the file is null or blank.
    }

    private static Set<String> constructCaseInsensitiveSet(String... values) {
        Set<String> caseInsensitiveSet = new HashSet<String>();
        Set<String> set = ArrayUtils.convertToSet(values);
        if (set != null) {
            for (String string : set) {
                if (string != null) {
                    caseInsensitiveSet.add(string.toLowerCase());
                }
            }
        }
        return caseInsensitiveSet;
    }

    private static Set<String> concat(Set<String>... setList) {
        Set<String> concatenatedSet = new HashSet<String>();
        if (setList != null) {
            for (Set<String> set : setList) {
                if (set != null) {
                    concatenatedSet.addAll(set);
                }
            }
        }
        return concatenatedSet;
    }
}
