package com.github.krelix.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by brizarda on 20/01/2017.
 */
public final class FileCreator {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileCreator.class);
    // Help converting from bytes to KB (and higher)
    private static final long TO_ABOVE_BYTE_RANGE = 1024;
    // Max file size in MB
    private static final long MAX_FILE_SIZE = 15 * TO_ABOVE_BYTE_RANGE * TO_ABOVE_BYTE_RANGE;
    // Minimum file size in bytes (to avoid 0 length files)
    private static final long MIN_FILE_SIZE = 500;

    private static long generateRandomFileSize() {
        final long value = Math.abs((new Random()).nextLong() % MAX_FILE_SIZE);
        return value < MIN_FILE_SIZE ? MIN_FILE_SIZE : value ;
    }


    private static byte[] generateRandomByteArray(int length) {
        byte[] bytes = new byte[length];
        new Random().nextBytes(bytes);
        return bytes;
    }

    public static File generateRandomFile(long length)
            throws IOException {
        File randomFile = File.createTempFile("tmp_data", ".jtmp");
        FileOutputStream fileOut = new FileOutputStream(randomFile);
        randomFile.setWritable(true);
        randomFile.deleteOnExit();
        while (randomFile.length() < length) {
            // To avoid some shenanigans with array sizes exceeding
            // the max authorized VM value
            int buffer = Integer.MAX_VALUE / 2;
            if (length < buffer) {
                buffer = (int) length;
            }
            fileOut.write(generateRandomByteArray(buffer));
            fileOut.flush();
        }
        return randomFile;
    }

    public static Flux<File> randomFilesCreator(int amount) {
        final List<File> files = new ArrayList<>();
        for(int i = 0; i < amount; i++) {
            try {
                files.add(generateRandomFile(generateRandomFileSize()));
            } catch (IOException e) {
                LOGGER.error("An error occured during file generation", e);
            }
        }
        return Flux.fromIterable(files);
    }
}
