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
    public void upsert(ParkingLot parkingLot) throws IOException {
        if (parkingLot == null) throw new IllegalArgumentException("parkingLot cannot be null");

        super.upsert(parkingLot); // XML primero

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

    // Métodos "de negocio" (compatibles con tu código existente)
    public boolean registerParkingLot(ParkingLot parkingLot) throws IOException {
        if (parkingLot == null) throw new IllegalArgumentException("parkingLot cannot be null");
        if (findParkingLotById(parkingLot.getParkingLotId()) != null) return false;

        upsert(parkingLot);
        return true;
    }

    public boolean deleteParkingLot(ParkingLot parkingLot) throws IOException {
        return delete(parkingLot);
    }

    public void editParkingLot(ParkingLot parkingLot) throws IOException {
        // En este esquema, editar = upsert (mismo ID)
        upsert(parkingLot);
    }
}