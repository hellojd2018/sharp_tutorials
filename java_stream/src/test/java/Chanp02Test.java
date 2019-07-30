import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.junit.Test;

/**
 * @author zhaogy
 */
public class Chanp02Test {

    @Test
    public void test2_3(){
        List<String> strings = Arrays.asList("this", "is", "a", "list", "of", "string");
        strings.forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        });
        //lamdba
        strings.forEach(s-> System.out.println(s));
        //方法引用
        strings.forEach(System.out::println);
    }

    @Test
    public void test2_4(){
        DoubleSupplier randomSupplier= new DoubleSupplier(){

            @Override
            public double getAsDouble() {
                return Math.random();
            };
        };
        randomSupplier=()->Math.random();
        randomSupplier=Math::random;
    }


    @Test
    public void test2_5(){
        List<String> strings = Arrays.asList("Mal", "Wash", "Kaylee", "Inara", "Jany", "string");
        Optional<String> first = strings.stream().filter(name -> name.startsWith("C")).findFirst();
        System.out.println(first);
        System.out.println(first.orElse("None"));
        System.out.println(first.orElse(String.format("No result found in %s",strings.stream().collect(
            Collectors.joining(", ")))));
    //仅当Optional为空时，才会调用Supplier接口get方法
        System.out.println(first.orElseGet(()->String.format("No result found in %s",strings.stream().collect(
            Collectors.joining(", ")))));
    }

    @Test
    public void test2_13(){
        List<String> names = Arrays.asList("Mal", "Wash", "Kaylee", "Inara", "Jany", "string");
        List<Integer> nameLengths = names.stream().map(new Function<String, Integer>() {
            @Override
            public Integer apply(String s) {
                return s.length();
            }
        }).collect(Collectors.toList());
        nameLengths=names.stream().map(s->s.length()).collect(Collectors.toList());
        nameLengths=names.stream().map(String::length).collect(Collectors.toList());


    }
}
