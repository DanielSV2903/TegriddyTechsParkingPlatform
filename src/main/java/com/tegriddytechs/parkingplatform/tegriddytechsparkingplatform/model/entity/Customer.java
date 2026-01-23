package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity;

import java.util.Objects;

public class Customer extends Person {
    private Vehicle vehicle;

    public Customer() {
        super();
    }

    public Customer(int id, String name, boolean disability, int age, Vehicle vehicle) {
        super(id, name, disability, age);
        this.vehicle = vehicle;
    }
    public Vehicle getVehicle() {
        return vehicle;
    }
    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "vehicle=" + vehicle +
                '}';
    }


}