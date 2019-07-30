package com.hellojd.chap01;

/**
 * @author zhaogy
 */
@FunctionalInterface
public interface MyInterface {
int myMethod();
//int myOtherMethod(); 必须注释掉，否则不能声明FunctionalInterface


default String sayHello(){return "hello world";}

static void myStaticMethod(){
    System.out.println("I'M a static method in an interface");
}
}
