package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Administrator extends User {
    private ArrayList<ParkingLot> parkingLots;

    public Administrator() {
    }

    public Administrator(int id, String name, String userName, String password) {
        super(id, name, userName, password, UserRole.ADMIN);
    }

    public Administrator(int id, String name, String userName, String password, ArrayList<ParkingLot> parkingLots) {
        super(id, name, userName, password, UserRole.ADMIN);
        this.parkingLots = parkingLots;
    }

    public ArrayList<ParkingLot> getParkingLots() {
        return parkingLots;
    }

    public void setParkingLots(ArrayList<ParkingLot> parkingLots) {
        this.parkingLots = parkingLots;
    }

}
