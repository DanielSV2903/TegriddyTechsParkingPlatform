package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class ParkingTicket {
    private String ticketId;
    private ParkingSpace parkingSpace;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private Rate rate;

    public ParkingTicket(String ticketId, ParkingSpace parkingSpace, LocalDateTime entryTime, Rate rate, LocalDateTime exitTime) {
        this.ticketId = ticketId;
        this.parkingSpace = parkingSpace;
        this.entryTime = entryTime;
        this.rate = rate;
        this.exitTime = exitTime;
    }

    public ParkingTicket(String ticketId, ParkingSpace parkingSpace, LocalDateTime entryTime, Rate rate) {
        this.ticketId = ticketId;
        this.parkingSpace = parkingSpace;
        this.entryTime = entryTime;
        this.rate = rate;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public ParkingSpace getParkingSpace() {
        return parkingSpace;
    }

    public void setParkingSpace(ParkingSpace parkingSpace) {
        this.parkingSpace = parkingSpace;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ParkingTicket that = (ParkingTicket) o;
        return Objects.equals(ticketId, that.ticketId) && Objects.equals(parkingSpace, that.parkingSpace) && Objects.equals(entryTime, that.entryTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketId, parkingSpace, entryTime);
    }

    @Override
    public String toString() {
        return "ParkingTicket{" +
                "ticketId='" + ticketId + '\'' +
                ", parkingSpace=" + parkingSpace +
                ", entryTime=" + entryTime +
                ", exitTime=" + exitTime +
                ", rate=" + rate +
                '}';
    }

}
