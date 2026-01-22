package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.VehicleData;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.PersistenceManager;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Vehicle;
import java.util.ArrayList;

public class VehicleController {
    private VehicleData vehicleData;

    public VehicleController() {
        this.vehicleData = new PersistenceManager().loadVehicleData();
    }

    public ArrayList<Vehicle> getAllVehicles() {
        return vehicleData.getAllVehicles();
    }

    public Vehicle findVehicleByPlate(String plate) {
        return vehicleData.findVehicleByLicensePlate(plate);
    }

    public OperationResult registerVehicle(Vehicle vehicle) {
        if (vehicleData.findVehicleByLicensePlate(vehicle.getPlate()) != null) {
            return OperationResult.failure("Vehicle already exists");
        }
        vehicleData.registerVehicle(vehicle);
        return OperationResult.success("Vehicle registered successfully");
    }

    public OperationResult deleteVehicle(Vehicle vehicle) {
        if (vehicleData.findVehicleByLicensePlate(vehicle.getPlate()) == null) {
            return OperationResult.failure("Vehicle not found");
        }
        vehicleData.removeVehicle(vehicle);
        return OperationResult.success("Vehicle removed successfully");
    }

    public OperationResult editVehicle(Vehicle vehicle) {
        if (vehicleData.findVehicleByLicensePlate(vehicle.getPlate()) == null) {
            return OperationResult.failure("Vehicle not found");
        }
        vehicleData.editVehicle(vehicle);
        return OperationResult.success("Vehicle updated successfully");
    }
}
