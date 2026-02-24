package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.ParkingTicketData;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.OperationResult;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingTicket;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.List;

public class ParkingTicketController {
    ParkingTicketData ticketData;
    public ParkingTicketController() throws IOException, JDOMException {
        ticketData = new ParkingTicketData();
    }

    public void addParkingTicket(ParkingTicket ticket) throws IOException {
        ticketData.registerTicket(ticket);
    }

    public void removeParkingTicket(ParkingTicket existing) throws IOException {
        ticketData.deleteTicket(existing);
    }

    public OperationResult updateParkingTicket(ParkingTicket ticket) throws IOException {
        if (ticketData.findTicketById(ticket.getTicketId()) == null) {
            return OperationResult.failure("Ticket not found");
        }
        ticketData.updateTicket(ticket);
        return OperationResult.success("Ticket updated successfully");
    }

    public List<ParkingTicket> getAllTickets() {
        return ticketData.getAllTickets();
    }
    public ParkingTicket findById(String id){
        return ticketData.findTicketById(id);
    }

    public int getNextId() {
        return ticketData.getNextId();
    }
}
