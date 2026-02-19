package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.ParkingSpaceData;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.OperationResult;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingLot;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingSpace;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.ArrayList;

public class ParkingSpaceController {
    private ParkingSpaceData parkingSpaceData;

    public ParkingSpaceController() throws IOException, JDOMException {
        parkingSpaceData=new ParkingSpaceData();

    }

    public ParkingSpace findParkingSpaceByNumber(int spaceNumber, ParkingLot parkingLot) {
        return parkingSpaceData.findParkingSpaceByNumber(spaceNumber, parkingLot);
    }

    public OperationResult registerParkingSpace(ParkingSpace space) throws IOException {
        if (space == null) return OperationResult.failure("Parking space cannot be null");
        if (findParkingSpaceByNumber(space.getSpaceNumber(), space.getParkingLot()) != null) {
            return OperationResult.failure("Parking space already exists");
        }
        parkingSpaceData.insert(space);
        return OperationResult.success("Parking space registered successfully");
    }

    public OperationResult editParkingSpace(ParkingSpace space) throws IOException {
        if (space == null) return OperationResult.failure("Parking space cannot be null");
        if (findParkingSpaceByNumber(space.getSpaceNumber(), space.getParkingLot()) == null) {
            return OperationResult.failure("Parking space not found");
        }
        parkingSpaceData.update(space);
        return OperationResult.success("Parking space updated successfully");
    }

    public OperationResult deleteParkingSpace(ParkingSpace existing) throws IOException {
        if (findParkingSpaceByNumber(existing.getSpaceNumber(), existing.getParkingLot()) == null)
            return OperationResult.failure("Parking space not found");
        parkingSpaceData.deleteParkingSpace(existing);
        return OperationResult.success("Parking space deleted successfully");
    }

    public ArrayList<ParkingSpace> getAllParkingSpaces() {
        return parkingSpaceData.getAllParkingSpaces();
    }
}
