package com.java.vehicle;

public class Employee {
    private Vehicle vehicle;

    public Employee(){}
    public Employee(Vehicle vehicle){
        this.vehicle=vehicle;
    }

    public void goToWork(){
        vehicle.take();
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
