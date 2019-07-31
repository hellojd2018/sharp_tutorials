import com.hellojd.chap02.Book;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
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
    public  void test3_5(){//随机流
        //根据当前值生成流的下一个值
        List<Double> nums = Stream.generate(Math::random)
            .limit(10).collect(Collectors.toList());
    }

    @Test
    public  void test3_7(){
        //根据当前值生成流的下一个值
        List<Integer> ints = IntStream.rangeClosed(10, 15).boxed().collect(Collectors.toList());
        ints = IntStream.range(10, 15).boxed().collect(Collectors.toList());
    }

    //
    @Test
    public  void test3_11(){
        //根据当前值生成流的下一个值
        List<Integer> ints=IntStream.of(1,3,5,6,7,8).mapToObj(Integer::valueOf).collect(Collectors.toList());

        ints = IntStream.of(1, 3, 5, 6, 7, 8)
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        System.out.println(ints);
    }
    @Test
    public void test3_14(){
        String[] strings="this is an array of strings".split(" ");
        long count = Arrays.stream(strings).map(String::length).count();

        int totalLength = Arrays.stream(strings).mapToInt(String::length).sum();
        OptionalDouble average = Arrays.stream(strings).mapToInt(String::length).average();
        OptionalInt max = Arrays.stream(strings).mapToInt(String::length).max();
        OptionalInt min = Arrays.stream(strings).mapToInt(String::length).min();
    }

    @Test
    public void test3_15(){
        int sum = IntStream.rangeClosed(1, 10).reduce((x, y) -> x + y).orElse(0);
        System.out.println(sum);

        sum= IntStream.rangeClosed(1, 10).reduce((x, y) ->{
//            System.out.printf("x=%d,y=%d%n",x,y);
            return x + y;}).orElse(0);
        System.out.println(sum);
        //二元运算符
        sum= IntStream.rangeClosed(1, 10).reduce(0,Integer::sum);
        System.out.println(sum);
    }

    /**
     * 求和过程中将值翻倍
     */
    @Test
    public void test3_17(){
       int sum= IntStream.rangeClosed(1, 10).reduce(0,(x,y)->x+2*y);
        System.out.println(sum);
    }

    @Test
    public void test3_24(){
     String s=    Stream.of("this","is","a","list").collect(()->new StringBuffer(),
             (sb,str)->sb.append(str),(sb1,sb2)->sb1.append(sb2)).toString();
        System.out.println(s);

        s = Stream.of("this", "is", "a", "list").reduce("", String::concat);
        System.out.println(s);

    }


    @Test
    public void test3_28(){
        List<Book> books = Arrays.asList(new Book(1,"book1"),new Book(2,"book2"));
        HashMap<Integer,Book> bookMap=books.stream().reduce(new HashMap<Integer,Book>(),
                                (map,book)->{map.put(book.getId(),book);return map;},
            (map1,map2)->{map1.putAll(map2);return map1;});
        bookMap.forEach((k,v)->{
            System.out.println(k+":"+v);
        });
    }

    public boolean isPalindrome(String s){
        StringBuilder sb = new StringBuilder();
        for (char c:s.toCharArray()){
            if (Character.isLetterOrDigit(c)){
                sb.append(c);
            }
        }
        String forward=sb.toString().toLowerCase();
        String backward=sb.reverse().toString().toLowerCase();
        return forward.equals(backward);
    }

    public boolean isPalindromeJava8(String s){
        String forward = s.toLowerCase().codePoints().filter(Character::isLetterOrDigit)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
        String backward=new StringBuilder(forward).reverse().toString();
        return forward.equals(backward);
    }
    @Test
    public  void test1(){
        boolean result = isPalindromeJava8("abcddqdcba");
        System.out.println(result);
    }
    @Test
    public void test3_39(){
        long count= Stream.of(1,2,3,4,5,6,7,8,9).count();
        System.out.println(count);
         count = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9).collect(Collectors.counting());
        System.out.println(count);
    }

    /**
     * 3.8汇总统计
     */
    @Test
    public void test3_42(){
        DoubleSummaryStatistics stats = DoubleStream.generate(Math::random)
            .limit(1000_000).summaryStatistics();
        System.out.println(stats.toString());
    }
    @Test
    public void test3_50(){
        Optional<Integer> any = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).unordered()
            .map(this::delay).findAny();
        System.out.println("Sequential Any:"+any);

        any = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).unordered().parallel()
            .map(this::delay).findAny();
        System.out.println("Parallel Any:"+any);
    }
    public Integer delay(Integer n){
        try {
            Thread.sleep((long)Math.random()*100);
        } catch (InterruptedException e) {
        }
        return n;
    }


    //质数校验
    public boolean isPrime(int num) {
      int limit=  (int)(Math.sqrt(num)+1);
      return num==2||num>1&&IntStream.range(2,limit).noneMatch(divisor->num%divisor==0);
    }


}

