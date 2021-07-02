package com.java;

/**
 * 一位公司员工每天开车去上班
 */
public class Person {

    private Vehicle vehicle;

    public Person(){}
    public Person(Vehicle vehicle){
        this.vehicle=vehicle;
    }

    public void goToWork(){
        vehicle.take();
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
