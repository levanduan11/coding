package hard;

public class ValidNumber {
    public static boolean isNumber(String s) {
        char[] chars = s.toCharArray();
        int stateDot = 0, stateE = 0;
        int n = chars.length;
        for (int i = 0; i < n; i++) {
            char ch = chars[i];
            if (isInValid(ch)) return false;
            if (isNumber(ch)) continue;
            if (isDot(ch)) {
                stateDot++;
                if (stateDot > 1 || chars.length == 1) return false;
                int prev = i - 1;
                int next = i + 1;
                if (prev >= 0) {
                    if (!(isNumber(chars[prev]) || isOption(chars[prev]))) return false;
                }
                if (next < n) {
                    if (i == 0) {
                        if (!isNumber(chars[next])) return false;
                    }
                    if (!(isNumber(chars[next]) || isE(chars[next]))) return false;
                }
            } else if (isE(ch)) {
                stateE++;
                if (stateE > 1) return false;
                if (s.indexOf('.') >= i || n == 1) return false;
                int prev = i - 1;
                int next = i + 1;
                if (prev >= 0) {
                    if (!(isNumber(chars[prev]) || isDot(chars[prev]))) return false;
                } else return false;
                if (next < n) {
                    if (isOption(chars[next])) {
                        int nextNext = next + 1;
                        if (nextNext >= n || !isNumber(chars[nextNext])) return false;
                    } else {
                        if (!isNumber(chars[next])) return false;
                    }
                } else return false;

            } else if (isOption(ch)) {
                int next = i + 1;
                int prev = i - 1;
                if (n == 1) return false;

                if (prev >= 0) {
                    if (!isE(chars[prev])) return false;
                } else {
                    if (s.indexOf(ch) != 0) return false;
                }
                if (next < n) {
                    if (isOption(chars[next])) return false;
                    else if (isDot(chars[next])) {
                        int nextNext = next + 1;
                        if (nextNext < n) {
                            if (!isNumber(chars[nextNext])) return false;
                        } else return false;
                    } else if (!isNumber(chars[next])) return false;
                }

            }
        }
        return true;
    }

    static boolean isNumber(char n) {
        return n >= '0' && n <= '9';
    }

    static boolean isDot(char n) {
        return n == '.';
    }

    static boolean isE(char n) {
        return n == 'e' || n == 'E';
    }

    static boolean isOption(char n) {
        return n == '-' || n == '+';
    }

    static boolean isInValid(char ch) {
        return !(isNumber(ch) || isDot(ch) || isE(ch) || isOption(ch));
    }

    static boolean isNumber2(String s) {
        s = s.trim();
        if (s.isEmpty()) return false;
        boolean seenNum = false, seenDot = false, seenE = false;
        for (int i = 0; i < s.length(); i++) {
            switch (s.charAt(i)) {
                case '.' -> {
                    if (seenDot || seenE) return false;
                    seenDot = true;
                }
                case 'e', 'E' -> {
                    if (seenE || !seenNum) return false;
                    seenE = true;
                    seenNum = false;
                }
                case '+', '-' -> {
                    if (i > 0 && s.charAt(i - 1) != 'e' && s.charAt(i - 1) != 'E') return false;
                    seenNum = false;
                }
                default -> {
                    if (!Character.isDigit(s.charAt(i))) return false;
                    seenNum = true;
                }
            }
        }
        return seenNum;
    }

    public static void main(String[] args) {
        String input = "99e2.5";
        System.out.println(isNumber2(input));

    }
}
