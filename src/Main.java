import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.TreeMap;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws Throwable{
        Path path=Paths.get("customer.csv");
        Files.lines(path).forEach(System.out::println);
    }
}