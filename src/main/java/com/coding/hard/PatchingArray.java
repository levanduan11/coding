package com.coding.hard;

import com.coding.InputReader;
import com.coding.OutputWriter;

import java.util.Arrays;
import java.util.stream.Collectors;

public class PatchingArray {

    public static int minPatchesV2(int[] nums, int n) {
        long target = 1;
        int patches = 0;
        int i = 0;
        while (target <= n) {
            if (i < nums.length && nums[i] <= target) {
                target += nums[i];
                i++;
            } else {
                target *= 2;
                patches++;
            }
        }
        return patches;
    }

    public static int minPatches(int[] nums, int n) {
        int res = 0;
        long max = 0;
        int i = 0;
        while (max < n) {
            if (i < nums.length && nums[i] <= max + 1) {
                max += nums[i++];
            } else {
                max += max + 1;
                res++;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        String out = InputReader.inputAsStrings("330.txt")
                .map(line -> {
                    String[] lineArr = line.split(";");
                    int n = Integer.parseInt(lineArr[1]);
                    int[] nums = Arrays.stream(lineArr[0].split(","))
                            .mapToInt(Integer::parseInt)
                            .toArray();
                    return minPatches(nums, n);
                })
                .map(String::valueOf)
                .collect(Collectors.joining("\n"));
        OutputWriter.write(out, "330.txt");
    }
}
