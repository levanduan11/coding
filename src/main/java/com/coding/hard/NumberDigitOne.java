package com.coding.hard;

public class NumberDigitOne {
    public static int countDigitOne(int n) {
        if (n < 1)
            return 0;
        if (n < 10)
            return 1;
        int count = 1;
        for (int i = 10; i <= n; i++) {
            int j = i;
            while (j > 0) {
                int m = j % 10;
                if (m == 1)
                    count++;
                j = j / 10;
            }
        }
        return count;
    }

    public static int countDigitOne02(int n) {
        int res = 0;
        for (long k = 1; k <= n; k *= 10) {
            long r = n / k, m = n % k;
            res += (int) ((r + 8) / 10 * k + (r % 10 == 1 ? m + 1 : 0));
        }
        return res;
    }

    public static int countDigitOne03(int n) {
        if (n <= 0)
            return 0;
        int count = 0;
        long base = 1;
        long prevCount = 0;
        while (n > 0) {
            int digit = n % 10;
            n /= 10;
            count += (int) (n * base);
            if (digit == 1)
                count += (int) (prevCount + 1);
            else if (digit > 1)
                count += (int) base;
            prevCount += digit * base;
            base *= 10;
        }
        return count;
    }

    public static void main(String[] args) {
        int n = 12345;
        int res = countDigitOne03(13);
        System.out.println(res);
    }
}
