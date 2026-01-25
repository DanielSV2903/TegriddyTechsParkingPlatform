package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.PersistenceManager;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.VehicleTypeData;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.SpaceType;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.VehicleType;

import java.util.ArrayList;

public class VehicleTypeController {

    private VehicleTypeData vehicleTypeData;

    public VehicleTypeController() {
        this.vehicleTypeData = new PersistenceManager().loadVehicleTypeData();
        if (this.vehicleTypeData.getVehicleTypeCounts() == null) {
            // Por si el JSON está vacío o viene null
            this.vehicleTypeData = new VehicleTypeData();
        }
        initKeys();
    }

    // Tipos predefinidos (no se guardan en JSON, solo se usan para la lógica)
    public ArrayList<VehicleType> getPredefinedVehicleTypes() {
        ArrayList<VehicleType> list = new ArrayList<>();

        list.add(new VehicleType(2, "Bicycle", (byte) 2, 0.0, SpaceType.BICYCLE));
        list.add(new VehicleType(2, "Motorcycle", (byte) 2, 0.0, SpaceType.MOTORCYCLE));
        list.add(new VehicleType(4, "Car", (byte) 4, 0.0, SpaceType.CAR));
        list.add(new VehicleType(6, "Heavy Vehicle", (byte) 6, 0.0, SpaceType.HEAVY));

        return list;
    }

    public int getCount(VehicleType type) {
        return vehicleTypeData.getCount(buildKey(type));
    }

    public void increaseCount(VehicleType type) {
        vehicleTypeData.increment(buildKey(type));
    }

    public void decreaseCount(VehicleType type) {
        vehicleTypeData.decrement(buildKey(type));
    }

    public String buildKey(VehicleType type) {
        return type.getId() + "_" + type.getSpaceType().name();
    }

    private void initKeys() {
        for (VehicleType type : getPredefinedVehicleTypes()) {
            String key = buildKey(type);
            if (vehicleTypeData.getVehicleTypeCounts().containsKey(key) == false) {
                vehicleTypeData.getVehicleTypeCounts().put(key, 0);
            }
        }
        vehicleTypeData.save();
    }
}
