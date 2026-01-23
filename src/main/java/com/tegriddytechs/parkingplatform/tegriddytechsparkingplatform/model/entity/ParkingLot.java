package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.PersistenceManager;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class ParkingLot {
    private String parkingLotId;
    private String name;
    private boolean active;
    private ArrayList<ParkingSpace> spaces;
    private Administrator administrator;

    public ParkingLot() {
    }

    public ParkingLot(String parkingLotId, String name) {
        this.parkingLotId = parkingLotId;
        this.name = name;
        this.active = true;
    }

    public ParkingLot(String parkingLotId, String name, Administrator administrator) {
        this.parkingLotId = parkingLotId;
        this.name = name;
        this.administrator = administrator;
    }

    public String getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(String parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ArrayList<ParkingSpace> getSpaces() {
        return spaces;
    }

    public void setSpaces(ArrayList<ParkingSpace> spaces) {
        this.spaces = spaces;
    }

    public Administrator getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Administrator administrator) {
        this.administrator = administrator;
    }

    @Override
    public String toString() {
        return "ParkingLot{" +
                "parkingLotId='" + parkingLotId + '\'' +
                ", name='" + name + '\'' +
                ", active=" + active +
                ", spaces=" + spaces +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ParkingLot that = (ParkingLot) o;
        return Objects.equals(parkingLotId, that.parkingLotId) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parkingLotId, name);
    }
}
