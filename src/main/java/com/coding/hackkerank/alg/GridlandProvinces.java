package com.coding.hackkerank.alg;

import com.coding.InputReader;
import com.coding.OutputWriter;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class GridlandProvinces {

    public void solve(int testNumber, InputReader2 in, OutputWriter2 out) {
        int n = in.readInt();
        String[] s = IOUtils.readStringArray(in, 2);
        LongSet set = new LongHashSet();
        addAll(n, s, set);
        for (int i = 0; i < 2; i++) {
            s[i] = StringUtils.reverse(s[i]);
        }
        addAll(n, s, set);
        out.printLine(set.size());
    }

    protected void addAll(int n, String[] s, LongSet set) {
        StringHash[] simple = new StringHash[2];
        for (int i = 0; i < 2; i++) {
            simple[i] = new SimpleStringHash(s[i]);
        }
        StringHash[] reverse = new StringHash[2];
        for (int i = 0; i < 2; i++) {
            reverse[i] = new SimpleStringHash(StringUtils.reverse(s[i]));
        }
        StringHash[] snail = new StringHash[2];
        for (int i = 0; i < 2; i++) {
            StringBuilder current = new StringBuilder(2 * n);
            for (int j = 0; j < n; j++) {
                current.append(s[i ^ (j & 1)].charAt(j));
                current.append(s[1 - (i ^ (j & 1))].charAt(j));
            }
            snail[i] = new SimpleStringHash(current.toString());
        }
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                for (int k = 0; k < 2; k++) {
                    StringHash hash = new CompositeStringHash(
                            new SubstringStringHash(reverse[k], n - i, n),
                            new CompositeStringHash(new SubstringStringHash(simple[1 - k], 0, i),
                                    new CompositeStringHash(new SubstringStringHash(snail[1 - (k ^ (i & 1))], 2 * i, 2 * j),
                                            new CompositeStringHash(new SubstringStringHash(simple[k], j, n),
                                                    new SubstringStringHash(reverse[1 - k], 0, n - j)))));
                    set.add(hash.hash(0));
                }
            }
        }
    }

    interface LongReversableCollection extends LongCollection {

    }

    interface StringHash {
        long hash(int from, int to);

        long hash(int from);

        int length();
    }

    static class LongHashSet extends LongAbstractStream implements LongSet {
        private static final Random RND = new Random();
        private static final int[] SHIFTS = new int[4];
        private static final byte PRESENT_MASK = 1;
        private static final byte REMOVED_MASK = 2;
        private int size;
        private int realSize;
        private long[] values;
        private byte[] present;
        private int step;
        private int ratio;

        static {
            for (int i = 0; i < 4; i++) {
                SHIFTS[i] = RND.nextInt(31) + 1;
            }
        }

        public LongHashSet() {
            this(3);
        }

        public LongHashSet(int capacity) {
            capacity = Math.max(capacity, 3);
            values = new long[capacity];
            present = new byte[capacity];
            ratio = 2;
            initStep(capacity);
        }

        public LongHashSet(LongCollection c) {
            this(c.size());
            addAll(c);
        }

        public LongHashSet(long[] arr) {
            this(new LongArray(arr));
        }

        private void initStep(int capacity) {
            step = RND.nextInt(capacity - 2) + 1;
            while (IntegerUtils.gcd(step, capacity) != 1) {
                step++;
            }
        }

        public LongIterator longIterator() {
            return new LongIterator() {
                private int position = size == 0 ? values.length : -1;

                @Override
                public long value() throws NoSuchElementException {
                    if (position == -1) {
                        advance();
                    }
                    if (position >= values.length) {
                        throw new NoSuchElementException();
                    }
                    if ((present[position] & PRESENT_MASK) == 0) {
                        throw new IllegalStateException();
                    }
                    return values[position];
                }

                @Override
                public boolean advance() {
                    if (position >= values.length) {
                        throw new NoSuchElementException();
                    }
                    do {
                        position++;
                    } while (position < values.length && (present[position] & PRESENT_MASK) == 0);
                    return isValid();
                }

                @Override
                public boolean isValid() {
                    return position < values.length;
                }

                public void remove() {
                    if ((present[position] & PRESENT_MASK) == 0) {
                        throw new IllegalStateException();
                    }
                    present[position] = REMOVED_MASK;
                }
            };
        }

        public int size() {
            return size;
        }

        public void add(long value) {
            ensureCapacity((realSize + 1) * ratio + 2);
            int current = getHash(value);
            while (present[current] != 0) {
                if ((present[current] & PRESENT_MASK) != 0 && values[current] == value) {
                    return;
                }
                current += step;
                if (current >= values.length) {
                    current -= values.length;
                }
            }
            while ((present[current] & PRESENT_MASK) != 0) {
                current += step;
                if (current >= values.length) {
                    current -= values.length;
                }
            }
            if (present[current] == 0) {
                realSize++;
            }
            present[current] = PRESENT_MASK;
            values[current] = value;
            size++;
        }

        private int getHash(long value) {
            int hash = LongHash.hash(value);
            int result = hash;
            for (int i : SHIFTS) {
                result ^= hash >> i;
            }
            result %= values.length;
            if (result < 0) {
                result += values.length;
            }
            return result;
        }

        public void ensureCapacity(int capacity) {
            if (values.length < capacity) {
                capacity = Math.max(capacity * 2, values.length);
                rebuild(capacity);
            }
        }

        private void rebuild(int capacity) {
            initStep(capacity);
            long[] oldValues = values;
            byte[] oldPresent = present;
            values = new long[capacity];
            present = new byte[capacity];
            size = 0;
            realSize = 0;
            for (int i = 0; i < oldValues.length; i++) {
                if ((oldPresent[i] & PRESENT_MASK) == PRESENT_MASK) {
                    add(oldValues[i]);
                }
            }
        }
    }

    static class InputReader2 {
        private InputStream stream;
        private byte[] buf = new byte[1024];
        private int curChar;
        private int numChars;
        private InputReader2.SpaceCharFilter filter;

        public InputReader2(InputStream stream) {
            this.stream = stream;
        }

        public int read() {
            if (numChars == -1) {
                throw new InputMismatchException();
            }
            if (curChar >= numChars) {
                curChar = 0;
                try {
                    numChars = stream.read(buf);
                } catch (IOException e) {
                    throw new InputMismatchException();
                }
                if (numChars <= 0) {
                    return -1;
                }
            }
            return buf[curChar++];
        }

        public int readInt() {
            int c = read();
            while (isSpaceChar(c)) {
                c = read();
            }
            int sgn = 1;
            if (c == '-') {
                sgn = -1;
                c = read();
            }
            int res = 0;
            do {
                if (c < '0' || c > '9') {
                    throw new InputMismatchException();
                }
                res *= 10;
                res += c - '0';
                c = read();
            } while (!isSpaceChar(c));
            return res * sgn;
        }

        public String readString() {
            int c = read();
            while (isSpaceChar(c)) {
                c = read();
            }
            StringBuilder res = new StringBuilder();
            do {
                if (Character.isValidCodePoint(c)) {
                    res.appendCodePoint(c);
                }
                c = read();
            } while (!isSpaceChar(c));
            return res.toString();
        }

        public boolean isSpaceChar(int c) {
            if (filter != null) {
                return filter.isSpaceChar(c);
            }
            return isWhitespace(c);
        }

        public static boolean isWhitespace(int c) {
            return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
        }

        public String next() {
            return readString();
        }

        public interface SpaceCharFilter {
            boolean isSpaceChar(int ch);
        }
    }

    static class OutputWriter2 {
        private final PrintWriter writer;

        public OutputWriter2(OutputStream outputStream) {
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream)));
        }

        public OutputWriter2(Writer writer) {
            this.writer = new PrintWriter(writer);
        }

        public void close() {
            writer.close();
        }

        public void printLine(int i) {
            writer.println(i);
        }
    }

    interface LongIterator {
        long value() throws NoSuchElementException;

        boolean advance();

        boolean isValid();
    }

    interface LongSet extends LongCollection {
    }

    interface LongStream extends Iterable<Long>, Comparable<LongStream> {
        LongIterator longIterator();

        default Iterator<Long> iterator() {
            return new Iterator<>() {
                private final LongIterator it = longIterator();

                @Override
                public boolean hasNext() {
                    return it.isValid();
                }

                @Override
                public Long next() {
                    long result = it.value();
                    it.advance();
                    return result;
                }
            };
        }

        default int compareTo(LongStream c) {
            LongIterator it = longIterator();
            LongIterator jt = c.longIterator();
            while (it.isValid() && jt.isValid()) {
                long i = it.value();
                long j = jt.value();
                if (i < j)
                    return -1;
                else if (i > j)
                    return 1;
                it.advance();
                jt.advance();
            }
            if (it.isValid())
                return 1;
            if (jt.isValid())
                return -1;
            return 0;
        }
    }

    static class StringUtils {
        public static String reverse(String s) {
            return new StringBuilder(s).reverse().toString();
        }
    }

    static class SubstringStringHash extends AbstractStringHash {
        private final StringHash base;
        private final int from;
        private final int to;

        public SubstringStringHash(StringHash base, int from) {
            this(base, from, base.length());
        }

        public SubstringStringHash(StringHash base, int from, int to) {
            this.base = base;
            this.from = from;
            this.to = to;
        }

        public long hash(int from, int to) {
            if (to + this.from > this.to) {
                throw new IllegalArgumentException("Trying to take substring out of bounds");
            }
            return base.hash(this.from + from, this.from + to);
        }

        public int length() {
            return to - from;
        }
    }

    interface LongCollection extends LongStream {
        int size();

        default void add(long value) {
            throw new UnsupportedOperationException();
        }

        default LongCollection addAll(LongStream values) {
            for (LongIterator it = values.longIterator(); it.isValid(); it.advance()) {
                add(it.value());
            }
            return this;
        }
    }

    static class SimpleStringHash extends AbstractStringHash {
        private static long[] firstReversePower = new long[0];
        private static long[] secondReversePower = new long[0];
        private final long[] firstHash;
        private final long[] secondHash;

        public SimpleStringHash(CharSequence string) {
            int length = string.length();
            ensureCapacity(length);
            firstHash = new long[length + 1];
            secondHash = new long[length + 1];
            long firstPower = 1;
            long secondPower = 1;
            for (int i = 0; i < length; i++) {
                firstHash[i + 1] = (firstHash[i] + string.charAt(i) * firstPower) % AbstractStringHash.FIRST_MOD;
                secondHash[i + 1] = (secondHash[i] + string.charAt(i) * secondPower) % AbstractStringHash.SECOND_MOD;
                firstPower *= AbstractStringHash.MULTIPLIER;
                firstPower %= AbstractStringHash.FIRST_MOD;
                secondPower *= AbstractStringHash.MULTIPLIER;
                secondPower %= AbstractStringHash.SECOND_MOD;
            }
        }

        private void ensureCapacity(int length) {
            if (firstReversePower.length >= length) {
                return;
            }
            length = Math.max(2 * firstReversePower.length, length);
            long[] oldFirst = firstReversePower;
            long[] oldSecond = secondReversePower;
            firstReversePower = new long[length];
            secondReversePower = new long[length];
            System.arraycopy(oldFirst, 0, firstReversePower, 0, oldFirst.length);
            System.arraycopy(oldSecond, 0, secondReversePower, 0, oldSecond.length);
            firstReversePower[0] = secondReversePower[0] = 1;
            for (int i = Math.max(oldFirst.length, 1); i < length; i++) {
                firstReversePower[i] = firstReversePower[i - 1] * AbstractStringHash.FIRST_REVERSE_MULTIPLIER % AbstractStringHash.FIRST_MOD;
                secondReversePower[i] = secondReversePower[i - 1] * AbstractStringHash.SECOND_REVERSE_MULTIPLIER % AbstractStringHash.SECOND_MOD;
            }
        }

        public long hash(int from, int to) {
            return (((firstHash[to] - firstHash[from] + AbstractStringHash.FIRST_MOD) * firstReversePower[from] % AbstractStringHash.FIRST_MOD) << 32) + ((secondHash[to] - secondHash[from] + AbstractStringHash.SECOND_MOD) * secondReversePower[from] % AbstractStringHash.SECOND_MOD);
        }

        public int length() {
            return firstHash.length - 1;
        }
    }

    static class LongHash {
        private LongHash() {
        }

        public static int hash(long c) {
            return Long.hashCode(c);
        }
    }

    static abstract class AbstractStringHash implements StringHash {
        public static final long MULTIPLIER;
        protected static final long FIRST_MOD;
        protected static final long SECOND_MOD;
        protected static final long FIRST_REVERSE_MULTIPLIER;
        protected static final long SECOND_REVERSE_MULTIPLIER;

        static {
            Random random = new Random(System.currentTimeMillis());
            FIRST_MOD = IntegerUtils.nextPrime((int) (1e9 + random.nextInt((int) 1e9)));
            SECOND_MOD = IntegerUtils.nextPrime((int) (1e9 + random.nextInt((int) 1e9)));
            MULTIPLIER = random.nextInt((int) 1e9 - 257) + 257;
            FIRST_REVERSE_MULTIPLIER = IntegerUtils.reverse(MULTIPLIER, FIRST_MOD);
            SECOND_REVERSE_MULTIPLIER = IntegerUtils.reverse(MULTIPLIER, SECOND_MOD);
        }

        public long hash(int from) {
            return hash(from, length());
        }
    }

    interface LongList extends LongReversableCollection {
        long get(int index);

        void addAt(int index, long value);

        void removeAt(int index);

        default LongIterator longIterator() {
            return new LongIterator() {
                private int at;
                private boolean removed;

                @Override
                public long value() throws NoSuchElementException {
                    if (removed) {
                        throw new IllegalStateException();
                    }
                    return get(at);
                }

                @Override
                public boolean advance() {
                    at++;
                    removed = false;
                    return isValid();
                }

                @Override
                public boolean isValid() {
                    return !removed && at < size();
                }

                public void remove() {
                    removeAt(at);
                    at--;
                    removed = true;
                }
            };
        }

        default void add(long value) {
            addAt(size(), value);
        }
    }

    static class LongArray extends LongAbstractStream implements LongList {
        private long[] data;

        public LongArray(long[] data) {
            this.data = data;
        }

        public int size() {
            return data.length;
        }

        public long get(int index) {
            return data[index];
        }

        public void addAt(int index, long value) {
            throw new UnsupportedOperationException();
        }

        public void removeAt(int index) {
            throw new UnsupportedOperationException();
        }
    }

    static abstract class LongAbstractStream implements LongStream {
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            boolean first = true;
            for (LongIterator it = longIterator(); it.isValid(); it.advance()) {
                if (first) {
                    first = false;
                } else {
                    builder.append(' ');
                }
                builder.append(it.value());
            }
            return builder.toString();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof LongStream)) {
                return false;
            }
            LongStream other = (LongStream) obj;
            LongIterator it = longIterator();
            LongIterator jt = other.longIterator();
            while (it.isValid() && jt.isValid()) {
                if (it.value() != jt.value()) {
                    return false;
                }
                it.advance();
                jt.advance();
            }
            return !it.isValid() && !jt.isValid();
        }

        @Override
        public int hashCode() {
            int result = 0;
            for (LongIterator it = longIterator(); it.isValid(); it.advance()) {
                result *= 31;
                result += Long.hashCode(it.value());
            }
            return result;
        }
    }

    static class IntegerUtils {
        public static int gcd(int a, int b) {
            a = Math.abs(a);
            b = Math.abs(b);
            while (b != 0) {
                int temp = a % b;
                a = b;
                b = temp;
            }
            return a;
        }

        public static long power(long base, long exponent, long mod) {
            if (base >= mod)
                base %= mod;
            if (exponent == 0)
                return 1 % mod;
            long result = power(base, exponent >> 1, mod);
            result = result * result % mod;
            if ((exponent & 1) != 0)
                result = result * base % mod;
            return result;
        }

        public static long reverse(long number, long module) {
            return power(number, module - 2, module);
        }

        public static boolean isPrime(long number) {
            if (number < 2)
                return false;
            for (long i = 2; i * i <= number; i++) {
                if (number % i == 0)
                    return false;
            }
            return true;
        }

        public static long nextPrime(long from) {
            if (from <= 2)
                return 2;
            from += 1 - (from & 1);
            while (!isPrime(from))
                from += 2;
            return from;
        }
    }

    static class CompositeStringHash extends AbstractStringHash {
        private static long[] firstPower = new long[0];
        private static long[] secondPower = new long[0];
        private final StringHash first;
        private final StringHash second;

        public CompositeStringHash(StringHash first, StringHash second) {
            this.first = first;
            this.second = second;
            ensureCapacity(first.length() + 1);
        }

        private void ensureCapacity(int length) {
            if (firstPower.length >= length) {
                return;
            }
            length = Math.max(length + 1, firstPower.length << 1);
            long[] oldFirst = firstPower;
            long[] oldSecond = secondPower;
            firstPower = new long[length];
            secondPower = new long[length];
            System.arraycopy(oldFirst, 0, firstPower, 0, oldFirst.length);
            System.arraycopy(oldSecond, 0, secondPower, 0, oldSecond.length);
            for (int i = Math.max(oldFirst.length, 1); i < length; i++) {
                firstPower[i] = firstPower[i - 1] * AbstractStringHash.MULTIPLIER % AbstractStringHash.FIRST_MOD;
                secondPower[i] = secondPower[i - 1] * AbstractStringHash.MULTIPLIER % AbstractStringHash.SECOND_MOD;
            }
        }

        public long hash(int from, int to) {
            long firstFirst;
            long firstSecond;
            long secondFirst;
            long secondSecond;
            if (to <= first.length()) {
                secondFirst = 0;
                secondSecond = 0;
            } else {
                long value = second.hash(Math.max(0, from - first.length()), to - first.length());
                secondFirst = value >>> 32;
                secondSecond = value & ((1L << 32) - 1);
            }
            if (from >= first.length()) {
                firstFirst = 0;
                firstSecond = 0;
            } else {
                long value = first.hash(from, to);
                firstFirst = value >>> 32;
                firstSecond = value & ((1L << 32) - 1);
            }
            return (((firstFirst + secondFirst * firstPower[Math.max(0, first.length() - from)]) % AbstractStringHash.FIRST_MOD) << 32) + ((firstSecond + secondSecond * firstPower[Math.max(0, first.length() - from)]) % AbstractStringHash.SECOND_MOD);
        }

        public int length() {
            return first.length() + second.length();
        }
    }

    static class IOUtils {
        public static String[] readStringArray(InputReader2 in, int size) {
            String[] array = new String[size];
            for (int i = 0; i < size; i++) {
                array[i] = in.readString();
            }
            return array;
        }
    }

    static int[][] dir = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    static int n;

    public static int gridlandProvinces(String s1, String s2) {

        return 0;
    }


    private static void dfs(char[][] grid, boolean[][] visited, int i, int j, StringBuilder sb, Set<String> set) {
        if (sb.length() == n) {
            set.add(sb.toString());
            return;
        }
        if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length || visited[i][j]) {
            return;
        }
        visited[i][j] = true;
        sb.append(grid[i][j]);
        for (int[] d : dir) {
            dfs(grid, visited, i + d[0], j + d[1], sb, set);
        }
        visited[i][j] = false;
        sb.deleteCharAt(sb.length() - 1);
    }

    private static boolean isVisitedAll(boolean[][] visited) {
        for (boolean[] booleans : visited) {
            for (int j = 0; j < visited[0].length; j++) {
                if (!booleans[j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        String out = InputReader.inputAsStrings("gridland-provinces.txt")
                .map(line -> line.split(","))
                .map(arr -> gridlandProvinces(arr[0], arr[1]))
                .map(String::valueOf)
                .collect(Collectors.joining(System.lineSeparator()));
        OutputWriter.write(out, "gridland-provinces.txt");
    }
}
