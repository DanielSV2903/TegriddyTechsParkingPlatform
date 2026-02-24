package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.repositories.ParkingLotXmlRepository;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingLot;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ParkingLotData extends ParkingLotXmlRepository {

    private final ArrayList<ParkingLot> parkingLots = new ArrayList<>();

    public ParkingLotData() throws IOException, JDOMException {
        super();
        reload();
    }

    public void reload() {
        parkingLots.clear();
        parkingLots.addAll(super.findAll()); // XML -> cache
    }

    public ArrayList<ParkingLot> getAllParkingLots() {
        return parkingLots;
    }

    public ParkingLot findParkingLotById(int id) {
        return findById(id).orElse(null);
    }

    private Optional<ParkingLot> findInCacheById(int id) {
        for (ParkingLot p : parkingLots) {
            if (p != null && p.getParkingLotId() == id) return Optional.of(p);
        }
        return Optional.empty();
    }

    @Override
    public List<ParkingLot> findAll() {
        return parkingLots; // cache
    }

    @Override
    public Optional<ParkingLot> findById(int id) {
        return findInCacheById(id); // cache
    }

    @Override
    public void insert(ParkingLot parkingLot) throws IOException {
        if (parkingLot == null) throw new IllegalArgumentException("parkingLot cannot be null");
        if (!findInCacheById(parkingLot.getParkingLotId()).isPresent()){
            super.insert(parkingLot);
            parkingLots.add(parkingLot);
        } else {
            throw new IllegalArgumentException("ParkingLot with ID " + parkingLot.getParkingLotId() + " already exists.");
        }
    }

    @Override
    public void update(ParkingLot parkingLot) throws IOException {
        if (parkingLot == null) throw new IllegalArgumentException("parkingLot cannot be null");

        super.update(parkingLot); // XML primero

        // cache después
        findInCacheById(parkingLot.getParkingLotId()).ifPresent(parkingLots::remove);
        parkingLots.add(parkingLot);
    }

    @Override
    public boolean deleteById(int id) throws IOException {
        boolean deleted = super.deleteById(id); // XML primero
        if (!deleted) return false;

        findInCacheById(id).ifPresent(parkingLots::remove);
        return true;
    }

    @Override
    public boolean delete(ParkingLot parkingLot) throws IOException {
        if (parkingLot == null) return false;
        return deleteById(parkingLot.getParkingLotId());
    }

    public boolean registerParkingLot(ParkingLot parkingLot) throws IOException {
        if (parkingLot == null) throw new IllegalArgumentException("parkingLot cannot be null");
        if (findParkingLotById(parkingLot.getParkingLotId()) != null) return false;
        if (findInCacheById(parkingLot.getParkingLotId()).isPresent()) return false;
        insert(parkingLot);
        return true;
    }

    public boolean deleteParkingLot(ParkingLot parkingLot) throws IOException {
        return delete(parkingLot);
    }

    public void editParkingLot(ParkingLot parkingLot) throws IOException {
        // En este esquema, editar = update (mismo ID)
        update(parkingLot);
    }

    public int getNextId() {
        int id = 0;
        for (ParkingLot p : parkingLots) {
            if (p.getParkingLotId() > id)
                id = p.getParkingLotId();
        }
        return id + 1;
    }
}