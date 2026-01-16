package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity;

import java.util.Objects;

public class VehicleType {
    private int id;
    private String description;
    private byte amountOfTyres;
    private double fee;
    private SpaceType spaceType;

    public VehicleType(int id, String description, byte amountOfTyres, double fee, SpaceType spaceType) {
        this.id = id;
        this.description = description;
        this.amountOfTyres = amountOfTyres;
        this.fee = fee;
        this.spaceType = spaceType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte getAmountOfTyres() {
        return amountOfTyres;
    }

    public void setAmountOfTyres(byte amountOfTyres) {
        this.amountOfTyres = amountOfTyres;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public SpaceType getSpaceType() {
        return spaceType;
    }

    public void setSpaceType(SpaceType spaceType) {
        this.spaceType = spaceType;
    }

    @Override
    public String toString() {
        return "VehicleType{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", amountOfTyres=" + amountOfTyres +
                ", fee=" + fee +
                ", spaceType=" + spaceType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VehicleType that = (VehicleType) o;
        return id == that.id && spaceType == that.spaceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, spaceType);
    }
}
