package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data;

public enum DatabasePaths {
   PARKING_LOT_FILE ("parking_lots.xml"),
    CUSTOMER_FILE ("customers.xml"),
    VEHICLE_FILE ("vehicles.xml"),
    VEHICLE_TYPE_FILE  ("vehicle_types.xml"),
    TICKET_FILE ("tickets.xml"),
    RATE_FILE ("rates.xml"),
    USER_FILE("users.xml"),
    PARKING_SPACE_FILE("spaces.xml");

    private String path;

    DatabasePaths(String path) {
        this.path = path;
    }
    public String getPath() {
        return path;
    }
}
