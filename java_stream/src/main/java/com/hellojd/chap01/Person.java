package com.hellojd.chap01;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author zhaogy
 */
public class Person {

    String name;

    public Person(String name) {
        this.name = name;
    }
    public Person(String ... names) {
        this.name = Arrays.stream(names).collect(Collectors.joining(" "));
    }
    public Person(Person p) {
        this.name = p.name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        return Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Person{" +
            "name='" + name + '\'' +
            '}';
    }
}
