package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingLot;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingSpace;

import java.util.ArrayList;

public class ParkingSpaceData {
    private ArrayList <ParkingSpace> parkingSpaces;

    public ParkingSpaceData() {
        this.parkingSpaces = new ArrayList<>();
    }

    public ArrayList <ParkingSpace> getAllParkingSpaces() {
        return parkingSpaces;
    }

    public ParkingSpace findParkingSpaceByNumber(int id, ParkingLot lot) {
        ParkingSpace parkingSpace=null;
        for (ParkingSpace actualParkingSpace : parkingSpaces) {
            if (actualParkingSpace.getSpaceNumber() == id && actualParkingSpace.getParkingLot().equals(lot)) {
                parkingSpace= actualParkingSpace;
            }
        }
        return parkingSpace;
    }

    public void registerParkingSpace(ParkingSpace parkingSpace) {
        this.parkingSpaces.add(parkingSpace);
    }

    public void deleteParkingSpace(ParkingSpace parkingSpace) {
        this.parkingSpaces.remove(parkingSpace);
    }

    public void editParkingSpace(ParkingSpace parkingSpace) {
        ParkingSpace existingParkingSpace = findParkingSpaceByNumber(parkingSpace.getSpaceNumber(),parkingSpace.getParkingLot());
        if (existingParkingSpace != null) {
            existingParkingSpace.setSpaceNumber(parkingSpace.getSpaceNumber());
            existingParkingSpace.setPreferential(parkingSpace.isPreferential());
            existingParkingSpace.setState(parkingSpace.isState());
            parkingSpaces.remove(existingParkingSpace);
            parkingSpaces.add(parkingSpace);
        }else {
            //TODO: throw exception
        }
    }


}
