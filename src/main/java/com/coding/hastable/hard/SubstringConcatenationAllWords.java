package com.coding.hastable.hard;

import java.util.*;

public class SubstringConcatenationAllWords {

    static List<Integer>result;
    static List<Integer>test;

    static List<Integer>integerList(){
        return new AbstractList<Integer>() {
            @Override
            public Integer get(int index) {
                if (test==null){
                    init();
                }
                return test.get(index);
            }

            @Override
            public int size() {
                if (test==null){
                    init();
                }
                return test.size();
            }
            public void init(){
                test=new ArrayList<>();
                test.add(1);
                test.add(1);
                test.add(1);
                test.add(1);
            }
        };
    }
    public static List<Integer> findSubstring(String input, String[] words) {
        return new AbstractList<Integer>(){
            @Override
            public Integer get(int index) {
                if (result==null){
                    init();
                }
                return result.get(index);
            }

            @Override
            public int size() {
                if (result==null){
                    init();
                }
                return result.size();
            }

            public void init(){
                Map<String,Integer>wordsMap=new HashMap<>();
                for (String word:words) {
                    wordsMap.put(word,wordsMap.getOrDefault(word,0)+1);
                }
                result=new ArrayList<>();
                int wordLength=words[0].length();
                String subString=null;
                Map<String,Integer>wordMapClone=null;
                int j=0;
                for (int i = 0; i <=(input.length()-wordLength); i++) {
                    j=i;
                    while (j<=(input.length()-wordLength)){
                        subString=input.substring(j,j+wordLength);
                        wordMapClone=(j==i)?new HashMap<>(wordsMap):wordMapClone;
                        if (wordMapClone.containsKey(subString)){
                            int value=wordMapClone.get(subString)-1;
                            if (value==0){
                                wordMapClone.remove(subString);
                            }else {
                                wordMapClone.put(subString,value);
                            }
                            if (wordMapClone.isEmpty()){
                                result.add(i);
                                break;
                            }
                            j+=wordLength;
                        }else break;
                    }
                }
            }
        };
    }

    public static List<Integer> findSubstring1(String s, String[] words) {
        List<Integer> result = new ArrayList<>();
        if (s == null || s.length() == 0 || words == null || words.length == 0) {
            return result;
        }

        int wordLength = words[0].length();
        int wordCount = words.length;
        int totalLength = wordLength * wordCount;

        Map<String, Integer> wordFreq = new HashMap<>();
        for (String word : words) {
            wordFreq.put(word, wordFreq.getOrDefault(word, 0) + 1);
        }

        for (int i = 0; i <= s.length() - totalLength; i++) {
            Map<String, Integer> seen = new HashMap<>();
            for (int j = i; j < i + totalLength; j += wordLength) {
                String currentWord = s.substring(j, j + wordLength);
                if (wordFreq.containsKey(currentWord)) {
                    seen.put(currentWord, seen.getOrDefault(currentWord, 0) + 1);

                    if (seen.get(currentWord) > wordFreq.get(currentWord)) {
                       break;
                    }
                    if (i + totalLength == j + wordLength) {
                        result.add(i);
                    }
                } else {
                    break;
                }
            }
        }
        return result;
    }

    public static void print(StringBuilder sb, String[] strings, int n, Set<String> set, String r) {
        if (n == 0) {
            if (isSame(sb.toString(), r)) {
                set.add(sb.toString());
            }
            return;
        }
        for (String string : strings) {
            sb.append(string);
            print(sb, strings, n - 1, set, r);
            sb.delete(sb.length() - strings[strings.length - n].length(), sb.length());
        }
    }

    public static boolean isSame(String a, String b) {
        if (a.length() != b.length()) {
            return false;
        }
        char[] chars = a.toCharArray();
        char[] chars2 = b.toCharArray();
        Arrays.sort(chars);
        Arrays.sort(chars2);
        for (int i = 0; i < a.length(); i++) {
            if (chars[i] != chars2[i]) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        String s = "barfoothefoobarman";
        String[] words = {"foo", "bar"};



    }
}
