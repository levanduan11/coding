package com.coding.datastructure.array;


import javax.sound.midi.MidiFileFormat;
import java.io.ObjectStreamField;
import java.io.Serial;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.LongBuffer;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public class BitArray implements Cloneable, Serializable {
    private static final int ADDRESS_BITS_PER_WORD = 6;
    private static final int BITS_PER_WORD = 1 << ADDRESS_BITS_PER_WORD;
    private static final int BIT_INDEX_MASK = BITS_PER_WORD - 1;
    private static final long WORD_MASK = 0xffffffffffffffffL;
    @Serial
    private static final ObjectStreamField[] serialPersistentFields = {
            new ObjectStreamField("bits", long[].class)
    };
    private long[] words;
    private transient int wordsInUse = 0;
    private transient boolean sizeIsSticky = false;
    private static final long serialVersionUID = 1L;

    private static int wordIndex(int bitIndex) {
        return bitIndex >> ADDRESS_BITS_PER_WORD;
    }

    private void checkInvariants() {
        assert (wordsInUse == 0 || words[wordsInUse - 1] != 0);
        assert (wordsInUse >= 0 && wordsInUse <= words.length);
        assert (wordsInUse == words.length || words[wordsInUse - 1] == 0);
    }

    private void recalculateWordsInUse() {
        int i;
        for (i = wordsInUse - 1; --i >= 0; ) {
            if (words[i] != 0)
                break;
        }
        wordsInUse = i + 1;
    }

    public BitArray() {
        initWords(BITS_PER_WORD);
        sizeIsSticky = false;
    }

    public BitArray(int nBits) {
        if (nBits < 0)
            throw new IllegalArgumentException();
        initWords(nBits);
        sizeIsSticky = true;
    }

    private void initWords(int nBits) {
        words = new long[wordIndex(nBits - 1) + 1];
    }

    private BitArray(long[] words) {
        this.words = words;
        this.wordsInUse = words.length;
        checkInvariants();
    }

    public static BitArray valueOf(long[] longs) {
        int n;
        //noinspection StatementWithEmptyBody
        for (n = longs.length; n > 0 && longs[n - 1] == 0; n--) ;
        return new BitArray(Arrays.copyOf(longs, n));
    }

    public static BitArray valueOf(LongBuffer lb) {
        lb = lb.slice();
        int n;
        for (n = lb.remaining(); n > 0 && lb.get(n - 1) == 0; n--) ;
        long[] words = new long[n];
        lb.get(words);
        return new BitArray(words);
    }

    public static BitArray valueOf(byte[] bytes) {
        return BitArray.valueOf(ByteBuffer.wrap(bytes));
    }

    public static BitArray valueOf(ByteBuffer bb) {
        bb = bb.slice().order(ByteOrder.LITTLE_ENDIAN);
        int n;
        for (n = bb.remaining(); n > 0 && bb.get(n - 1) == 0; n--) ;
        long[] words = new long[(n + 7) / 8];
        bb.limit(n);
        int i = 0;
        while (bb.remaining() >= 8)
            words[i++] = bb.getLong();
        for (int remaining = bb.remaining(), j = 0; j < remaining; j++) {
            words[i] |= (bb.get() & 0xffL) << (8 * j);
        }
        return new BitArray(words);
    }

    public byte[] toByteArray() {
        int n = wordsInUse;
        if (n == 0)
            return new byte[0];
        int len = 8 * (n - 1);
        for (long x = words[n - 1]; x != 0; x >>>= 8) {
            len++;
        }
        byte[] bytes = new byte[len];
        ByteBuffer bb = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i < n - 1; i++) {
            bb.putLong(words[i]);
        }
        for (long x = words[n - 1]; x != 0; x >>>= 8) {
            bb.put((byte) (x & 0xff));
        }
        return bytes;
    }

    public long[] toLongArray() {
        return Arrays.copyOf(words, wordsInUse);
    }

    private void ensureCapacity(int wordsRequired) {
        if (words.length < wordsRequired) {
            int request = Math.max(2 * words.length, wordsRequired);
            words = Arrays.copyOf(words, request);
            sizeIsSticky = false;
        }
    }

    private void expandTo(int wordIndex) {
        int wordsRequired = wordIndex + 1;
        if (wordsInUse < wordsRequired) {
            ensureCapacity(wordsRequired);
            wordsInUse = wordsRequired;
        }
    }

    private static void checkRange(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex < 0 || fromIndex > toIndex)
            throw new IndexOutOfBoundsException();
    }

    public void flip(int bitIndex) {
        if (bitIndex < 0)
            throw new IndexOutOfBoundsException();
        int wordIndex = wordIndex(bitIndex);
        expandTo(wordIndex);
        words[wordIndex] ^= (1L << bitIndex);
        recalculateWordsInUse();
        checkInvariants();
    }

    public void flip(int fromIndex, int toIndex) {
        checkRange(fromIndex, toIndex);
        if (fromIndex == toIndex)
            return;
        int startWordIndex = wordIndex(fromIndex);
        int endWordIndex = wordIndex(toIndex - 1);
        expandTo(endWordIndex);
        long firstWordMask = WORD_MASK << fromIndex;
        long lastWordMask = WORD_MASK >>> -toIndex;
        if (startWordIndex == endWordIndex) {
            words[startWordIndex] ^= (firstWordMask & lastWordMask);
        } else {
            words[startWordIndex] ^= firstWordMask;
            for (int i = startWordIndex + 1; i < endWordIndex; i++) {
                words[i] ^= WORD_MASK;
            }
            words[endWordIndex] ^= lastWordMask;
        }
        recalculateWordsInUse();
        checkInvariants();
    }

    public void set(int bitIndex) {
        if (bitIndex < 0)
            throw new IndexOutOfBoundsException();
        int wordIndex = wordIndex(bitIndex);
        expandTo(wordIndex);
        words[wordIndex] |= (1L << bitIndex);
        checkInvariants();
    }

    public void set(int bitIndex, boolean value) {
        if (value)
            set(bitIndex);
        else
            clear(bitIndex);
    }

    public void set(int fromIndex, int toIndex) {
        checkRange(fromIndex, toIndex);
        if (fromIndex == toIndex)
            return;
        int startWordIndex = wordIndex(fromIndex);
        int endWordIndex = wordIndex(toIndex - 1);
        expandTo(endWordIndex);
        long firstWordMask = WORD_MASK << fromIndex;
        long lastWordMask = WORD_MASK >>> -toIndex;
        if (startWordIndex == endWordIndex) {
            words[startWordIndex] |= (firstWordMask & lastWordMask);
        } else {
            words[startWordIndex] |= firstWordMask;
            for (int i = startWordIndex + 1; i < endWordIndex; i++) {
                words[i] = WORD_MASK;
            }
            words[endWordIndex] |= lastWordMask;
        }
        checkInvariants();
    }

    public void set(int fromIndex, int toIndex, boolean value) {
        if (value)
            set(fromIndex, toIndex);
        else
            clear(fromIndex, toIndex);
    }

    public void clear(int bitIndex) {
        if (bitIndex < 0)
            throw new IndexOutOfBoundsException();
        int wordIndex = wordIndex(bitIndex);
        if (wordIndex >= wordsInUse)
            return;
        words[wordIndex] &= ~(1L << bitIndex);
        recalculateWordsInUse();
        checkInvariants();
    }

    public void clear(int fromIndex, int toIndex) {
        checkRange(fromIndex, toIndex);
        if (fromIndex == toIndex)
            return;
        int startWordIndex = wordIndex(fromIndex);
        if (startWordIndex >= wordsInUse)
            return;
        int endWordIndex = wordIndex(toIndex);
        if (endWordIndex >= wordsInUse) {
            toIndex = length();
            endWordIndex = wordsInUse - 1;
        }
        long firstWordMask = WORD_MASK << fromIndex;
        long lastWordMask = WORD_MASK >>> -toIndex;
        if (startWordIndex == endWordIndex) {
            words[startWordIndex] &= ~(firstWordMask & lastWordMask);
        } else {
            words[startWordIndex] &= ~firstWordMask;
            for (int i = startWordIndex + 1; i < endWordIndex; i++) {
                words[i] = 0;
            }
            words[endWordIndex] &= ~lastWordMask;
        }
        recalculateWordsInUse();
        checkInvariants();
    }

    public void clear() {
        while (wordsInUse > 0)
            words[--wordsInUse] = 0;
    }

    public boolean get(int bitIndex) {
        if (bitIndex < 0)
            throw new IndexOutOfBoundsException();
        checkInvariants();
        int wordIndex = wordIndex(bitIndex);
        return (wordIndex < wordsInUse) && ((words[wordIndex] & (1L << bitIndex)) != 0);
    }

    public BitArray get(int fromIndex, int toIndex) {
        checkRange(fromIndex, toIndex);
        checkInvariants();
        int len = length();
        if (len <= fromIndex || fromIndex == toIndex)
            return new BitArray(0);
        if (toIndex > len)
            toIndex = len;
        BitArray result = new BitArray(toIndex - fromIndex);
        int targetWords = wordIndex(toIndex - fromIndex - 1) + 1;
        int sourceIndex = wordIndex(fromIndex);
        boolean wordAligned = ((fromIndex & BITS_PER_WORD) == 0);
        for (int i = 0; i < targetWords - 1; i++, sourceIndex++) {
            result.words[i] = wordAligned ? words[sourceIndex] :
                    (words[sourceIndex] >>> fromIndex) |
                            (words[sourceIndex + 1] << -fromIndex);
        }
        long lasWordMask = WORD_MASK >>> -toIndex;
        result.words[targetWords - 1] =
                ((toIndex - 1) & BITS_PER_WORD) < (fromIndex & BIT_INDEX_MASK)
                        ? ((words[sourceIndex] >> fromIndex) | (words[sourceIndex] & lasWordMask) << -fromIndex)
                        : ((words[sourceIndex]) >>> fromIndex);
        result.wordsInUse = targetWords;
        result.recalculateWordsInUse();
        result.checkInvariants();
        return result;
    }

    public int nextSetBit(int fromIndex) {
        if (fromIndex < 0)
            throw new IndexOutOfBoundsException();
        checkInvariants();
        int u = wordIndex(fromIndex);
        if (u >= wordsInUse)
            return -1;
        long word = words[u] & (WORD_MASK << fromIndex);
        while (true) {
            if (word != 0)
                return (u * BITS_PER_WORD) + Long.numberOfTrailingZeros(word);
            if (++u == wordsInUse)
                return -1;
            word = words[u];
        }
    }

    public int nextClearBit(int fromIndex) {
        if (fromIndex < 0)
            throw new IndexOutOfBoundsException();
        checkInvariants();
        int u = wordIndex(fromIndex);
        if (u >= wordsInUse)
            return fromIndex;
        long word = ~words[u] & (WORD_MASK << fromIndex);
        while (true) {
            if (word != 0)
                return (u * BITS_PER_WORD) + Long.numberOfTrailingZeros(word);
            if (++u == wordsInUse)
                return wordsInUse * BITS_PER_WORD;
            word = ~words[u];
        }
    }

    public int previousSetBit(int fromIndex) {
        if (fromIndex < 0) {
            if (fromIndex == -1)
                return -1;
            throw new IndexOutOfBoundsException();
        }
        checkInvariants();
        int u = wordIndex(fromIndex);
        if (u >= wordsInUse)
            return length() - 1;
        long word = words[u] & (WORD_MASK >>> -(fromIndex + 1));
        while (true) {
            if (word != 0)
                return (u + 1) * BITS_PER_WORD - 1 - Long.numberOfLeadingZeros(word);
            if (u-- == 0)
                return -1;
            word = words[u];
        }
    }

    public int previousClearBit(int fromIndex) {
        if (fromIndex < 0) {
            if (fromIndex == -1)
                return -1;
            throw new IndexOutOfBoundsException();
        }
        checkInvariants();
        int u = wordIndex(fromIndex);
        if (u >= wordsInUse)
            return fromIndex;
        long word = ~words[u] & (WORD_MASK >>> -(fromIndex + 1));
        while (true) {
            if (word != 0)
                return (u + 1) * BITS_PER_WORD - 1 - Long.numberOfLeadingZeros(word);
            if (u-- == 0)
                return -1;
            word = ~words[u];
        }
    }

    public int length() {
        if (wordsInUse == 0)
            return 0;
        return BITS_PER_WORD * (wordsInUse - 1) +
                (BITS_PER_WORD - Long.numberOfLeadingZeros(words[wordsInUse - 1]));
    }

    public boolean isEmpty() {
        return wordsInUse == 0;
    }

    public boolean intersects(BitArray bitArray) {
        for (int i = Math.min(wordsInUse, bitArray.wordsInUse); --i >= 0; ) {
            if ((words[i] & bitArray.words[i]) != 0)
                return true;
        }
        return false;
    }

    public int cardinality() {
        int sum = 0;
        for (int i = 0; i < wordsInUse; i++) {
            sum += Long.bitCount(words[i]);
        }
        return sum;
    }

    public void and(BitArray bitArray) {
        if (this == bitArray)
            return;
        while (wordsInUse > bitArray.wordsInUse)
            words[--wordsInUse] = 0;
        for (int i = 0; i < wordsInUse; i++) {
            words[i] &= bitArray.words[i];
        }
        recalculateWordsInUse();
        checkInvariants();
    }

    public void or(BitArray bitArray) {
        if (this == bitArray)
            return;
        int wordsInCommon = Math.min(wordsInUse, bitArray.wordsInUse);
        if (wordsInUse < bitArray.wordsInUse) {
            ensureCapacity(bitArray.wordsInUse);
            wordsInUse = bitArray.wordsInUse;
        }
        for (int i = 0; i < wordsInCommon; i++) {
            words[i] |= bitArray.words[i];
        }
        if (wordsInCommon < bitArray.wordsInUse) {
            System.arraycopy(bitArray.words, wordsInCommon, words, wordsInCommon, wordsInUse - wordsInCommon);
        }
        checkInvariants();
    }

    public void xor(BitArray bitArray) {
        int wordsInCommon = Math.min(wordsInUse, bitArray.wordsInUse);
        if (wordsInUse < bitArray.wordsInUse) {
            ensureCapacity(bitArray.wordsInUse);
            wordsInUse = bitArray.wordsInUse;
        }
        for (int i = 0; i < wordsInCommon; i++) {
            words[i] ^= bitArray.words[i];
        }
        if (wordsInCommon < bitArray.wordsInUse) {
            System.arraycopy(bitArray.words, wordsInCommon, words, wordsInCommon, bitArray.wordsInUse - wordsInCommon);
        }
        recalculateWordsInUse();
        checkInvariants();
    }

    public void andNot(BitArray bitArray) {
        for (int i = Math.min(wordsInUse, bitArray.wordsInUse); --i >= 0; ) {
            words[i] &= ~bitArray.words[i];
        }
        recalculateWordsInUse();
        checkInvariants();
    }

    public int size() {
        return words.length * BITS_PER_WORD;
    }

    @Override
    public Object clone() {
        if (!sizeIsSticky)
            trimToSize();
        try {
            BitArray result = (BitArray) super.clone();
            result.words = words.clone();
            result.checkInvariants();
            return result;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    private void trimToSize() {
        if (wordsInUse != words.length) {
            words = Arrays.copyOf(words, wordsInUse);
            checkInvariants();
        }
    }

    public IntStream stream() {
        class BitSetSpliterator implements Spliterator.OfInt {
            private int index;
            private int fence;
            private int est;
            private boolean root;

            public BitSetSpliterator(int origin, int fence, int est, boolean root) {
                this.index = origin;
                this.fence = fence;
                this.est = est;
                this.root = root;
            }

            private int getFence() {
                int hi;
                if ((hi = fence) < 0) {
                    hi = fence = (wordsInUse >= wordIndex(Integer.MAX_VALUE)) ? Integer.MAX_VALUE : wordsInUse << ADDRESS_BITS_PER_WORD;
                    est = cardinality();
                    index = nextSetBit(0);
                }
                return hi;
            }

            @Override
            public OfInt trySplit() {
                int hi = getFence();
                int lo = index;
                if (lo < 0)
                    return null;
                hi = fence = (hi < Integer.MAX_VALUE || !get(Integer.MAX_VALUE))
                        ? previousSetBit(hi - 1) + 1
                        : Integer.MAX_VALUE;
                int mid = (lo + hi) >>> 1;
                if (lo >= mid)
                    return null;
                index = nextSetBit(mid, wordIndex(hi - 1));
                root = false;
                return new BitSetSpliterator(lo, mid, est >>>= 1, false);
            }

            @Override
            public long estimateSize() {
                getFence();
                return est;
            }

            @Override
            public int characteristics() {
                return (root ? Spliterator.SIZED : 0) | Spliterator.ORDERED | Spliterator.DISTINCT | Spliterator.SORTED;
            }

            @Override
            public boolean tryAdvance(IntConsumer action) {
                Objects.requireNonNull(action);
                int hi = getFence();
                int i = index;
                if (i < 0 || i >= hi) {
                    if (i == Integer.MAX_VALUE && hi == Integer.MAX_VALUE) {
                        index = -1;
                        action.accept(Integer.MAX_VALUE);
                        return true;
                    }
                    return false;
                }
                index = nextSetBit(i + 1, wordIndex(hi - 1));
                action.accept(i);
                return true;
            }

            @Override
            public void forEachRemaining(IntConsumer action) {
                Objects.requireNonNull(action);
                int hi = getFence();
                int i = index;
                index = -1;
                if (i >= 0 && i < hi) {
                    action.accept(i++);
                    int u = wordIndex(i);
                    int v = wordIndex(hi - 1);
                    words_loop:
                    for (; u <= v && i <= hi; u++, i = u << ADDRESS_BITS_PER_WORD) {
                        long word = words[u] & (WORD_MASK << i);
                        while (word != 0) {
                            i = (u << ADDRESS_BITS_PER_WORD) + Long.numberOfTrailingZeros(word);
                            if (i >= hi)
                                break words_loop;
                            word &= ~(1L << i);
                            action.accept(i);
                        }
                    }
                }
                if (i == Integer.MAX_VALUE && hi == Integer.MAX_VALUE)
                    action.accept(Integer.MAX_VALUE);
            }
        }
        return StreamSupport.intStream(new BitSetSpliterator(0, -1, 0, true), false);
    }

    private int nextSetBit(int fromIndex, int toWordIndex) {
        int u = wordIndex(fromIndex);
        if (u > toWordIndex)
            return -1;
        long word = words[u] & (WORD_MASK << fromIndex);
        while (true) {
            if (word != 0)
                return (u * BITS_PER_WORD) + Long.numberOfTrailingZeros(word);
            if (++u > toWordIndex)
                return -1;
            word = words[u];
        }
    }
}
