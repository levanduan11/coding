package com.coding;

import java.io.*;
import java.util.*;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;

public class App {
    static final String s = "1111_1111_1111_1111_1111_1111_1111_1111";

    public static void main(String[] args) throws IOException {
        Person[] a = {new Person(9), new Person(8), new Person(7),
                new Person(6), new Person(5), new Person(0),
                new Person(1), new Person(2), new Person(3),
                new Person(-4), new Person(-7),
                new Person(-9), new Person(10), new Person(-10),
        };
        Random r = new Random();
        Person[] people = IntStream.range(1, 1000).boxed()
                .map(integer -> {
                    int id = r.nextInt(10000);
                    return new Person(id);
                }).toArray(Person[]::new);
        InputStream is = new BufferedInputStream(new FileInputStream(""));

    }
}

record Person(int id) {
}