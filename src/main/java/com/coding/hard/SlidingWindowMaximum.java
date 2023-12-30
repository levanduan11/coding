package com.coding.hard;

import com.coding.InputReader;
import com.coding.OutputWriter;

import java.util.*;
import java.util.stream.Collectors;

public class SlidingWindowMaximum {
    public static int[] maxSlidingWindow(int[] nums, int k) {
        int n = nums.length;
        if (n == 1 || k == 1)
            return nums;
        int[] res = new int[n - k + 1];
        int idx = 0;
        Deque<Integer> deque = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            while (!deque.isEmpty() && deque.peek() < i - k + 1)
                deque.poll();
            while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i])
                deque.pollLast();
            deque.offer(i);
            if (i >= k - 1) {
                res[idx] = nums[deque.peek()];
                idx++;
            }
        }
        return res;
    }

    public static int[] maxSlidingWindowV2(int[] nums, int k) {
        int[] re = new int[nums.length - k + 1];
        int maxId = 0;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < k; i++) {
            if (nums[i] >= max) {
                max = nums[i];
                maxId = i;
            }
        }
        re[0] = max;
        for (int i = 1, end; i < re.length; i++) {
            end = i + k - 1;
            if (i <= maxId) {
                if (max <= nums[end]) {
                    max = nums[end];
                    maxId = end;
                }
            } else {
                if (nums[end] >= max - 1) {
                    max = nums[end];
                    maxId = end;
                } else if (nums[i] >= max - 1) {
                    max = nums[i];
                    maxId = i;
                } else {
                    max = Integer.MIN_VALUE;
                    for (int x = i; x < end + 1; x++) {
                        if (nums[x] >= max) {
                            max = nums[x];
                            maxId = x;
                        }
                    }
                }
            }
            re[i] = max;
        }
        return re;
    }

    public static int[] maxSlidingWindowV3(int[] nums, int k) {
        int[] res = new int[nums.length - k + 1];
        TreeMap<Integer, Integer> values = new TreeMap<>(Collections.reverseOrder());
        for (int i = 0; i < k; i++) {
            values.put(nums[i], values.getOrDefault(nums[i], 0) + 1);
        }
        for (int i = k; i < nums.length; i++) {
            res[i - k] = values.firstKey();
            values.put(nums[i], values.getOrDefault(nums[i], 0) + 1);
            int remove = nums[i - k];
            if (values.get(remove) == 1)
                values.remove(remove);
            else
                values.put(remove, values.get(remove) - 1);
        }
        res[nums.length - k] = values.firstKey();
        return res;
    }

    public static void main(String[] args) {
        String collect = getString();
        OutputWriter.write(collect, "239_out.txt");
    }

    private static String getString() {
        return InputReader.inputAsStrings("239_sliding-window.txt")
                .map(s -> s.split(":"))
                .map(strings -> {
                    int[] arr = new int[strings[0].length()];
                    int index = 0;
                    char[] chars = strings[0].toCharArray();
                    int sign = 1;
                    for (char aChar : chars) {
                        if (aChar == '[' || aChar == ']' || aChar == ',')
                            continue;
                        if (aChar == '-') {
                            sign = -1;
                        } else {
                            arr[index++] = sign * (aChar - '0');
                            sign = 1;
                        }
                    }
                    int[] newArr = Arrays.copyOf(arr, index);
                    int k = Integer.parseInt(strings[1]);
                    return Map.of(k, newArr);
                })
                .map(m -> {
                    Map.Entry<Integer, int[]> next = m.entrySet().iterator().next();
                    int k = next.getKey();
                    int[] value = next.getValue();
                    int[] ints = maxSlidingWindowV3(value, k);
                    return Arrays.toString(ints);
                }).collect(Collectors.joining(System.lineSeparator()));
    }
}
