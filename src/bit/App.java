package bit;

import java.util.Arrays;
import java.util.BitSet;

public class App {
    static int checkSign(int n) {
        return -(int) ((n & 0x80000000) >>> 31);
    }

    static boolean detectOpposite(int a, int b) {
        return (a ^ b) < 0;
    }

    static int absolute(int a) {
        int mask = a >> 31;
        return (a ^ mask) - mask;
    }

    static int min(int x, int y) {
        return y ^ ((x ^ y) & -((x < y) ? 1 : 0));
    }

    static int max(int x, int y) {
        return x ^ ((x ^ y) & -(x < y ? 1 : 0));
    }

    static int min1(int x, int y) {
        return y + ((x - y) & ((x - y) >> 31));
    }

    static int max1(int x, int y) {
        return x - ((x - y) & ((x - y) >> 31));
    }

    static boolean isPowerTwo(int x) {
        return (x & (x - 1)) == 0;
    }

    static int signExtend(int value, int original, int target) {
        int shift = target - original;
        int sign = value & (1 << (original - 1));
        return (value << shift) | (sign << shift) >> shift;
    }

    static int setBit(int value, int mask, boolean condition) {
        int setBits = mask & (condition ? 1 : 0);
        return value | setBits;
    }

    static int clearBit(int value, int mask, boolean condition) {
        int clearBit = mask & (condition ? 0 : 1);
        return value & ~clearBit;
    }

    static int sBit(int m, int w, boolean f) {
        return (w & ~m) | (-(f ? 1 : 0) & m);
    }

    static int negate(boolean f, int v) {
        int a = f ? 1 : 0;
        return (a ^ (a - 1)) * v;
    }

    static int mergeBit(int a, int b, int mask) {
        return (a & mask) | (b & ~mask);
    }

    static int countBit(int v) {
        int count = 0;
        while (v != 0) {
            count += 1 & v;
            v >>= 1;
        }
        return count;
    }
    static int countBitV2(int v){
        int count = 0;
        while (v!=0){
            count++;
            v &=(v-1);
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println(Integer.toBinaryString(10));
        System.out.println(countBitV2(10));
    }
}

class CountBitsLookupTable {
    static final int[] bitCountLookup = new int[256];

    static {
        for (int i = 0; i < 256; i++) {
            bitCountLookup[i] = countBitOne(i);
        }
    }

    static int countBitOne(int n) {
        int c = 0;
        while (n != 0) {
            c += n & 1;
            n >>= 1;
        }
        return c;
    }

    static int countSetBit(int n) {
        int count = 0;
        for (int i = 0; i < 4; i++) {
            int index = n & 0xff;
            count += bitCountLookup[index];
            n >>= 8;
        }
        return count;
    }
}
