package bit;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.IntStream.range;

public class App {
    static class Tuple2<T1, T2> {
        final T1 t1;
        final T2 t2;

        Tuple2(T1 t1, T2 t2) {
            this.t1 = t1;
            this.t2 = t2;
        }
    }

    record Person(int id, String name) {
    }

    static <T1, T2> Stream<Tuple2<T1, T2>> zip(
            IntStream source,
            Function<? super Integer, ? extends T1> fnT1,
            Function<? super Integer, ? extends T2> fnT2) {
        return source.boxed()
                .map(integer -> {
                    T1 t1 = fnT1.apply(integer);
                    T2 t2 = fnT2.apply(integer);
                    return new Tuple2<>(t1, t2);
                });
    }

    static Stream<Tuple2<Integer, Integer>> zip(
            IntStream source1,
            IntStream source2) {
        Objects.requireNonNull(source1);
        Objects.requireNonNull(source2);
        int[] arr1 = source1.toArray();
        int[] arr2 = source2.toArray();
        if (arr1.length != arr2.length)
            throw new IllegalStateException();
        Stream.Builder<Tuple2<Integer, Integer>> builder = Stream.builder();
        for (int i = 0; i < arr1.length; i++) {
            Tuple2<Integer, Integer> t = new Tuple2<>(arr1[i], arr2[i]);
            builder.add(t);
        }
        return builder.build();
    }

    static <T1, T2> Stream<Tuple2<T1, T2>> zip(
            IntStream source1,
            Function<? super Integer, ? extends T1> fn1,
            IntStream source2,
            Function<? super Integer, ? extends T2> fn2
    ) {
        Objects.requireNonNull(source1);
        Objects.requireNonNull(source2);
        Objects.requireNonNull(fn1);
        Objects.requireNonNull(fn2);

        int[] arr1 = source1.toArray();
        int[] arr2 = source2.toArray();
        if (arr1.length != arr2.length)
            throw new IllegalStateException();
        Stream.Builder<Tuple2<T1, T2>> builder = Stream.builder();
        for (int i = 0; i < arr1.length; i++) {
            T1 t1 = fn1.apply(arr1[i]);
            T2 t2 = fn2.apply(arr2[i]);
            Tuple2<T1, T2> t = new Tuple2<>(t1, t2);
            builder.add(t);
        }
        return builder.build();
    }

    public static void main(String[] args) throws Throwable {
        System.out.println(Integer.toBinaryString(-1>>1));
        System.out.println(-1>>1);
        System.out.println(-1>>>1);
        System.out.println(Integer.MAX_VALUE); 
        System.out.println(-1/2);
    }

    static boolean hasZeroByte(int word) {
        return ((word & 0xff) == 0) ||
                ((((word >> 8) & 0xff) == 0) ||
                        ((word >> 16) & 0xff) == 0 ||
                        ((word >> 25)) == 0);
    }

    static int interleaveBits2(int x, int y) {
        final int[] B = {0x55555555, 0x33333333, 0x0f0f0f0f, 0x00ff00ff};
        final int[] S = {1, 2, 3, 4};
        x = (x | (x << S[3])) & B[3];
        x = (x | (x << S[2])) & B[2];
        x = (x | (x << S[1])) & B[1];
        x = (x | (x << S[0])) & B[0];

        y = (y | (y << S[3])) & B[3];
        y = (y | (y << S[2])) & B[2];
        y = (y | (y << S[1])) & B[1];
        y = (y | (y << S[0])) & B[0];

        int z = x | (y << 1);
        return z;
    }

    static int interleaveBits(int x, int y) {
        int z = 0;
        for (int i = 0; i < Integer.SIZE; i++) {
            z |= ((x & (1 << i)) << i) | ((y & (1 << i)) << (i + 1));
            String v = Integer.toBinaryString(z);
            System.out.println(v);
        }
        return z;
    }

    static int countTrailingFloat(int v) {
        float f = (float) (v & -v);
        int floatBits = Float.floatToIntBits(f);
        int exponent = (floatBits >> 23) & 0xff;
        int bias = 0x7f;
        return exponent - bias;
    }

    static int countTrailingZeros3(int v) {
        int c;
        if ((v & 0x1) != 0) {
            c = 0;
        } else {
            c = 1;
            if ((v & 0xffff) == 0) {
                v >>= 16;
                c += 16;
            }
            if ((v & 0xff) == 0) {
                v >>= 8;
                c += 8;
            }
            if ((v & 0xf) == 0) {
                v >>= 4;
                c += 4;
            }
            if ((v & 0x3) == 0) {
                v >>= 2;
                c += 2;
            }
            c -= v & 0x1;
        }
        return c;
    }

