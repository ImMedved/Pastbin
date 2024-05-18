import java.net.*;
import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.util.*;

public class MavenWrapperDownloader {

    private static final String WRAPPER_VERSION = "0.5.6";
    private static final boolean VERBOSE = Boolean.parseBoolean(System.getenv("MVNW_VERBOSE"));
    public static void main(String args[]) {
        log("Apache Maven Wrapper Downloader " + WRAPPER_VERSION);

        if (args.length != 1) {
            log(" - ERROR: Expected one argument: URL of the Maven Wrapper distribution zip file.");
            System.exit(1);
        }

        try {
            log(" - Downloading from: " + args[0]);

            File baseDirectory = new File(System.getProperty("user.dir"));
            log(" - User directory: " + baseDirectory.getAbsolutePath());

            File wrapperDirectory = new File(baseDirectory, ".mvn/wrapper");
            log(" - Creating wrapper directory: " + wrapperDirectory.getAbsolutePath());
            if (!wrapperDirectory.exists() && !wrapperDirectory.mkdirs()) {
                log(" - ERROR: Failed to create wrapper directory.");
                System.exit(1);
            }

            File zipFile = new File(wrapperDirectory, "maven-wrapper.zip");
            log(" - Downloading to: " + zipFile.getAbsolutePath());
            downloadFileFromURL(args[0], zipFile);

            log(" - Unzipping to: " + wrapperDirectory.getAbsolutePath());
            unzip(zipFile, wrapperDirectory);

            log(" - Deleting the zip file");
            if (!zipFile.delete()) {
                log(" - ERROR: Failed to delete the zip file.");
                System.exit(1);
            }

            log(" - Done");
        } catch (Exception e) {
            log(" - ERROR: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void log(String msg) {
        if (VERBOSE) {
            System.out.println(msg);
        }
    }

    private static void downloadFileFromURL(String urlString, File destination) throws Exception {
        URL website = new URL(urlString);
        ReadableByteChannel rbc;
        try (InputStream in = website.openStream(); FileOutputStream fos = new FileOutputStream(destination)) {
            rbc = Channels.newChannel(in);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        }
    }

    private static void unzip(File zipFile, File targetDirectory) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                File newFile = newFile(targetDirectory, entry);
                if (entry.isDirectory()) {
                    if (!newFile.isDirectory() && !newFile.mkdirs()) {
                        throw new IOException("Failed to create directory " + newFile);
                    }
                } else {
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        int len;
                        byte[] buffer = new byte[1024];
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
            }
        }
    }

    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());
        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }
        return destFile;
    }
}
