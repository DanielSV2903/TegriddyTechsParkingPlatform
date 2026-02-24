package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.repositories.ParkingTicketXmlRepository;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingTicket;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.ArrayList;

public class ParkingTicketData extends ParkingTicketXmlRepository {
    private ArrayList<ParkingTicket> tickets;

    public ParkingTicketData() throws IOException, JDOMException {
        super();
        tickets = new ArrayList<>();
        reload();
    }
    public void reload(){
        tickets.clear();
        tickets.addAll(super.findAll());
    }

    public ArrayList<ParkingTicket> getAllTickets() {
        return tickets;
    }

    public void registerTicket(ParkingTicket ticket) throws IOException {
        if (ticket == null) throw new IllegalArgumentException("ticket cannot be null");
        if (tickets.isEmpty()){
            super.insert(ticket);
            tickets.add(ticket);
        }
        if (findTicketById(ticket.getTicketId()) != null) {
            throw new IllegalArgumentException("Ticket with ID " + ticket.getTicketId() + " already exists.");
        }
        super.insert(ticket);
        tickets.add(ticket);
    }

    public ParkingTicket findTicketById(String ticketId) {
        return findById(ticketId).orElse(null);
    }

    public void updateTicket(ParkingTicket updatedTicket) throws IOException {
        if(findTicketById(updatedTicket.getTicketId()) == null) {
            throw new IllegalArgumentException("Ticket with ID " + updatedTicket.getTicketId() + " does not exist.");
        }
        super.update(updatedTicket);
        tickets.remove(findTicketById(updatedTicket.getTicketId()));
        tickets.add(updatedTicket);
    }

    public void deleteTicket(ParkingTicket ticket) throws IOException {
        if (!findById(ticket.getTicketId()).isPresent())
            throw new IllegalArgumentException("Ticket with ID " + ticket.getTicketId() + " does not exist.");
        super.delete(ticket);
        tickets.remove(ticket);
    }

    public int getNextId() {
        int id = 0;
        for (ParkingTicket ticket : tickets) {
            if (ticket.getTicketId().length() > id) {
                id = ticket.getTicketId().length();
            }
        }
        return id + 1;
    }
}
