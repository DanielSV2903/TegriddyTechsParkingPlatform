package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingLot;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingSpace;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.DatabasePaths.CUSTOMER_FILE;

/*
TODO
    On development
Handles the persistence of data to and from JSON files.
 */
public class PersistenceManager {
    private static final String PARKING_LOT_FILE = "parking_lots.xml";
    private static final String VEHICLE_TYPE_FILE = "vehicle_types.json";
    private static final String TICKET_FILE = "tickets.json";
    private static final String RATE_FILE = "rates.json";

    private final Gson gson;

    public PersistenceManager() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .setPrettyPrinting()
                .create();
    }
    public void saveVehicleTypeData(VehicleTypeData data) throws IOException {
        try (Writer writer = new FileWriter(VEHICLE_TYPE_FILE)) {
            gson.toJson(data, writer);
        }
    }

    public VehicleTypeData loadVehicleTypeData() {
        File file = new File(VEHICLE_TYPE_FILE);
        if (!file.exists()) {
            return new VehicleTypeData();
        }

        try (Reader reader = new FileReader(file)) {
            VehicleTypeData data = gson.fromJson(reader, VehicleTypeData.class);
            return data != null ? data : new VehicleTypeData();
        } catch (IOException e) {
            return new VehicleTypeData();
        }
    }

    public void saveParkingTicketData(ParkingTicketData data) throws IOException {
        try (Writer writer = new FileWriter(TICKET_FILE)) {
            gson.toJson(data, writer);
        }
    }

    public ParkingTicketData loadParkingTicketData() {
        File file = new File(TICKET_FILE);
        if (!file.exists()) {
            return new ParkingTicketData();
        }

        try (Reader reader = new FileReader(file)) {
            ParkingTicketData data = gson.fromJson(reader, ParkingTicketData.class);
            return data != null ? data : new ParkingTicketData();
        } catch (IOException e) {
            return new ParkingTicketData();
        }
    }

}
