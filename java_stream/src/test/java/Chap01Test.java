import com.hellojd.chap01.Person;
import java.util.ArrayList;
import static  org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;

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
}
