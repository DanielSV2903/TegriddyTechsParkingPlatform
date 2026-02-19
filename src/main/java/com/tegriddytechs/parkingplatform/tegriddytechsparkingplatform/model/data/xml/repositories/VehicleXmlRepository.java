package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.repositories;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.DatabasePaths;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.PersistenceXMLManager;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.XmlEntityMapper;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.mappers.VehicleXmlMapper;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Vehicle;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class VehicleXmlRepository implements DataRepository<Vehicle>{
    protected PersistenceXMLManager<Vehicle> xmlManager;

    public VehicleXmlRepository() throws IOException, JDOMException {
        this.xmlManager =PersistenceXMLManager.openXMLFile(DatabasePaths.VEHICLE_FILE.getPath(), "vehicles",new VehicleXmlMapper());
    }
    @Override
    public List<Vehicle> findAll() {
        return xmlManager.load();
    }

    @Override
    public Optional<Vehicle> findById(int id) {
        return xmlManager.findById(id);
    }


    @Override
    public void insert(Vehicle vehicle) throws IOException {
        xmlManager.insert(vehicle);
    }

    @Override
    public void update(Vehicle vehicle) throws IOException {
        xmlManager.update(vehicle);
    }

    @Override
    public boolean deleteById(int id) throws IOException {
        return xmlManager.deleteById(id);
    }

    @Override
    public boolean delete(Vehicle vehicle) throws IOException {
        return xmlManager.delete(vehicle);
    }
}
