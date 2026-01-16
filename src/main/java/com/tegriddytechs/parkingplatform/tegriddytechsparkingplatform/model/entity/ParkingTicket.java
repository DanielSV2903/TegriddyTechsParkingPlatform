package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity;

import java.time.LocalDateTime;

public class ParkingTicket {
    private String ticketId;
    private ParkingSpace parkingSpace;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private Rate rate;
}
