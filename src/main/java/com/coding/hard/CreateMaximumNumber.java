package com.coding.hard;

import com.coding.InputReader;
import com.coding.OutputWriter;

import java.util.Arrays;
import java.util.stream.Collectors;


public class CreateMaximumNumber {
    public static int[] maxNumberV2(int[] nums1, int[] nums2, int k) {
        int[] res = null;
        for (int i = 0; i <= k; i++) {
            if (i <= nums1.length && k - i <= nums2.length) {
                int[] max1 = getMaxSub(nums1, i);
                int[] max2 = getMaxSub(nums2, k - i);
                if (max1 == null || max2 == null)
                    continue;
                int[] tmp = mergeMax(max1, max2);
                if (compareArr(tmp, res)) {
                    res = tmp;
                }
            }
        }
        return res;
    }

    public static int[] mergeMax(int[] max1, int[] max2) {
        int i = 0, j = 0, id = 0;
        int size1 = max1.length;
        int size2 = max2.length;
        int[] res = new int[size1 + size2];
        while (size1 > 0 && size2 > 0 && i < size1 && j < size2) {
            if (max1[i] > max2[j]) {
                res[id++] = max1[i++];
            } else if (max1[i] == max2[j]) {
                if (i + 1 < size1 && j + 1 < size2) {
                    if (max1[i + 1] == max2[j + 1]) {
                        if (max1[i + 1] >= max1[i]) {
                            int m = i + 2, n = j + 2;
                            boolean goToMax1 = true;
                            boolean isTracking = false;
                            while (m < size1 && n < size2) {
                                if (max1[m] != max2[n]) {
                                    if (max1[m] > max2[n]) {
                                        isTracking = true;
                                    } else {
                                        isTracking = true;
                                        goToMax1 = false;
                                    }
                                    break;
                                }
                                m++;
                                n++;
                            }
                            if (isTracking) {
                                if (goToMax1) {
                                    res[id++] = max1[i++];
                                } else {
                                    res[id++] = max2[j++];
                                }
                            } else {
                                if (m >= size1 && n < size2) {
                                    if (max2[--n] > max1[i]) {
                                        res[id++] = max2[j++];
                                    } else if (max2[--n] == max1[i]) {
                                        res[id++] = max2[j++];
                                    } else {
                                        res[id++] = max1[i++];
                                    }
                                } else if (m < size1) {
                                    if (max1[--m] > max2[j]) {
                                        res[id++] = max1[i++];
                                    } else if (max2[--n] == max1[i]) {
                                        res[id++] = max1[i++];
                                    } else {
                                        res[id++] = max2[j++];
                                    }
                                } else {
                                    res[id++] = max1[i++];
                                    res[id++] = max2[j++];
                                }
                            }
                        } else {
                            res[id++] = max1[i++];
                            res[id++] = max2[j++];
                        }
                    } else if (max1[i + 1] > max2[j] && max1[i + 1] > max2[j + 1]) {
                        res[id++] = max1[i++];
                    } else if (max2[j + 1] > max1[i] && max2[j + 1] > max1[i + 1]) {
                        res[id++] = max2[j++];
                    } else {
                        res[id++] = max1[i++];
                        res[id++] = max2[j++];
                    }
                } else if (i + 1 >= size1 && j + 1 >= size2) {
                    if (max1[i] > max2[j]) {
                        res[id++] = max1[i++];
                        res[id++] = max2[j++];
                    } else {
                        res[id++] = max2[j++];
                        res[id++] = max1[i++];
                    }
                } else if (i + 1 < size1) {
                    if (max1[i + 1] >= max2[j])
                        res[id++] = max1[i++];
                    else
                        res[id++] = max2[j++];
                } else {
                    if (max2[j + 1] >= max1[i])
                        res[id++] = max2[j++];
                    else
                        res[id++] = max1[i++];
                }
            } else {
                res[id++] = max2[j++];
            }
        }
        if (size1 == 0 || i >= size1) {
            for (int value : max2) {
                res[id++] = value;
            }
        } else {
            for (int value : max1) {
                res[id++] = value;
            }
        }
        return res;
    }

    private static boolean compareArr(int[] tmp, int[] res) {
        if (res == null)
            return true;
        for (int i = 0; i < tmp.length; i++) {
            if (tmp[i] > res[i])
                return true;
            if (tmp[i] < res[i])
                return false;
        }
        return true;
    }

    private static int[] getMaxSub(int[] nums, int k) {
        if (k == 0)
            return new int[]{};
        if (k > nums.length)
            return null;
        int[] res = new int[k];
        res[0] = nums[0];
        int last = nums.length - 1;
        int cnt = 1;
        for (int i = 1; i <= last; i++) {
            while (cnt > 0 && res[cnt - 1] < nums[i] && last - i >= k - cnt) {
                cnt--;
            }
            if (cnt < k) {
                res[cnt++] = nums[i];
            }
        }
        return res;
    }

    public static int[] maxNumber(int[] nums1, int[] nums2, int k) {
        if (nums1.length == 0 || nums2.length == 0) {
            return new int[0];
        }
        int m = nums1.length, n = nums2.length;
        int l = Math.max(0, k - n), r = Math.min(k, m);
        int[] ans = new int[k];
        for (int x = l; x <= r; x++) {
            int[] arr1 = f(nums1, x);
            int[] arr2 = f(nums2, k - x);
            int[] arr = merge(arr1, arr2);
            if (compare(arr, ans, 0, 0)) {
                ans = arr;
            }
        }
        return ans;
    }

    private static int[] f(int[] nums, int k) {
        int n = nums.length;
        int[] stk = new int[k];
        int top = -1;
        int remain = n - k;
        for (int x : nums) {
            while (top >= 0 && stk[top] < x && remain > 0) {
                --top;
                --remain;
            }
            if (top + 1 < k) {
                stk[++top] = x;
            } else {
                --remain;
            }
        }
        return stk;
    }

    private static int[] merge(int[] nums1, int[] nums2) {
        int m = nums1.length, n = nums2.length;
        int i = 0, j = 0;
        int[] ans = new int[m + n];
        for (int k = 0; k < m + n; k++) {
            if (compare(nums1, nums2, i, j)) {
                ans[k] = nums1[i];
                i++;
            } else {
                ans[k] = nums2[j];
                j++;
            }
        }
        return ans;
    }

    private static boolean compare(int[] nums1, int[] nums2, int i, int j) {
        if (i >= nums1.length)
            return false;
        if (j >= nums2.length)
            return true;
        if (nums1[i] > nums2[j])
            return true;
        if (nums1[i] < nums2[j])
            return false;
        return compare(nums1, nums2, i + 1, j + 1);
    }

    public static void main(String[] args) {
        String out = InputReader.inputAsStrings("321_Create_Maximum_Number.txt")
                .map(s -> s.split(";"))
                .map(strings -> {
                    int[] nums1 = Arrays.stream(strings[0].split(",")).mapToInt(Integer::parseInt).toArray();
                    int[] nums2 = Arrays.stream(strings[1].split(",")).mapToInt(Integer::parseInt).toArray();
                    int k = Integer.parseInt(strings[2]);
                    return Arrays.toString(maxNumber(nums1, nums2, k));
                })
                .collect(Collectors.joining(System.lineSeparator()));
        OutputWriter.write(out, "321_Create_Maximum_Number.txt");

    }
}
