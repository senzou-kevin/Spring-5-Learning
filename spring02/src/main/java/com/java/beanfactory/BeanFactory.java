package com.java.beanfactory;

import com.java.domain.Animal;

public class BeanFactory {


    public Animal getAnimal(){
        Animal animal=new Animal();
        animal.setAnimalName("dog");
        return animal;
    }
}
