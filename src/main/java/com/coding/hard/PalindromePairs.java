package com.coding.hard;

import com.coding.InputReader;
import com.coding.OutputWriter;

import java.util.*;
import java.util.stream.Collectors;

public class PalindromePairs {
    private static boolean isPalindrome(String s) {
        for (int i = 0, n = s.length(), j = n - 1; i < j; i++, j--) {
            if (s.charAt(i) != s.charAt(j))
                return false;
        }
        return true;
    }

    public static List<List<Integer>> palindromePairs(String[] words) {
        List<List<Integer>> pairs = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            map.put(words[i], i);
        }
        for (int i = 0; i < words.length; i++) {
            for (int j = 0; j <= words[i].length(); j++) {
                String prefix = words[i].substring(0, j);
                String suffix = words[i].substring(j);

                if (isPalindrome(prefix)) {
                    String suffixRev = new StringBuilder(suffix).reverse().toString();
                    if (map.containsKey(suffixRev) && map.get(suffixRev) != i) {
                        pairs.add(Arrays.asList(map.get(suffixRev), i));
                    }
                }
                if (!suffix.isEmpty() && isPalindrome(suffix)) {
                    String prefixRev = new StringBuilder(prefix).reverse().toString();
                    if (map.containsKey(prefixRev) && map.get(prefixRev) != i) {
                        pairs.add(Arrays.asList(i, map.get(prefixRev)));
                    }
                }

            }
        }
        return pairs;

    }

    Map<Object, Integer> wordToIndexMap;
    boolean[] hasWordSize;
    List<List<Integer>> result;
    Word[] wordObjects;

    public List<List<Integer>> palindromePairsV2(String[] words) {
        wordToIndexMap = new HashMap<>();
        hasWordSize = new boolean[350];
        result = new ArrayList<>();
        wordObjects = new Word[words.length];

        Integer emptyWordIndex = null;
        for (int i = 0; i < words.length; i++) {
            Word word = new Word(words[i]);
            wordObjects[i] = word;
            mapPut(word, i);
        }
        emptyWordIndex = mapGet(new Word(""));
        for (int i = 0; i < words.length; i++) {
            Word word = wordObjects[i];
            if (word.isEmpty())
                continue;
            boolean isPalindrome = word.isPalindrome();
            if (emptyWordIndex != null && isPalindrome) {
                resulAdd(i, emptyWordIndex);
                resulAdd(emptyWordIndex, i);
            }
            Word reversedWord = word.reverse();
            Integer reversedWordIndex = mapGet(reversedWord);
            if (!isPalindrome && reversedWordIndex != null)
                resulAdd(i, reversedWordIndex);
            for (int wi = 0; wi < word.len; wi++) {
                if (hasWordSize[wi] && word.isPalindromeFrom(wi)) {
                    Integer reversedSubIndex = mapGet(word.reverseUpTo(wi));
                    if (reversedSubIndex != null)
                        resulAdd(i, reversedSubIndex);
                }
                if (hasWordSize[word.length() - wi] && word.isPalindromeFrom(wi)) {
                    Integer reversedSubIndex = mapGet(word.reverseFrom(wi));
                    if (reversedSubIndex != null)
                        resulAdd(i, reversedSubIndex);
                }
            }
        }
        return result;
    }

    void mapPut(Word word, int index) {
        hasWordSize[word.length()] = true;
        wordToIndexMap.put(word, index);
    }

    Integer mapGet(Word word) {
        if (!hasWordSize[word.length()])
            return null;
        return wordToIndexMap.get(word);
    }

    boolean isPalindromeFrom(String word, int from) {
        return isPalindrome(word, from, word.length());
    }

    boolean isPalindromeUpTo(String word, int to) {
        return isPalindrome(word, 0, to);
    }

    boolean isPalindrome(String word, int from, int to) {
        int left = from;
        int right = to - 1;
        while (left < right) {
            if (word.charAt(left) != word.charAt(right))
                return false;
            left++;
            right--;
        }
        return true;
    }

    String reverse(String word) {
        return new StringBuilder(word).reverse().toString();
    }

    String reverseFrom(String word, int from) {
        return reverse(word.substring(from));
    }

    String reverseUpTo(String word, int to) {
        return reverse(word.substring(0, to));
    }

    void resulAdd(int i, int j) {
        result.add(List.of(i, j));
    }

    static class Word {
        char[] word;
        int fromInclusive;
        int toExclusive;
        int incr;
        int len;
        Integer hash;

        Word() {
        }

        Word(String word) {
            this(word.toCharArray(), 0, word.length() - 1);
        }

        Word(char[] word, int fromInclusive, int toExclusive) {
            this.incr = (toExclusive >= fromInclusive) ? 1 : -1;
            this.word = word;
            this.fromInclusive = fromInclusive;
            this.toExclusive = toExclusive;
            updateLength();

        }

        boolean isEmpty() {
            return len == 0;
        }

        int length() {
            return len;
        }

        void updateLength() {
            len = (toExclusive == -1) ? 0 : (toExclusive - fromInclusive + incr) * incr;
        }

        boolean isPalindrome() {
            return isPalindrome(0, len);
        }

        boolean isPalindromeFrom(int from) {
            return isPalindrome(from, len);
        }

        boolean isPalindromeUpTo(int to) {
            return isPalindrome(0, to);
        }

        boolean isPalindrome(int from, int to) {
            if (len <= 1) {
                return true;
            }
            int left = from;
            int right = to - 1;
            while (left < right) {
                if (word[left] != word[right]) {
                    return false;
                }
                left++;
                right--;
            }
            return true;
        }

        Word reverse() {
            if (toExclusive == -1)
                return new Word(word, fromInclusive, toExclusive);
            return new Word(word, toExclusive, fromInclusive);
        }

        Word reverseFrom(int from) {
            if (toExclusive == -1)
                return new Word(word, fromInclusive, toExclusive);
            return new Word(word, toExclusive, fromInclusive + (from * incr));
        }

        Word reverseUpTo(int to) {
            if (toExclusive == -1)
                return new Word(word, fromInclusive, toExclusive);
            int lenDiff = Math.abs(len - to);
            return new Word(word, toExclusive - lenDiff * incr, fromInclusive);
        }

        @Override
        public int hashCode() {
            if (hash != null)
                return hash;
            int h = 0;
            int i = fromInclusive;
            int count = len;
            while (count-- > 0) {
                h = 31 * h + word[i];
                i += incr;
            }
            hash = h;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!(obj instanceof Word that))
                return false;
            char[] otherWord = that.word;
            int otherIncr = that.incr;
            int otherLen = that.length();
            if (len != otherLen)
                return false;
            if (otherLen == 0)
                return true;
            int otherIndex = that.fromInclusive;
            int myIndex = fromInclusive;
            int count = otherLen;
            while (count-- > 0) {
                if (word[myIndex] != otherWord[otherIndex])
                    return false;
                myIndex += incr;
                otherIndex += otherIncr;
            }
            return true;
        }

        @Override
        public String toString() {
            return new String(word, fromInclusive, len);
        }
    }

    public static void main(String[] args) {
        String out = InputReader.inputAsStrings("336.txt")
                .map(line -> {
                    String[] strings = Arrays.stream(line.split(","))
                            .map(s -> s.substring(1, s.length() - 1))
                            .toArray(String[]::new);
                    return palindromePairs(strings);
                })
                .map(List::toString)
                .collect(Collectors.joining(System.lineSeparator()));
        OutputWriter.write(out, "336.txt");
    }
}
