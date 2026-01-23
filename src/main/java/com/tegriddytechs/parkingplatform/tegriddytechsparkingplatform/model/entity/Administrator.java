package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Administrator extends User {
    private ArrayList<ParkingLot> parkingLots;

    public Administrator(int id,int adminId, String name, String userName, String password) {
        super(id, name, userName, password, UserRole.ADMIN);
    }

    public Administrator(int id, String name, String userName, String password, ArrayList<ParkingLot> parkingLots) {
        super(id, name, userName, password, UserRole.ADMIN);
        this.parkingLots = parkingLots;
    }
    @Override
    public boolean verifyLogin(String userName, String password) {
        boolean admited = false;
        if (this.getUserName().equalsIgnoreCase(userName)
                && this.getPassword().equals(password)) {
            admited = true;
            this.setUserRole(UserRole.ADMIN);
        }
        return admited;
    }

    public ArrayList<ParkingLot> getParkingLots() {
        return parkingLots;
    }

    public void setParkingLots(ArrayList<ParkingLot> parkingLots) {
        this.parkingLots = parkingLots;
    }

    @Override
    public String toString() {
        return "Administrator{" +
                ", parkingLots=" + parkingLots +
                '}';
    }
}
