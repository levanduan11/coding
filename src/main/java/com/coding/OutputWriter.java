package com.coding;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public final class OutputWriter {
    public static final String BASE_PATH = "src/main/resources/output";

    private OutputWriter() {
    }

    public static void write(Object obj, String... paths) {
        if (!(obj instanceof Serializable))
            throw new IllegalArgumentException();
        Path path = Paths.get(BASE_PATH);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try (ByteArrayOutputStream op = new ByteArrayOutputStream();
             ObjectOutputStream os = new ObjectOutputStream(op)) {
            Path writePath = path.resolve(String.join("/", paths));
            String s = op.toString();
            if (obj instanceof String string) {
                s = string;
            } else {
                os.writeObject(obj);
                os.flush();
            }
            Files.writeString(writePath, s, StandardOpenOption.CREATE, StandardOpenOption.WRITE,StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
