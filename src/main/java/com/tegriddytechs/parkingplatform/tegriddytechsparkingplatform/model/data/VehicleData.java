package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.repositories.VehicleXmlRepository;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Vehicle;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VehicleData extends VehicleXmlRepository {

    private final ArrayList<Vehicle> vehicles = new ArrayList<>();

    public VehicleData() throws IOException, JDOMException {
        super();
        reload();
    }

    public void reload() {
        vehicles.clear();
        vehicles.addAll(super.findAll()); // XML -> cache
    }

    public ArrayList<Vehicle> getAllVehicles() {
        return vehicles;
    }

    public Vehicle findVehicleByLicensePlate(String plate) {
        if (plate == null) return null;
        for (Vehicle v : vehicles) {
            if (v != null && plate.equalsIgnoreCase(v.getPlate())) {
                return v;
            }
        }
        return null;
    }

    // Opcional: exponer también el findAll del repo como cache
    @Override
    public List<Vehicle> findAll() {
        return vehicles; // cache
    }

    public void registerVehicle(Vehicle vehicle) throws IOException {
        if (vehicles.isEmpty()){
            super.insert(vehicle);
            vehicles.add(vehicle);
            return;
        }
        if (vehicle == null) throw new IllegalArgumentException("vehicle cannot be null");

        Vehicle existing = findVehicleByLicensePlate(vehicle.getPlate());
        if (existing != null) {
            super.insert(vehicle);
            vehicles.add(vehicle);
        }else {
            throw new IllegalArgumentException("Vehicle already exists");
        }

    }

    public void removeVehicle(Vehicle vehicle) throws IOException {
        if (vehicle == null) return;

        // XML primero
        boolean deleted = super.delete(vehicle);
        if (!deleted) return;

        // cache después
        Vehicle existing = findVehicleByLicensePlate(vehicle.getPlate());
        if (existing != null) {
            vehicles.remove(existing);
        }
    }

    public void editVehicle(Vehicle vehicle) throws IOException {
        if (vehicle == null) throw new IllegalArgumentException("vehicle cannot be null");

        super.update(vehicle);

        Vehicle existing = findVehicleByLicensePlate(vehicle.getPlate());
        if (existing != null) {
            vehicles.remove(existing);
        }
        vehicles.add(vehicle);
    }
}