    static int countTrailingZeros2(int v) {
        int c = 32;
        v &= -v;
        String s = Integer.toBinaryString(-v);
        String a = Integer.toBinaryString(v);
        String dd = Integer.toBinaryString(0x33333333);
        String aaa = Integer.toBinaryString(0x55555555);
        if (v != 0) c--;
        if ((v & 0x0000ffff) != 0) c -= 16;
        if ((v & 0x00ff00ff) != 0) c -= 8;
        if ((v & 0x0f0f0f0f) != 0) c -= 4;
        if ((v & 0x33333333) != 0) c -= 2;
        if ((v & 0x55555555) != 0) c -= 1;
        return c;
    }

    static int countTrailingZeros(int v) {
        int c;
        if (v != 0) {
            String a = Integer.toBinaryString(v);
            String e = Integer.toBinaryString(v - 1);

            v = (v ^ (v - 1)) >> 1;
            String s = Integer.toBinaryString(v);
            for (c = 0; v != 0; c++) {
                v >>= 1;
                String d = Integer.toBinaryString(v);
                System.out.println();
            }
            return c;
        } else {
            return Integer.SIZE;
        }
    }

    static int findLogBase24(int v) {
        if (v < 0)
            throw new IllegalStateException();
        int[] b = {0xAAAAAAAA, 0xCCCCCCCC, 0xF0F0F0F0,
                0xFF00FF00, 0xFFFF0000};
        int r = (v & b[0]) != 0 ? 1 : 0;
        return r;
    }

    static int findLogBase23(int v) {
        if (v < 0)
            throw new IllegalStateException();
        int r = 0;
        int shift;

        shift = (v > 0xffff) ? 16 : 0;
        v >>= shift;
        r |= shift;
        shift = (v > 0xff) ? 8 : 0;
        v >>= shift;
        r |= shift;
        shift = (v > 0xf) ? 4 : 0;
        v >>= shift;
        r |= shift;
        shift = (v > 0x3) ? 2 : 0;
        v >>= shift;
        r |= shift;
        r |= (v >> 1);
        return r;
    }

    static int findLogBase22(int v) {
        if (v < 0)
            throw new IllegalStateException();
        int[] b = {0x2, 0xc, 0xf0, 0xff00, 0xffff0000};
        int[] s = {1, 2, 4, 8, 16};
        int r = 0;
        for (int i = 4; i >= 0; i--) {
            String a = Integer.toBinaryString(v);
            String c = Integer.toBinaryString(b[i]);
            if ((v & b[i]) != 0) {
                v >>= s[i];
                String d = Integer.toBinaryString(v);
                r |= s[i];
                String e = Integer.toBinaryString(r);
                System.out.println();
            }
        }
        return r;
    }

    static int findLogBase21(double floatValue) {
        long bits = Double.doubleToLongBits(floatValue);
        System.out.println(Long.toBinaryString(bits));
        int exponent = (int) ((bits >> 52) & 0x7ff) - 1023;
        System.out.println(Integer.toBinaryString(exponent));
        return exponent;
    }

    static int finLogBase2(int num) {
        int log = -1;
        while (num > 0) {
            num >>= 1;
            log++;
        }
        return log;
    }

    static int modulus3(int n, int s) {
        int d = (1 << s) - 1;
        int m;
        for (m = n; n > d; n = m) {
            for (m = 0; n != 0; n >>= s) {
                m += n & d;
            }
        }
        m = m == d ? 0 : m;
        return m;
    }

    static int modulus2(int dividend, int s) {
        int divisor = (1 << s) - 1;
        int quotient = (dividend + (dividend >> s)) & divisor;
        return quotient;
    }

    static int modulus(int n, int d) {
        return n & (d - 1);
    }

    static int reversed5(int v) {
        int s = Integer.SIZE;
        int mask = ~0;
        String a = Integer.toBinaryString(s);
        String b = Integer.toBinaryString(v);
        while ((s >>= 1) > 0) {
            String c = Integer.toBinaryString(s);
            String f = Integer.toBinaryString(mask);
            mask ^= (mask << s);
            String d = Integer.toBinaryString(mask);
            String e2 = Integer.toBinaryString(v);
            String e = Integer.toBinaryString(((v >> s) & mask));
            String e4 = Integer.toBinaryString(((v >> s)));
            String e1 = Integer.toBinaryString(((v << s) & ~mask));
            v = ((v >> s) & mask) | ((v << s) & ~mask);
            String e3 = Integer.toBinaryString(v);
            System.out.println();
        }
        return v;
    }

