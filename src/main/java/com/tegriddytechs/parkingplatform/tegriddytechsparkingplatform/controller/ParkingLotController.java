package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.ParkingLotData;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.OperationResult;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingLot;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingSpace;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.ArrayList;

public class ParkingLotController {
    private final ParkingLotData parkingLotData;

    public ParkingLotController() throws IOException, JDOMException {
        this.parkingLotData = new ParkingLotData();
    }

    public ArrayList<ParkingLot> getAllParkingLots() {
        return parkingLotData.getAllParkingLots();
    }

    public ParkingLot findParkingLotById(int id) {
        return parkingLotData.findParkingLotById(id);
    }

    // Registers a new parking lot if it does not exist on the data
    public OperationResult registerParkingLot(ParkingLot parkingLot) throws IOException {
        if (parkingLot == null) return OperationResult.failure("Parking lot cannot be null");

        boolean created = parkingLotData.registerParkingLot(parkingLot);
        if (!created) {
            return OperationResult.failure("Parking lot already exists");
        }
        return OperationResult.success("Parking lot registered successfully");
    }

    // Deletes parking lot by ID if exists on the data
    public OperationResult deleteParkingLot(ParkingLot parkingLot) throws IOException {
        if (parkingLot == null) return OperationResult.failure("Parking lot cannot be null");
        if (hasVehiclesParked(parkingLot.getSpaces()))
            return OperationResult.failure("Parking lot cannot be deleted because it has vehicles parked");

        boolean deleted = parkingLotData.deleteParkingLot(parkingLot);
        if (!deleted) {
            return OperationResult.failure("Parking lot not found");
        }
        return OperationResult.success("Parking lot removed successfully");
    }

    private boolean hasVehiclesParked(ParkingSpace[] spaces) {
        for (ParkingSpace space : spaces) {
            if (space.isParked()) return true;
        }
        return false;
    }

    // A parking lot can be edited but not its ID
    public OperationResult editParkingLot(ParkingLot parkingLot) throws IOException {
        if (parkingLot == null) return OperationResult.failure("Parking lot cannot be null");

        ParkingLot existing = parkingLotData.findParkingLotById(parkingLot.getParkingLotId());
        if (existing == null) {
            return OperationResult.failure("Parking lot not found");
        }

        // update por ID
        parkingLotData.editParkingLot(parkingLot);
        return OperationResult.success("Parking lot updated successfully");
    }

    public int getNextId() {
        return parkingLotData.getNextId();
    }
}