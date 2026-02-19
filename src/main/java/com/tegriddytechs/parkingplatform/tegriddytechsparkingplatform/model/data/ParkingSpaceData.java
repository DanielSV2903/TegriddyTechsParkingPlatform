package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.repositories.ParkingSpaceXmlRepository;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingLot;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingSpace;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ParkingSpaceData extends ParkingSpaceXmlRepository {
    private ArrayList <ParkingSpace> parkingSpaces;

    public ParkingSpaceData() throws IOException, JDOMException {
        super();
        parkingSpaces = new ArrayList<>();
        reload();
    }

    public void reload() {
        parkingSpaces.clear();
        parkingSpaces.addAll(super.findAll()); // XML -> cache
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

    @Override
    public List<ParkingSpace> findAll() {
        return super.findAll();
    }

    @Override
    public Optional<ParkingSpace> findById(int id) {
        return super.findById(id);
    }

    @Override
    public void insert(ParkingSpace parkingSpace) throws IOException {
        super.insert(parkingSpace);
    }

    @Override
    public void update(ParkingSpace parkingSpace) throws IOException {
        super.update(parkingSpace);
    }

    @Override
    public boolean deleteById(int id) throws IOException {
        return super.deleteById(id);
    }

    @Override
    public boolean delete(ParkingSpace parkingSpace) throws IOException {
        return super.delete(parkingSpace);
    }

    public void registerParkingSpace(ParkingSpace parkingSpace) throws IOException {
        insert(parkingSpace);
    }

    public void deleteParkingSpace(ParkingSpace parkingSpace) throws IOException {
        delete(parkingSpace);
    }

    public void editParkingSpace(ParkingSpace parkingSpace) throws IOException {
            update(parkingSpace);
    }


    public List<ParkingSpace> getAllParkingSpacesByLot(int lotId) {
        List<ParkingSpace> spaces = new ArrayList<>();
        for (ParkingSpace parkingSpace : parkingSpaces) {
            if (parkingSpace.getParkingLot().getParkingLotId() == lotId) {
                spaces.add(parkingSpace);
            }
        }
        return spaces;
    }
}
