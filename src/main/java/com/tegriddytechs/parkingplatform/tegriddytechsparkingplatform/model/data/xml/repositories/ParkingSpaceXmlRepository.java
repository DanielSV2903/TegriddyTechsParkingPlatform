package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.repositories;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.DatabasePaths;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.PersistenceXMLManager;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.mappers.ParkingSpaceXmlMapper;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingSpace;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ParkingSpaceXmlRepository implements DataRepository<ParkingSpace>{
    private PersistenceXMLManager<ParkingSpace> xml;

    public ParkingSpaceXmlRepository() throws IOException, JDOMException {
        this.xml = PersistenceXMLManager.openXMLFile(DatabasePaths.PARKING_SPACE_FILE.getPath(), "parkingSpaces", new ParkingSpaceXmlMapper());
    }
    @Override
    public List<ParkingSpace> findAll() {
        return xml.load();
    }

    @Override
    public Optional<ParkingSpace> findById(int id) {
        return xml.findById(id);
    }

    @Override
    public void insert(ParkingSpace parkingSpace) throws IOException {
        xml.insert(parkingSpace);
    }

    @Override
    public void update(ParkingSpace parkingSpace) throws IOException {
        xml.update(parkingSpace);
    }

    @Override
    public boolean deleteById(int id) throws IOException {
        return xml.deleteById(id);
    }

    @Override
    public boolean delete(ParkingSpace parkingSpace) throws IOException {
        return xml.delete(parkingSpace);
    }
}
