package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.ParkingLotData;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.PersistenceManager;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.OperationResult;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingLot;

import java.util.ArrayList;

public class ParkingLotController {
    private ParkingLotData parkingLotData;

    public ParkingLotController() {
        this.parkingLotData = new PersistenceManager().loadParkingLotData();
    }

    public ArrayList<ParkingLot> getAllParkingLots() {
        return parkingLotData.getAllParkingLots();
    }

    public ParkingLot findParkingLotById(String id) {
        return parkingLotData.findParkingLotById(id);
    }

    //Registers a new parking lot if it does not exist on the data
    public OperationResult registerParkingLot(ParkingLot parkingLot) {
        if (parkingLotData.findParkingLotById(parkingLot.getParkingLotId()) != null) {
            return OperationResult.failure("Parking lot already exists");
        }
        parkingLotData.registerParkingLot(parkingLot);
        return OperationResult.success("Parking lot registered successfully");
    }
    //Deletes parking lot by ID if exists on the data
    public OperationResult deleteParkingLot(ParkingLot parkingLot) {
        if (parkingLotData.findParkingLotById(parkingLot.getParkingLotId()) == null) {
            return OperationResult.failure("Parking lot not found");
        }
        parkingLotData.deleteParkingLot(parkingLot);
        return OperationResult.success("Parking lot removed successfully");
    }

    //A parking lot can be edited but not its ID
    public OperationResult editParkingLot(ParkingLot parkingLot) {
        ParkingLot existingParkingLot = parkingLotData.findParkingLotById(parkingLot.getParkingLotId());
        if (existingParkingLot != null) {
            existingParkingLot.setName(parkingLot.getName());
            existingParkingLot.setActive(parkingLot.isActive());
            parkingLotData.editParkingLot(existingParkingLot);
            return OperationResult.success("Parking lot updated successfully");
        } else {
            return OperationResult.failure("Parking lot not found");
        }
    }

}
