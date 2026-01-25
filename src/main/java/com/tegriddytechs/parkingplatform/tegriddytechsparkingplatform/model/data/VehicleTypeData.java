package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data;

import java.util.HashMap;
import java.util.Map;

public class VehicleTypeData {

    private Map<String, Integer> vehicleTypeCounts;
    private transient PersistenceManager persistenceManager;

    public VehicleTypeData() {
        this.vehicleTypeCounts = new HashMap<>();
        this.persistenceManager = new PersistenceManager();
    }

    public void save() {
        try {
            persistenceManager.saveVehicleTypeData(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Integer> getVehicleTypeCounts() {
        return vehicleTypeCounts;
    }

    public int getCount(String key) {
        Integer v = vehicleTypeCounts.get(key);
        return (v == null) ? 0 : v;
    }

    public void setCount(String key, int value) {
        vehicleTypeCounts.put(key, value);
        save();
    }

    public void increment(String key) {
        vehicleTypeCounts.put(key, getCount(key) + 1);
        save();
    }

    public void decrement(String key) {
        int current = getCount(key);
        if (current > 0) {
            vehicleTypeCounts.put(key, current - 1);
            save();
        }
    }
}
