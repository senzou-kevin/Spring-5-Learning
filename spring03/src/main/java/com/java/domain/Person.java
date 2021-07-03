package com.java.domain;

import java.util.*;

public class Person {
    private String name;
    private Integer age;
    private String[] myArrays;
    private List<String> list;
    private Set<String> set;
    private Map<String,String> map;
    private Properties pro;

    public Person(){
        System.out.println("Person对象被创建好了...");
    }
    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setMyArrays(String[] myArrays) {
        this.myArrays = myArrays;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public void setSet(Set<String> set) {
        this.set = set;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public void setPro(Properties pro) {
        this.pro = pro;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", myArrays=" + Arrays.toString(myArrays) +
                ", list=" + list +
                ", set=" + set +
                ", map=" + map +
                ", pro=" + pro +
                '}';
    }
}
