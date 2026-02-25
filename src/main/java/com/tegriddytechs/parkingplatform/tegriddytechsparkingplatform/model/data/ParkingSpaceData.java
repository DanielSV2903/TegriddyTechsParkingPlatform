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
        if (lot == null) return null;
        ParkingSpace parkingSpace = null;
        for (ParkingSpace actualParkingSpace : parkingSpaces) {
            if (actualParkingSpace == null) continue;
            boolean sameNumber = actualParkingSpace.getSpaceNumber() == id;
            ParkingLot actualLot = actualParkingSpace.getParkingLot();
            boolean sameLot = actualLot != null && actualLot.getParkingLotId() == lot.getParkingLotId();
            if (sameNumber && sameLot) {
                parkingSpace = actualParkingSpace;
                break; // encontrado
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
        reload();
    }

    @Override
    public void update(ParkingSpace parkingSpace) throws IOException {
        super.update(parkingSpace);
        reload();
    }

    @Override
    public boolean deleteById(int id) throws IOException {
        parkingSpaces.removeIf(parkingSpace -> parkingSpace.getSpaceNumber() == id);
        return super.deleteById(id);
    }

    @Override
    public boolean delete(ParkingSpace parkingSpace) throws IOException {
        parkingSpaces.remove(parkingSpace);
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

    public void deleteParkingSpaces(ParkingSpace[] spaces) throws IOException {
        for (ParkingSpace space: spaces) {
                delete(space);
        }
    }
}
