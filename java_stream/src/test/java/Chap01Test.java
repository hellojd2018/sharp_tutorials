import com.hellojd.chap01.Person;
import java.util.ArrayList;
import static  org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;
import	java.util.Comparator;
/**
 * @author zhaogy
 */
public class Chap01Test {

    /**
     * 字符串转实例
     */
    @Test
    public void test1_15(){
        List<String> names=  Arrays.asList("tom","jack","jim");
        List<Person> people = names.stream().map(name -> new Person(name))
            .collect(Collectors.toList());
        assertEquals(people.size(), names.size());
        List<Person> people2 = names.stream().map(Person::new)
            .collect(Collectors.toList());
        assertEquals(people2.size(), names.size());
    }

    @Test
    public void test1_17(){
        Person before = new Person("zhao");
        List<Person> people = Stream.of(before).map(Person::new).collect(Collectors.toList());
        Person after = people.get(0);
        assertFalse(after==before);
        assertEquals(after,before);
    }

    /**
     * 可变构造函数
     */
    @Test
    public void test1_20(){
        List<String> names=  Arrays.asList("tom Bruse","jack ma","jim ton");
        List<Person> peopes = names.stream().map(name -> name.split(" ")).map(Person::new)
            .collect(Collectors.toList());
        assertEquals(peopes.get(0).getName(),"tom Bruse");
    }
@Test
    public void test1_21(){
        List<String> names=  Arrays.asList("tom Bruse","jack ma","jim ton");
        Person[] peopes = names.stream().map(name -> name.split(" ")).map(Person::new)
            .toArray(Person[]::new);
        assertEquals(peopes.length,3);
    }
    @Test
    public void test1_28(){
        List<String> bonds = Arrays.asList("Connery", "Lazenby", "Moore", "Dalton","Brosnan","Craig");
        //自然排序
        String sorted = bonds.stream().sorted(Comparator.naturalOrder())
            .collect(Collectors.joining(","));
        //Brosnan,Connery,Craig,Dalton,Lazenby,Moore
        //反向排序
        sorted = bonds.stream().sorted(Comparator.reverseOrder())
            .collect(Collectors.joining(","));
        //Moore,Lazenby,Dalton,Craig,Connery,Brosnan
         //按小写名称排序
        sorted = bonds.stream().sorted(Comparator.comparing(String::toLowerCase))
            .collect(Collectors.joining(","));
        //Brosnan,Connery,Craig,Dalton,Lazenby,Moore
    //按名称长度排序
        sorted = bonds.stream().sorted(Comparator.comparingInt(String::length))
            .collect(Collectors.joining(","));
        //Moore,Craig,Dalton,Connery,Lazenby,Brosnan
        //按名称长度排序，再按字典序排序
        sorted = bonds.stream().sorted(Comparator.comparingInt(String::length)
            .thenComparing(Comparator.naturalOrder()))
            .collect(Collectors.joining(","));
    }
}
