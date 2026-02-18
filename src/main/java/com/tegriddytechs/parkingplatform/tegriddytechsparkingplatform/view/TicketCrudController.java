package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingLot;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingSpace;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingTicket;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Rate;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class TicketCrudController {

    private final MainMenuController mainMenuController;

    @FXML
    private TextField tfTicketId;
    @FXML
    private TableColumn<ParkingTicket, Rate> colAmount;
    @FXML
    private TextField tfAmount;
    @FXML
    private TableColumn<ParkingTicket, String> colVehiclePlate;
    @FXML
    private TextField tfSpaceId;
    @FXML
    private TableView<ParkingTicket> tableTickets;
    @FXML
    private TextField tfVehiclePlate;
    @FXML
    private TableColumn<ParkingTicket, LocalDateTime> colEntryDate;
    @FXML
    private Label lblTotalRecords;
    @FXML
    private DatePicker dpExitDate;
    @FXML
    private TableColumn<ParkingTicket, LocalDateTime> colExitDate;
    @FXML
    private TextField tfSearch;
    @FXML
    private TableColumn<ParkingTicket, Integer> colSpaceId;
    @FXML
    private DatePicker dpEntryDate;
    @FXML
    private TableColumn<ParkingTicket, String> colTicketId;
    @FXML
    private TextField tfLotId;
    @FXML
    private TextField tfSpaceNumber;
    @FXML
    private TextField tfRateId;

    private ObservableList<ParkingTicket> masterList = FXCollections.observableArrayList();
    private FilteredList<ParkingTicket> filteredList = new FilteredList<>(masterList, p -> true);

    public TicketCrudController(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
    }

    @FXML
    private void initialize() {
        try {
            colTicketId.setCellValueFactory(new PropertyValueFactory<>("ticketId"));
            colVehiclePlate.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getParkingSpace() != null && cell.getValue().getParkingSpace().getParkingLot() != null ? String.valueOf(cell.getValue().getParkingSpace().getParkingLot().getParkingLotId()) : ""));
            colSpaceId.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getParkingSpace() != null ? cell.getValue().getParkingSpace().getSpaceNumber() : null));
            colEntryDate.setCellValueFactory(new PropertyValueFactory<>("entryTime"));
            colExitDate.setCellValueFactory(new PropertyValueFactory<>("exitTime"));
            colAmount.setCellValueFactory(new PropertyValueFactory<>("rate"));

            tableTickets.setItems(filteredList);

            if (tfSearch != null) {
                tfSearch.textProperty().addListener((obs, oldV, newV) -> {
                    String lower = newV == null ? "" : newV.toLowerCase();
                    filteredList.setPredicate(t -> {
                        if (lower.isBlank()) return true;
                        return String.valueOf(t.getTicketId()).toLowerCase().contains(lower)
                                || (t.getParkingSpace() != null && String.valueOf(t.getParkingSpace().getSpaceNumber()).contains(lower));
                    });
                    updateRecordCount();
                });
            }

            loadData();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadData() {
        masterList.setAll(mainMenuController.getAllTickets());
        updateRecordCount();
    }

    private void updateRecordCount() {
        if (lblTotalRecords != null) lblTotalRecords.setText(String.valueOf(filteredList.size()));
    }

    @FXML
    private void onCreate(ActionEvent actionEvent) {
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
    private void onRead(ActionEvent actionEvent) {
        String ticketId = CrudFormUtils.readRequired(tfTicketId, "Tickets", "Id");
        if (ticketId == null) {
            return;
        }
        ParkingTicket ticket = mainMenuController.readTicketById(ticketId);
        CrudAlertHelper.showEntity("Tickets", ticket);
    }

    @FXML
    private void onUpdate(ActionEvent actionEvent) {
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
    private void onDelete(ActionEvent actionEvent) {
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
        int lotId = Integer.parseInt(CrudFormUtils.readRequired(tfLotId, "Tickets", "Id parqueadero"));
        if (lotId == 0) {
            return null;
        }
        ParkingLot lot = mainMenuController.readParkingLotById(lotId);
        if (lot == null) {
            CrudAlertHelper.showWarning("Tickets", "Parqueadero no encontrado");
        }
        return lot;
    }

    @FXML
    public void goBack(ActionEvent actionEvent) {
        if (actionEvent != null && actionEvent.getSource() instanceof Node node) {
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    public void onRefresh(ActionEvent actionEvent) {
        loadData();
    }

    @FXML
    public void onClear(ActionEvent actionEvent) {
        if (tfTicketId != null) tfTicketId.clear();
        if (tfVehiclePlate != null) tfVehiclePlate.clear();
        if (tfSpaceId != null) tfSpaceId.clear();
        if (dpEntryDate != null) dpEntryDate.setValue(null);
        if (dpExitDate != null) dpExitDate.setValue(null);
        if (tfAmount != null) tfAmount.clear();
        if (tfLotId != null) tfLotId.clear();
        if (tfSpaceNumber != null) tfSpaceNumber.clear();
        if (tfRateId != null) tfRateId.clear();
    }
}
