package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller;


import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.VehicleTypeData;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.OperationResult;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.SpaceType;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.VehicleType;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VehicleTypeController {

    private VehicleTypeData vehicleTypeData;

    public VehicleTypeController() throws IOException, JDOMException {
        this.vehicleTypeData = new VehicleTypeData();
    }
    public OperationResult addVehicleType(VehicleType vehicleType) throws IOException {
        if (findById(vehicleType.getId())!=null)
            return OperationResult.failure("VehicleType with ID " + vehicleType.getId() + " already exists.");
        vehicleTypeData.addVehicleType(vehicleType);
        return OperationResult.success("VehicleType created successfully");
    }

    public OperationResult updateVehicleType(VehicleType vehicleType) throws IOException {
        if (vehicleType==null)
            return OperationResult.failure("VehicleType cannot be null");
        vehicleTypeData.updateVehicleType(vehicleType);
        return OperationResult.success("VehicleType updated successfully");
    }

    public OperationResult removeVehicleType(VehicleType vehicleType) throws IOException {
        if (findById(vehicleType.getId())==null)
            return OperationResult.failure("VehicleType not found");
        vehicleTypeData.removeVehicleType(vehicleType);
        return OperationResult.success("VehicleType removed successfully");
    }

    public List<VehicleType> getAllVehicleTypes() {
        return vehicleTypeData.getAllVehicleTypes();
    }
    public VehicleType findById(int id){
        return vehicleTypeData.findById(id).orElse(null);
    }
}
