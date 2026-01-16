package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity;

import java.util.Objects;

public class ParkingSpace {
    private int spaceNumber;
    private SpaceType spaceType;
    private boolean preferential;
    private boolean state;
    private ParkingLot parkingLot;

    public ParkingSpace() {
    }

    public ParkingSpace(int spaceNumber, SpaceType spaceType, boolean preferential, boolean state) {
        this.spaceNumber = spaceNumber;
        this.spaceType = spaceType;
        this.preferential = preferential;
        this.state = state;
    }

    public int getSpaceNumber() {
        return spaceNumber;
    }

    public void setSpaceNumber(int spaceNumber) {
        this.spaceNumber = spaceNumber;
    }

    public SpaceType getSpaceType() {
        return spaceType;
    }

    public void setSpaceType(SpaceType spaceType) {
        this.spaceType = spaceType;
    }

    public boolean isPreferential() {
        return preferential;
    }

    public void setPreferential(boolean preferential) {
        this.preferential = preferential;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSpace that = (ParkingSpace) o;
        return spaceNumber == that.spaceNumber && Objects.equals(spaceType, that.spaceType) && Objects.equals(parkingLot, that.parkingLot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(spaceNumber, spaceType, parkingLot);
    }
}
