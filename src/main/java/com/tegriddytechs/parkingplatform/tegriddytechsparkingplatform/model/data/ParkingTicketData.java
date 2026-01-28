package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingTicket;

import java.util.ArrayList;

public class ParkingTicketData {

    private ArrayList<ParkingTicket> tickets;

    public ParkingTicketData() {
        this.tickets = new ArrayList<>();
    }

    public ArrayList<ParkingTicket> getAllTickets() {
        return tickets;
    }

    public void registerTicket(ParkingTicket ticket) {
        tickets.add(ticket);
    }

    public ParkingTicket findTicketById(String ticketId) {
        for (ParkingTicket ticket : tickets) {
            if (ticket.getTicketId().equals(ticketId)) {
                return ticket;
            }
        }
        return null;
    }

    public void updateTicket(ParkingTicket updatedTicket) {
        for (int i = 0; i < tickets.size(); i++) {
            if (tickets.get(i).getTicketId().equals(updatedTicket.getTicketId())) {
                tickets.set(i, updatedTicket);
                return;
            }
        }
    }

    public void deleteTicket(ParkingTicket ticket){
        tickets.remove(ticket);
    }

}
