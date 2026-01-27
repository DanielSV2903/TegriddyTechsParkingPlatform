package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity;

public class Vehicle {
    private String plate;
    private VehicleType vehicleType;
    private VehicleStatus vehicleStatus;
    private Customer owner;
    private ParkingTicket ticket;
    private boolean disabledPermit;

    public Vehicle() {
    }

    public Vehicle(String plate, VehicleType vehicleType, VehicleStatus vehicleStatus) {
        this.plate = plate;
        this.vehicleType = vehicleType;
        this.vehicleStatus = vehicleStatus;
    }

    public Vehicle(String plate, VehicleType vehicleType, VehicleStatus vehicleStatus, Customer owner, ParkingTicket ticket) {
        this.plate = plate;
        this.vehicleType = vehicleType;
        this.vehicleStatus = vehicleStatus;
        this.owner = owner;
        this.ticket = ticket;
    }

    public String getLicensePlate() {
        return plate;
    }

    public void setLicensePlate(String licensePlate) {
        this.plate = licensePlate;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public VehicleStatus getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(VehicleStatus vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }

    public Customer getOwner() {
        return owner;
    }

    public void setOwner(Customer owner) {
        this.owner = owner;
    }

    public ParkingTicket getTicket() {
        return ticket;
    }

    public void setTicket(ParkingTicket ticket) {
        this.ticket = ticket;
    }
}
