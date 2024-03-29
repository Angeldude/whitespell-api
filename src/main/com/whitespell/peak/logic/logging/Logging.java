package main.com.whitespell.peak.logic.logging;


import main.com.whitespell.peak.Server;
import main.com.whitespell.peak.logic.config.Config;

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by wwadewitte on 10/2/14.
 */
public class Logging {

    public static int errorsNotWrittenCount;

    public static void writeSampleErrors(int amount) {
        long start = Server.getMilliTime();
        for (int i = 0; i < amount; i++) {
            try {
                throw new Exception();
            } catch (Exception e) {
                log("CRUCIAL", e);
            }
        }
        long end = Server.getMilliTime();
        System.out.println("Write sample errors took: " + (end - start) + " ms");
    }

    public static boolean canLogToDisk(Object err) {
        long folderSize = 0;
        try {
            long start = Server.getMilliTime();
            folderSize = FileChecks.folderSize(new File(Config.ERROR_PATH)) / 1000000;
            if (folderSize > Config.MAX_ERROR_FOLDER_SIZE_MB) {
                errorsNotWrittenCount++;
                System.out.println("" + Config.ERROR_PATH + " is" + folderSize + "mb (" + errorsNotWrittenCount + ". Abort logging.");
                if (err instanceof Exception) {
                    Exception z = (Exception) err;
                    z.printStackTrace(System.out);
                } else if (err instanceof String) {
                    System.out.println(err);
                }
                return false;
            }
            long end = Server.getMilliTime();
            System.out.println("Checking log files took: " + (end - start) + " ms");
        } catch (Exception e) {
            System.out.println("Error in checking file of error folder");
            e.printStackTrace(System.out);
        }
        System.out.println("Folder size is " + folderSize + "mb");
        return true;
    }


    public static void log(String errorLevel, Exception e) {
        if (!canLogToDisk(e)) {
            return;
        }
        final Timestamp now = new Timestamp(Server.getMilliTime()); // 15 mins max
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        System.err.print("[" + errorLevel + " (version " + Config.SERVER_VERSION + ") " + sdf.format(now) + "]");
        e.printStackTrace(System.err);
    }

    public static void log(String errorLevel, String s) {
        if (!canLogToDisk(s)) {
            return;
        }

        final Timestamp now = new Timestamp(Server.getMilliTime()); // 15 mins max
        //synchronized(System.err) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        System.err.println("[" + errorLevel + " (version " + Config.SERVER_VERSION + ") " + sdf.format(now) + "] " + s);
    }


    public static class ErrorFile extends FileOutputStream {
        private final PrintStream errorStream = System.err;

        public ErrorFile() throws FileNotFoundException {
            super(ErrorFile.getErrorFile(null), true);
        }

        public ErrorFile(String folder) throws FileNotFoundException {
            super(ErrorFile.getErrorFile(folder), true);
        }

        private static String getErrorFile(String errorFolder) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd");

            if (errorFolder != null) {
                // we found an error in the first attempt to set user defined error stream now we're setting this manually.
                Config.ERROR_PATH = errorFolder;
            }

            final Timestamp now = new Timestamp(Server.getMilliTime()); // 15 mins max

            String name = (Config.ERROR_PATH + "Error-v" +
                    Config.SERVER_VERSION + "-" +
                    sdf.format(now) +
                    ".log");

            return name;
        }

        @Override
        public void write(int b) throws IOException {
            errorStream.write(b);
            super.write(b);
        }

        @Override
        public void write(byte[] b) throws IOException {
            errorStream.write(b);
            super.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            errorStream.write(b, off, len);
            super.write(b, off, len);
        }
    }


}
