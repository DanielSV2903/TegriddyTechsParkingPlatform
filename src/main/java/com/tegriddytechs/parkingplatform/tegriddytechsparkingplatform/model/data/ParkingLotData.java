package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingLot;

import java.util.ArrayList;

public class ParkingLotData {
    private ArrayList <ParkingLot> parkingLots;
    private transient PersistenceManager persistenceManager;

    public ParkingLotData() {
        this.parkingLots = new ArrayList<>();
        this.persistenceManager = new PersistenceManager();
    }
    public void save() {
        try {
            persistenceManager.saveParkingLotData(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public ArrayList <ParkingLot> getAllParkingLots() {
        return parkingLots;
    }
    public ParkingLot findParkingLotById(String id) {
        ParkingLot parkingLot=null;
        for (ParkingLot actualParkingLot : parkingLots) {
            if (actualParkingLot.getParkingLotId().equals(id)) {
                parkingLot= actualParkingLot;
            }
        }
        return parkingLot;
    }
    public void registerParkingLot(ParkingLot parkingLot) {
        this.parkingLots.add(parkingLot);
        save();
    }
    public void deleteParkingLot(ParkingLot parkingLot) {
        this.parkingLots.remove(parkingLot);
        save();
    }

    public void editParkingLot(ParkingLot parkingLot) {
        ParkingLot existingParkingLot = findParkingLotById(parkingLot.getParkingLotId());
        if (existingParkingLot != null) {
            existingParkingLot.setName(parkingLot.getName());
            existingParkingLot.setActive(parkingLot.isActive());
            existingParkingLot.setSpaces(parkingLot.getSpaces());
            parkingLots.remove(existingParkingLot);
            parkingLots.add(parkingLot);
            save();
        }
    }

}
