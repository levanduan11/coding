package hard;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static java.util.stream.IntStream.range;


public class TextJustification {
    static int maxW;

    public static List<String> fullJustify(String[] words, int maxWidth) {
        maxW = maxWidth;
        List<String> ans = new ArrayList<>();
        List<String> tmp = new ArrayList<>();
        int space = 0, count = 0;
        boolean isEnd = false;
        for (int i = 0; i < words.length; i++) {
            int wordLen = words[i].length();
            if (count + wordLen <= maxWidth) {
                tmp.add(words[i]);
                count += wordLen;
                if (count + 1 <= maxWidth) {
                    space++;
                    count += 1;
                }

            } else {
                while (count < maxWidth) {
                    space++;
                    count += 1;
                }

                String clean = formatLine(tmp, space);
                ans.add(clean);
                space = count = 0;

                tmp = new ArrayList<>();
                tmp.add(words[i]);
                count += (1 + words[i].length());
                if (count + 1 <= maxWidth) {
                    space += 1;
                }
                if (i == words.length - 1) {
                    isEnd = true;
                    while (count < maxWidth) {
                        count += 1;
                        space += 1;
                    }
                    clean = String.join("", tmp);
                    int remain = maxWidth - clean.length();
                    ans.add(clean.concat(" ".repeat(remain)));
                }
            }
        }
        if (count < maxWidth || !isEnd) {
            while (count < maxWidth) {
                count += 1;
                space += 1;
            }
            String v = String.join(" ", tmp);
            int remain = maxWidth - v.length();
            v = v.concat(" ".repeat(remain));
            ans.add(v);
        }

        return ans;
    }

    private static String formatLine(List<String> tmp, int space) {
        StringBuilder sb = new StringBuilder();
        int wordLen = tmp.size();
        int shouldSpaceLen = wordLen - 1;
        if (shouldSpaceLen == 0) {
            String v = tmp.get(0);
            int remain = maxW - v.length();
            return String.join("", tmp).concat(" ".repeat(remain));
        }
        int a = space / shouldSpaceLen;
        int b = space % shouldSpaceLen;
        for (String s : tmp) {
            sb.append(s);
            if (shouldSpaceLen > 0) {
                sb.append(" ".repeat(a));
                shouldSpaceLen--;
            }
            if (b > 0) {
                sb.append(" ");
                b--;
            }
        }
        return sb.toString();
    }

    public static List<String> fullJustify2(String[] words, int maxWidth) {
        List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        int start = 0, length = 0;
        for (int i = 0; i < words.length; i++) {
            if (words[i].length() <= (maxWidth - length)) {
                length += words[i].length() + 1;
            } else {
                int remainingSpaces = maxWidth - length + 1;
                int noOfWords = i - start - 1;
                int extraSpace = 0;

                if (noOfWords > 0) {
                    extraSpace = remainingSpaces / noOfWords;
                    remainingSpaces = remainingSpaces % noOfWords;
                }

                while (start < i - 1) {
                    sb.append(words[start++]).append(" ");
                    int k = 0;
                    while (k < extraSpace) {
                        sb.append(" ");
                        k++;
                    }
                    if (remainingSpaces > 0) {
                        sb.append(" ");
                        remainingSpaces--;
                    }
                }
                sb.append(words[start]);

                while (remainingSpaces > 0) {
                    sb.append(" ");
                    remainingSpaces--;
                }
                result.add(sb.toString());
                start = i;
                length = words[i].length() + 1;
                sb.setLength(0);
            }
        }
        sb.setLength(0);
        while (start < words.length - 1) {
            sb.append(words[start++]).append(" ");
        }
        sb.append(words[start]);
        while (sb.length() < maxWidth) {
            sb.append(" ");
        }
        result.add(sb.toString());
        return result;

    }

    static String randomString(int len) {
        var ran = new SecureRandom();
        var charPool = new StringBuilder();
        var res = new StringBuilder();
        range(0, 26)
                .flatMap(i -> {
                    if (i < 10) {
                        return IntStream.of(i + '0');
                    }
                    return IntStream.of((char) (i + 'a'), (char) (i + 'A'));
                })
                .forEach(charPool::appendCodePoint);
        range(0, len)
                .map(unused -> ran.nextInt(charPool.length()))
                .boxed()
                .map(charPool::charAt)
                .forEach(res::append);
        return res.toString();
    }

    public static void main(String[] args) {
        String[] words = {"Here", "is", "an", "example", "of", "text", "justification."};
        int maxWidth = 15;
        int full = Arrays.stream(words)
                .map(String::length)
                .reduce(Integer::sum)
                .orElse(0);

        var m = new MyIterable(range(0, 10).toArray());

        for (int a : m) {
            System.out.println(a);
        }
    }
}

class MyIterable implements Iterable<Integer> {
    private final int[] data;

    MyIterable(int[] data) {
        this.data = data;
    }

    public void forEx(Consumer<? super Integer> action) {
        for (int a : data) {
            action.accept(a);
        }
    }


    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<>() {
            int pointer;

            @Override
            public boolean hasNext() {
                return pointer < data.length;
            }

            @Override
            public Integer next() {
                return data[pointer++];
            }
        };
    }
}