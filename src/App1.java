import java.io.*;
import java.util.*;
import java.util.List;

import static java.util.stream.IntStream.range;

public class App1 {
    static char[] chars = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
    };


    public static void main(String[] args) throws IOException {
        var ran = new Random();
        int[] arr =range(0,50).map(i->ran.nextInt(50)).toArray();
        Arrays.sort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
