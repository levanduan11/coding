package hard;

import java.util.*;

public class SubstringConcatenationAllWords {
    static List<Integer> result;

    public static List<Integer> findSubstring1(String input, String[] words) {
        return new AbstractList<Integer>() {
            @Override
            public Integer get(int index) {
                if (result == null) {
                    init();
                }
                return result.get(index);
            }

            @Override
            public int size() {
                if (result == null) {
                    init();
                }
                return result.size();
            }

            private void init() {
                HashMap<String, Integer> wordsMap = new HashMap<>();
                for (String w : words) {
                    wordsMap.put(w, wordsMap.getOrDefault(w, 0) + 1);
                }
                result = new ArrayList<>();
                int wordLength = words[0].length();
                String subString;
                HashMap<String, Integer> wordMapClone = null;
                int j = 0;
                for (int i = 0; i <= (input.length() - wordLength); i++) {
                    j = i;
                    while (j <= (input.length() - wordLength)) {
                        subString = input.substring(j, j + wordLength);
                        wordMapClone = (j == i) ? new HashMap<>(wordsMap) : wordMapClone;
                        if (wordMapClone.containsKey(subString)) {
                            int value = wordMapClone.get(subString) - 1;
                            if (value == 0)
                                wordMapClone.remove(subString);
                            else
                                wordMapClone.put(subString, value);

                            if (wordMapClone.isEmpty()) {
                                result.add(i);
                                break;
                            }
                            j += wordLength;
                        } else break;
                    }
                }
            }
        };
    }

    public static List<Integer> findSubstring(String s, String[] words) {
        List<Integer> res = new ArrayList<>();
        Map<String, Integer> map = new TreeMap<>();
        int c = words[0].length();
        int n = c * words.length;
        for (String w : words) {
            map.put(w, map.getOrDefault(w, 0) + 1);
        }
        for (int i = 0; i <= s.length() - n; i++) {
            String str = s.substring(i, Math.min(s.length(), i + n));
            Map<String, Integer> map1 = new TreeMap<>(map);
            for (int j = 0; j <= str.length()-c; j += c) {
                String slice = str.substring(j, j + c);
                if (map.containsKey(slice)) {
                    int value = map1.get(slice)-1;
                    if (value == 0)
                        map1.remove(slice);
                    else
                        map1.put(slice,value);
                    if (map1.isEmpty()){
                        res.add(i);
                        break;
                    }
                 //   map1.put(slice, map1.getOrDefault(slice, 0) + 1);
                } else {
                    break;
                }
            }
//            if (isEqMap(map, map1)) {
//                res.add(i);
//            }
        }
        return res;
    }

    private static boolean isEqMap(Map<String, Integer> map, Map<String, Integer> map1) {
        if (map.size() != map1.size()) return false;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String k1 = entry.getKey();
            Integer v1 = entry.getValue();
            Integer v2 = map1.get(k1);
            if (!v1.equals(v2)) return false;
            map1.remove(k1);
        }
        return map1.isEmpty();
    }

    public static void main(String[] args) {
        String s = "barfoothefoobarman";
        String[] words = {"foo", "bar"};
        var res = findSubstring(s, words);
        System.out.println(res);
    }
}
