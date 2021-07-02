package com.java;

import com.java.domain.Animal;
import com.java.domain.Person;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSpring {

    public static void main(String[] args) {
        //1.实例化一个ApplicationContext
        ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext.xml");
        //Person person =(Person)ac.getBean("person");
        //Person person=ac.getBean("person",Person.class);
        Animal animal = ac.getBean("animal02", Animal.class);
        System.out.println(animal);
    }
}
