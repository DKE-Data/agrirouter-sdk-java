package com.dke.data.agrirouter.test.helper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * Generic content reader for the testcases.
 */
public class ContentReader {

    private static final String FOLDER = "./message-content/";

    public static String readBase64EncodedMessageContent(Identifier identifier) throws Throwable {
        Path path = Paths.get(FOLDER.concat(identifier.getFileName()));
        final byte[] rawData = Files.readAllBytes(path);
        return new String(Base64.getEncoder().encode(rawData));
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
