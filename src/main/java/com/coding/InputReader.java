package com.coding;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public final class InputReader {
    public static final String BASE_PATH = "/input";

    private InputReader() {
    }

    public static Stream<String> inputAsStrings(String path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException();
        }
        String p = BASE_PATH + "/" + path;
        try (var is = Objects.requireNonNull(InputReader.class.getResourceAsStream(p))) {
            return new String(is.readAllBytes())
                    .lines();
        } catch (IOException e) {
            System.err.println("Could not read input!!!");
            return Stream.<String>builder().build();
        }
    }

    public static void main(String[] args) throws IOException {
        byte[] bytes = Objects.requireNonNull(InputReader.class.getResourceAsStream("/input/basic-calculator.txt"))
                .readAllBytes();
        new String(bytes)
                .lines()
                .forEach(System.out::println);
    }
}
