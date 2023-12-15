package com.dke.data.agrirouter.test.helper;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Generic content reader for the testcases.
 */
public class ContentReader {

    private static final String FOLDER = "./message-content/";

    public static byte[] readRawData(Identifier identifier) throws Throwable {
        var path = Paths.get(FOLDER.concat(identifier.getFileName()));
        return Files.readAllBytes(path);
    }

    public enum Identifier {
        BIG_TASK_DATA("big_taskdata.zip"),
        SMALL_TASK_DATA("small_taskdata.zip");

        private final String fileName;

        Identifier(String fileName) {
            this.fileName = fileName;
        }

        public String getFileName() {
            return fileName;
        }
    }
}
