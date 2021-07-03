package com.java.domain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Person {
    @Value("kevin")
    private String name;
    @Value("${男}")
    private String sex;
    @Value("20")
    private int age;

    public Person(){
        System.out.println("Person对象被创建了");
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                '}';
    }
}
