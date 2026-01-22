package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingLot;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingSpace;

import java.io.*;
import java.util.ArrayList;

/*
TODO
    On development
Handles the persistence of data to and from JSON files.
 */
public class PersistenceManager {
    private static final String PARKING_LOT_FILE = "parking_lots.json";
    private static final String CUSTOMER_FILE = "customers.json";
    private static final String VEHICLE_FILE = "vehicles.json";
    private static final String TICKET_FILE = "tickets.json";

    private final Gson gson;

    public PersistenceManager() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeSpecialFloatingPointValues()
                .create();
    }

    public void saveParkingLotData(ParkingLotData data) throws IOException {
        try (Writer writer = new FileWriter(PARKING_LOT_FILE)) {
            gson.toJson(data, writer);
        }
    }

    public ParkingLotData loadParkingLotData() {
        File file = new File(PARKING_LOT_FILE);
        if (!file.exists()) {
            return new ParkingLotData();
        }

        try (Reader reader = new FileReader(file)) {
            ParkingLotData data = gson.fromJson(reader, ParkingLotData.class);
            if (data != null && data.getAllParkingLots() != null) {
                for (ParkingLot lot : data.getAllParkingLots()) {
                    if (lot.getSpaces() != null) {
                        for (ParkingSpace space : lot.getSpaces()) {
                            space.setParkingLot(lot);
                        }
                    }
                }
            }
            return data != null ? data : new ParkingLotData();
        } catch (IOException e) {
            return new ParkingLotData();
        }
    }

    public void saveCustomerData(CustomerData data) throws IOException {
        try (Writer writer = new FileWriter(CUSTOMER_FILE)) {
            gson.toJson(data, writer);
        }
    }

    public CustomerData loadCustomerData() {
        File file = new File(CUSTOMER_FILE);
        if (!file.exists()) {
            return new CustomerData();
        }

        try (Reader reader = new FileReader(file)) {
            CustomerData data = gson.fromJson(reader, CustomerData.class);
            return data != null ? data : new CustomerData();
        } catch (IOException e) {
            return new CustomerData();
        }
    }

    public void saveVehicleData(VehicleData data) throws IOException {
        try (Writer writer = new FileWriter(VEHICLE_FILE)) {
            gson.toJson(data, writer);
        }
    }

    public VehicleData loadVehicleData() {
        File file = new File(VEHICLE_FILE);
        if (!file.exists()) {
            return new VehicleData();
        }

        try (Reader reader = new FileReader(file)) {
            VehicleData data = gson.fromJson(reader, VehicleData.class);
            return data != null ? data : new VehicleData();
        } catch (IOException e) {
            return new VehicleData();
        }
    }
}
