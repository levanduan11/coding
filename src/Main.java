import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws Throwable {
        List<Integer> list = new List<>(Arrays.asList(1, 2, 3, 4));
        list.map(integer -> integer * 2)
                .forEach(System.out::println);
    }
}

class List<E> extends ArrayList<E> {
    public List(java.util.List<E> list) {
        super(list);
    }

    public List() {
    }

    @SuppressWarnings("unchecked")
    public <R> List<R> map(Function<? super E, ? extends R> fn) {
        Object[] elementData = toArray();
        List<R> res = new List<>();
        for (Object elementDatum : elementData) {
            E e = (E) elementDatum;
            R r = fn.apply(e);
            res.add(r);
        }
        return res;
    }
}