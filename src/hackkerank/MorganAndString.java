package hackkerank;

public class MorganAndString {
    public static String morganAndString(String a, String b) {

        StringBuilder res = new StringBuilder();
        StringBuilder s1 = new StringBuilder(a);
        s1.append('z');
        StringBuilder s2 = new StringBuilder(b);
        s2.append('z');
        int m = s1.length(), n = s2.length(), l = m + n;
        int i = 0, j = 0;
        while (i<s1.length()&&j<s2.length()){

            if (s1.charAt(i) < s2.charAt(j))
                res.append(s1.charAt(i++));
            else if (s1.charAt(i) > s2.charAt(j))
                res.append(s2.charAt(j++));
            else {
                if (s1.charAt(i) == 'z') {
                    i++;
                    j++;
                    continue;
                }
                int startingI = i;
                int startingJ = j;
                while (s1.charAt(i) == s2.charAt(j)) {
                    i++;
                    j++;
                    if (i >= s1.length() && j >= s2.length()) {
                        i = startingI;
                        j = startingJ;
                        break;
                    } else if (i >= s1.length()) {
                        char prev = s2.charAt(startingJ);
                        while (s2.charAt(startingJ) <= prev) {
                            res.append(s2.charAt(startingJ));
                            prev = s2.charAt(startingJ);
                            startingI++;
                        }
                        i = startingI;
                        j = startingJ;
                    } else if (j >= s2.length()) {
                        char prev = s1.charAt(startingI);
                        while (s1.charAt(startingI) <= prev) {
                            res.append(s1.charAt(startingI));
                            prev = s1.charAt(startingI);
                            startingI++;
                        }
                        i = startingI;
                        j = startingJ;
                    }
                }

                if (s1.charAt(i) <= s2.charAt(j)) {
                    char prev = s1.charAt(startingI);
                    while (s1.charAt(startingI) <= prev) {
                        res.append(s1.charAt(startingI));
                        prev = s1.charAt(startingI);
                        startingI++;
                    }
                    i = startingI;
                    j = startingJ;
                }
                if (s1.charAt(i) > s2.charAt(j)) {
                    char prev = s2.charAt(startingJ);
                    while (s2.charAt(startingJ) <= prev) {
                        res.append(s2.charAt(startingJ));
                        prev = s2.charAt(startingJ);
                        startingJ++;
                    }
                    i = startingI;
                    j = startingJ;
                }
            }
        }
        while (i<s1.length()){
            res.append(s1.charAt(i++));
        }
        while (j<s2.length()){
            res.append(s2.charAt(j++));
        }
        return res.toString();
    }

    public static void main(String[] args) {
        String a = "ABACABA";
        String b = "ABACABA";
        String ex = "DAJACKNIEL";
        String res = morganAndString(a, b);
        System.out.println(res);
        System.out.println(res.equals(ex));
    }
}