    static int reversed4(int value, int numBits) {
        int reversed = value, shift = numBits, mask = (1 << numBits) - 1;
        for (int layer = numBits; layer > 1; layer >>= 1) {
            shift >>= 1;
            mask ^= (mask << shift) & mask;
            reversed = ((reversed & mask) << shift) | ((reversed >> shift) & mask);
        }
        return reversed;
    }

    static byte reversed3(byte b) {
        b = (byte) (((b & 0xaa) >> 1) | ((b & 0x55)) << 1);
        b = (byte) (((b & 0xcc) >> 2) | ((b & 0x55)) << 1);
        b = (byte) ((b >> 4) | (b << 4));
        return b;
    }

    static void reversed2(int v) {
        int r = v;
        String a = Integer.toBinaryString(r);
        int s = Integer.SIZE - 1;
        v >>= 1;
        while (v != 0) {
            r <<= 1;
            String b = Integer.toBinaryString(r);
            r |= v & 1;
            String c = Integer.toBinaryString(r);
            v >>= 1;
            String d = Integer.toBinaryString(v);
            s--;
        }
        r <<= s;
        String b = Integer.toBinaryString(r);
        System.out.println();
    }

    static void reverse(int num) {
        String s = Integer.toBinaryString(num);
        int res = 0;
        int n = Integer.SIZE - 1;
        for (int i = 0; i < 32 & num != 0; i++) {
            res |= (num & 1);
            num >>= 1;
            res <<= 1;
            String d = Integer.toBinaryString(res);
            System.out.println();
            n--;
        }
        res <<= n;
        String v = Integer.toBinaryString(res);
        System.out.println(res);
    }

    static void swap(int a, int b) {
        String i = Integer.toBinaryString(a);
        String j = Integer.toBinaryString(b);
        a = a ^ b;
        String k = Integer.toBinaryString(a);
        b = a ^ b;
        String l = Integer.toBinaryString(b);
        a = a ^ b;
        String m = Integer.toBinaryString(a);
        System.out.println(a);
        System.out.println(b);
    }

    static boolean computeParityWithMultiply(int v) {
        v ^= v >>> 16;
        String a = Integer.toBinaryString(v);
        v ^= v >>> 8;
        String b = Integer.toBinaryString(v);
        v ^= v >>> 4;
        String c = Integer.toBinaryString(v);
        v &= 0xf;
        String d = Integer.toBinaryString(v);
        String e = Integer.toBinaryString(0x6996);
        return ((0x6996 >> v) & 1) == 1;
    }

    static boolean parity(int v) {
        boolean parity = false;
        while (v != 0) {
            parity = !parity;
            v = v & (v - 1);
        }
        return parity;
    }

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

    static int countBitV2(int v) {
        int count = 0;
        while (v != 0) {
            count++;
            v &= (v - 1);
        }
        return count;
    }
}

class ReverseBit {
    static byte[] table = new byte[256];

    static {
        for (int i = 0; i < 256; i++) {
            table[i] = reverseByte((byte) i);
        }
    }

    private static byte reverseByte(byte b) {
        byte result = 0;
        for (int i = 0; i < 8; i++) {
            result = (byte) ((result << 1) | (b >> i) & 1);
        }
        return result;
    }

    static int reverseBitTable(int num) {
        int res = 0;
        String k = Integer.toBinaryString(num);
        for (int i = 0; i < 4; i++) {
            int currentByte = ((num >>> (i * 8)) & 0xff);
            String s = Integer.toBinaryString(currentByte);
            String s1 = Integer.toBinaryString(table[currentByte]);
            String s2 = Integer.toBinaryString(table[currentByte] & 0xff);
            res |= (table[currentByte] & 0xff) << ((3 - i) * 8);
            String a = Integer.toBinaryString(res);
            System.out.println();
        }
        return res;
    }
}

class ParityLookupTable {
    static final Boolean[] parityTable256;

    static {
        parityTable256 = range(0, 256).boxed().map(integer -> integer % 2 != 0).toArray(Boolean[]::new);
    }

    static boolean computeParityUsingTable(byte b) {
        return parityTable256[b & 0xff];
    }

    static boolean computeParity32UsingTable(int v) {
        String s = Integer.toBinaryString(v);
        v ^= v >> 16;
        String a = Integer.toBinaryString(v);
        v ^= v >> 8;
        String b = Integer.toBinaryString(v);
        String c = Integer.toBinaryString(0xff);
        int index = v & 0xff;
        String d = Integer.toBinaryString(index);
        return parityTable256[index];
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
