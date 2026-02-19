package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.repositories;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.DatabasePaths;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.PersistenceXMLManager;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.mappers.VehicleTypeXmlMapper;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.VehicleType;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class VehicleTypeXmlRepository implements DataRepository<VehicleType>{
    private PersistenceXMLManager<VehicleType> xml;

    public VehicleTypeXmlRepository() throws IOException, JDOMException {
        this.xml = PersistenceXMLManager.openXMLFile(DatabasePaths.VEHICLE_TYPE_FILE.getPath(), "vehicleTypes", new VehicleTypeXmlMapper());
    }

    @Override
    public List<VehicleType> findAll() {
        return xml.load();
    }

    @Override
    public Optional<VehicleType> findById(int id) {
        return xml.findById(id);
    }

    @Override
    public void update(VehicleType vehicleType) throws IOException {
        xml.update(vehicleType);
    }

    @Override
    public void insert(VehicleType vehicleType) throws IOException {
        xml.insert(vehicleType);
    }

    @Override
    public boolean deleteById(int id) throws IOException {
        return xml.deleteById(id);
    }

    @Override
    public boolean delete(VehicleType vehicleType) throws IOException {
        return xml.delete(vehicleType);
    }
}
