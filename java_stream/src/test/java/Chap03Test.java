import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;

import	java.math.BigDecimal;
/**
 * @author zhaogy
 */
public class Chap03Test {

    @Test
    public  void test3_2(){
      String names=  Stream.of("Mal", "Wash", "Kaylee", "Inara", "Jany", "string").collect(Collectors.joining(","));

    }

    @Test
    public  void test3_3(){
        String[] munsters=  {"Mal", "Wash", "Kaylee", "Inara", "Jany", "string"};
       String names= Arrays.stream(munsters).collect(Collectors.joining(","));
    }

    @Test
    public  void test3_4(){
        //根据当前值生成流的下一个值
        List<BigDecimal> nums = Stream.iterate(BigDecimal.TEN, n -> n.add(BigDecimal.ONE))
            .limit(10).collect(Collectors.toList());
    }

    @Test
    public  void test3_5(){
        //根据当前值生成流的下一个值
        List<BigDecimal> nums = Stream.iterate(BigDecimal.TEN, n -> n.add(BigDecimal.ONE))
            .limit(10).collect(Collectors.toList());
    }
}
