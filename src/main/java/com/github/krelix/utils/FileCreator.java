package com.github.krelix.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Created by brizarda on 20/01/2017.
 */
public final class FileCreator {
    public static byte[] generateRandomByteArray(int length) {
        byte[] bytes = new byte[length];
        new Random().nextBytes(bytes);
        return bytes;
    }

    public static File generateRandomFile(long length)
            throws IOException {
        File randomFile = File.createTempFile("tmp_data", ".jtmp");
        FileOutputStream fileOut = new FileOutputStream(randomFile);
        randomFile.setWritable(true);
        while (randomFile.length() < length) {
            int buffer = Integer.MAX_VALUE;
            if (length < buffer) {
                buffer = (int) length;
            }
            fileOut.write(generateRandomByteArray(buffer));
            fileOut.flush();
        }
        return randomFile;
    }
}
