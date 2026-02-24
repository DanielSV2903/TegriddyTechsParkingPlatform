package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Customer extends Person {
    List<Vehicle> vehicles = new ArrayList<>();

    public Customer() {
        super();
    }

    public Customer(int id, String name, boolean disability, int age, Vehicle vehicle) {
        super(id, name, disability, age);
        setVehicle(vehicle);
    }
    public Vehicle getVehicle() {
        return vehicles.isEmpty() ? null : vehicles.get(0);
    }
    public List<Vehicle> getVehicles() {
        return vehicles;
    }
    public void setVehicle(Vehicle vehicle) {
        if (!vehicles.contains(vehicle))
            this.vehicles.add(vehicle);
    }



    @Override
    public String toString() {
        return getId() + " - " + getName();
    }

}