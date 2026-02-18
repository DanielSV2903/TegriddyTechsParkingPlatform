package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity;

import java.util.Objects;

public class Clerk extends User{
    private ParkingLot parkingLot;

    public Clerk(int id, String name, String userName, String password) {
        super(id, name, userName, password, UserRole.CLERK);
    }

    public Clerk(int id, String name, String userName, String password, ParkingLot parkingLot) {
        super(id, name, userName, password, UserRole.CLERK);
        this.parkingLot = parkingLot;
    }


    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }
}
