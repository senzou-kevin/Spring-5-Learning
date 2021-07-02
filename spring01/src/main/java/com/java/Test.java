package com.java;

public class Test {

    public static void main(String[] args) {
        //创建一个ShuttleBus对象
        ShuttleBus bus=new ShuttleBus();
        //创建一个Person对象
        Person person=new Person();
        //也可以通过构造方法传递
        //Person person=new Person(bus);
        person.setVehicle(bus);
        person.goToWork();
    }
}
