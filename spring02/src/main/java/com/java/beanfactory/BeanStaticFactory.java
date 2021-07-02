package com.java.beanfactory;

import com.java.domain.Animal;

public class BeanStaticFactory {

    public static Animal getAnimal(){
        Animal animal=new Animal();
        animal.setAnimalName("doge");
        return animal;
    }
}
