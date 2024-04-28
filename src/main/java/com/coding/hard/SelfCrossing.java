package com.coding.hard;

import com.coding.InputReader;
import com.coding.OutputWriter;

import java.util.stream.Collectors;

public class SelfCrossing {
    public static boolean isSelfCrossing(int[] distance) {
        if (distance.length < 4)
            return false;
        for (int i = 3; i < distance.length; i++) {
            if (distance[i] >= distance[i - 2] && distance[i - 1] <= distance[i - 3])
                return true;
            if (i >= 4 && distance[i - 1] == distance[i - 3] && distance[i] + distance[i - 4] >= distance[i - 2])
                return true;
            if (i >= 5 && distance[i - 2] >= distance[i - 4]
                    && distance[i] + distance[i - 4] >= distance[i - 2]
                    && distance[i - 1] <= distance[i - 3]
                    && distance[i - 1] + distance[i - 5] >= distance[i - 3])
                return true;
        }
        return false;
    }

    public static void main(String[] args) {
        String out = InputReader.inputAsStrings("335.txt")
                .map(line -> {
                    String[] strings = line.split(",");
                    int[] distance = new int[strings.length];
                    for (int i = 0; i < strings.length; i++) {
                        distance[i] = Integer.parseInt(strings[i]);
                    }
                    boolean ans = isSelfCrossing(distance);
                    return String.valueOf(ans);
                })
                .collect(Collectors.joining(System.lineSeparator()));
        OutputWriter.write(out, "335.txt");
    }
}
