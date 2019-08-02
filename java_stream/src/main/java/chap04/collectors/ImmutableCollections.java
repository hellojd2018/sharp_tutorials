package chap04.collectors;

import java.util.*;

import static java.util.stream.Collectors.*;

/**
 * 4.8创建不可变集合
 */
public class ImmutableCollections {
    public Map<String, Integer> map = Collections.unmodifiableMap(
            new HashMap<String, Integer>() {{
                put("have", 1);
                put("the", 2);
                put("high", 3);
                put("ground", 4);
            }});

    @SafeVarargs
    public final <T> List<T> createImmutableList(T... elements) {
        //终止器，对生成的集合进行包装
        return Arrays.stream(elements)
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    @SafeVarargs
    public final <T> List<T> createImmutableListJava7(T... elements) {
        return Collections.unmodifiableList(Arrays.asList(elements));
    }

    @SafeVarargs
    public final <T> Set<T> createImmutableSet(T... elements) {
        return Arrays.stream(elements)
                .collect(collectingAndThen(toSet(), Collections::unmodifiableSet));
    }

    @SafeVarargs
    public final <T> Set<T> createImmutableSetJava7(T... elements) {
        return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(elements)));
    }


}
