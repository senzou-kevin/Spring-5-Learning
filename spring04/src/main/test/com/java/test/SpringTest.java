package com.java.test;

import com.java.domain.Person;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringTest {

    public static void main(String[] args) {
        ApplicationContext ac=new AnnotationConfigApplicationContext("com.java");
        Person person = ac.getBean("person", Person.class);
        System.out.println(person);
    }
}
