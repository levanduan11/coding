package com.coding.hard;

public class ReadNCharactersGivenRead4 {
    private static int read4(char[] buf4) {
        return 1;
    }

    public static int read(char[] buf, int n) {
        boolean isEof = false;
        int charsRead = 0;
        char[] buf4 = new char[4];
        while (!isEof && charsRead < n) {
            int size = read4(buf4);
            if (size < 4)
                isEof = true;
            if (charsRead + size > n)
                size = n - charsRead;
            System.arraycopy(buf4, 0, buf, charsRead, size);
        }
        return charsRead;
    }

    public static void main(String[] args) {
        
    }
}
