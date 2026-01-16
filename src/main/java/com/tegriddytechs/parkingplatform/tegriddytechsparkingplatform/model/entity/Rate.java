package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Rate {
    private int rateId;
    private VehicleType vehicleType;
    private TimeUnit timeUnit;
    private double fee;
    private boolean active;

    public Rate() {
    }

    public Rate(int rateId, VehicleType vehicleType, TimeUnit timeUnit, double fee) {
        this.rateId = rateId;
        this.vehicleType = vehicleType;
        this.timeUnit = timeUnit;
        this.fee = fee;
        this.active=true;
    }

    public int getRateId() {
        return rateId;
    }

    public void setRateId(int rateId) {
        this.rateId = rateId;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "rateId=" + rateId +
                ", vehicleType=" + vehicleType +
                ", timeUnit=" + timeUnit +
                ", fee=" + fee +
                ", active=" + active +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Rate rate = (Rate) o;
        return rateId == rate.rateId && Objects.equals(vehicleType, rate.vehicleType) && timeUnit == rate.timeUnit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rateId, vehicleType, timeUnit);
    }
}
