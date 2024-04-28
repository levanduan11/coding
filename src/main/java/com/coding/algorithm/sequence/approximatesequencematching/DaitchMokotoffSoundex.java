package com.coding.algorithm.sequence.approximatesequencematching;


import java.util.*;

public class DaitchMokotoffSoundex {
    private static final Map<Character, String[]> soundexmap = new HashMap<>();

    static {
        soundexmap.put('B', new String[]{"7"});
        soundexmap.put('P', new String[]{"7"});
        soundexmap.put('F', new String[]{"7"});
        soundexmap.put('V', new String[]{"7"});
        soundexmap.put('C', new String[]{"4", "5"});
        soundexmap.put('S', new String[]{"4", "5"});
        soundexmap.put('K', new String[]{"5"});
        soundexmap.put('G', new String[]{"5"});
    }

    public static Set<String> encode(String name) {
        Set<String> result = new HashSet<>();
        char[] chars = name.toUpperCase().toCharArray();
        List<String[]> possibleCodes = new ArrayList<>();

        for (char c : chars) {
            if (soundexmap.containsKey(c)) {
                possibleCodes.add(soundexmap.get(c));
            }
        }
        generateCodes(possibleCodes, new StringBuilder(), 0, result);
        return result;
    }
    private static void generateCodes(List<String[]>codes,StringBuilder currentCode,int index,Set<String> result){
        if (currentCode.length() == 6){
            result.add(currentCode.toString());
            return;
        }
        if (index >= codes.size())
            return;
        String[] possibleValues = codes.get(index);
        for (String value : possibleValues) {
            StringBuilder newCode = new StringBuilder(currentCode);
            newCode.append(value);
            generateCodes(codes, newCode, index + 1, result);
        }
    }

    public static void main(String[] args) {
        String in = "Jacobs";
        System.out.println(encode(in));
    }
}
