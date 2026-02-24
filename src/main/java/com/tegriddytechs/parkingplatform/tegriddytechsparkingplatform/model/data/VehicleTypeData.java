package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.repositories.VehicleTypeXmlRepository;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.VehicleType;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VehicleTypeData extends VehicleTypeXmlRepository {
    private List<VehicleType> vehicleTypes;

    public VehicleTypeData() throws IOException, JDOMException {
        super();
        vehicleTypes = new ArrayList<>();
        reload();
    }

    private void reload() {
        vehicleTypes.clear();
        vehicleTypes.addAll(super.findAll());
    }


    public void addVehicleType(VehicleType vehicleType) throws IOException {
        if (getAllVehicleTypes().isEmpty()){
            super.insert(vehicleType);
            vehicleTypes.add(vehicleType);
            return;
        }
        if (vehicleType==null)
            throw new IllegalArgumentException("VehicleType cannot be null");
        if (findById(vehicleType.getId()).isPresent())
            throw new IllegalArgumentException("VehicleType with ID " + vehicleType.getId() + " already exists.");
        super.insert(vehicleType);
        vehicleTypes.add(vehicleType);
    }

    public void updateVehicleType(VehicleType vehicleType) throws IOException {
        if (vehicleType==null)
            throw new IllegalArgumentException("VehicleType cannot be null");
        super.update(vehicleType);
        reload();
    }

    public void removeVehicleType(VehicleType vehicleType) throws IOException {
        if (findById(vehicleType.getId())==null)
            throw new IllegalArgumentException("VehicleType with ID " + vehicleType.getId() + " does not exist.");
        boolean deleted = super.delete(vehicleType);
        if (deleted) vehicleTypes.remove(vehicleType);
    }

    public Optional<VehicleType> findById(int id) {
        return super.findById(id);
    }

    public List<VehicleType> getAllVehicleTypes() {
        return super.findAll();
    }
}
