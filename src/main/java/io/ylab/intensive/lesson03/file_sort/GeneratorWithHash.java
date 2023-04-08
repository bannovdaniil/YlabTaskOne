package io.ylab.intensive.lesson03.file_sort;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * Дополнительно, считает хешкод, для качественно проверки.
 */
public class GeneratorWithHash {
    private long hashCode = 0L;

    public File generate(String name, int count) throws IOException {
        Random random = new Random();
        File file = new File(name);
        try (PrintWriter pw = new PrintWriter(file)) {
            for (int i = 0; i < count; i++) {
                long number = random.nextLong();
                pw.println(number);
                hashCode ^= number;
            }
            pw.flush();
        }
        return file;
    }

    public long getHashCode() {
        return hashCode;
    }
}