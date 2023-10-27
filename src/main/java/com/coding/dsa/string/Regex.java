package dsa.string;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {

    static void ex1(){
        String text = "John Doe's phone number is 123-456-7890.";
        String patternString = "(?<name>\\d{3}-\\d{3}-\\d{4})";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            String phoneNumber = matcher.group("name");
            System.out.println("Phone number found: " + phoneNumber);
        } else {
            System.out.println("Phone number not found.");
        }
    }
    static void ex2(){
        String text = "apple banana cherry date";
        String patternString = "\\b(?:\\w{5})\\b";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            System.out.println("Matched word: " + matcher.group());
        }
    }
    public static void main(String[] args) {

    }

}
