package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingLot;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingSpace;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingTicket;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Rate;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;

public class TicketCrudController {

    private final MainMenuController mainMenuController;

    @FXML
    private TextField tfTicketId;
    @FXML
    private TextField tfLotId;
    @FXML
    private TextField tfSpaceNumber;
    @FXML
    private TextField tfRateId;

    public TicketCrudController(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
    }

    @FXML
    private void onCreate() {
        String ticketId = CrudFormUtils.readRequired(tfTicketId, "Tickets", "Id");
        ParkingLot lot = loadLot();
        Integer spaceNumber = CrudFormUtils.readInt(tfSpaceNumber, "Tickets", "Numero espacio");
        Integer rateId = CrudFormUtils.readInt(tfRateId, "Tickets", "Id tarifa");
        if (ticketId == null || lot == null || spaceNumber == null || rateId == null) {
            return;
        }
        ParkingSpace space = mainMenuController.readParkingSpaceByNumber(spaceNumber, lot);
        if (space == null) {
            CrudAlertHelper.showWarning("Tickets", "Espacio no encontrado");
            return;
        }
        Rate rate = mainMenuController.readRateById(rateId);
        if (rate == null) {
            CrudAlertHelper.showWarning("Tickets", "Tarifa no encontrada");
            return;
        }
        ParkingTicket ticket = new ParkingTicket(ticketId, space, LocalDateTime.now(), rate);
        CrudAlertHelper.showResult("Tickets", mainMenuController.createTicket(ticket));
    }

    @FXML
    private void onRead() {
        String ticketId = CrudFormUtils.readRequired(tfTicketId, "Tickets", "Id");
        if (ticketId == null) {
            return;
        }
        ParkingTicket ticket = mainMenuController.readTicketById(ticketId);
        CrudAlertHelper.showEntity("Tickets", ticket);
    }

    @FXML
    private void onUpdate() {
        String ticketId = CrudFormUtils.readRequired(tfTicketId, "Tickets", "Id");
        Integer rateId = CrudFormUtils.readInt(tfRateId, "Tickets", "Id tarifa");
        if (ticketId == null || rateId == null) {
            return;
        }
        ParkingTicket ticket = mainMenuController.readTicketById(ticketId);
        if (ticket == null) {
            CrudAlertHelper.showWarning("Tickets", "Ticket no encontrado");
            return;
        }
        Rate rate = mainMenuController.readRateById(rateId);
        if (rate == null) {
            CrudAlertHelper.showWarning("Tickets", "Tarifa no encontrada");
            return;
        }
        ticket.setRate(rate);
        ticket.setExitTime(LocalDateTime.now());
        CrudAlertHelper.showResult("Tickets", mainMenuController.updateTicket(ticket));
    }

    @FXML
    private void onDelete() {
        String ticketId = CrudFormUtils.readRequired(tfTicketId, "Tickets", "Id");
        if (ticketId == null) {
            return;
        }
        ParkingTicket ticket = mainMenuController.readTicketById(ticketId);
        if (ticket == null) {
            CrudAlertHelper.showWarning("Tickets", "Ticket no encontrado");
            return;
        }
        CrudAlertHelper.showResult("Tickets", mainMenuController.deleteTicket(ticket));
    }

    private ParkingLot loadLot() {
        String lotId = CrudFormUtils.readRequired(tfLotId, "Tickets", "Id parqueadero");
        if (lotId == null) {
            return null;
        }
        ParkingLot lot = mainMenuController.readParkingLotById(lotId);
        if (lot == null) {
            CrudAlertHelper.showWarning("Tickets", "Parqueadero no encontrado");
        }
        return lot;
    }
}
