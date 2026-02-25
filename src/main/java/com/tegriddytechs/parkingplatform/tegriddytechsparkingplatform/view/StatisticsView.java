package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller.ParkingLotController;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller.ParkingSpaceController;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller.ParkingTicketController;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller.VehicleController;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.*;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.VehicleStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public class StatisticsView {

    // Labels del FXML (tarjetas)
    @FXML private Label totalParkingLots;
    @FXML private Label totalSpaces;
    @FXML private Label totalActiveVehicles;
    @FXML private Label todayUsage;

    // Para volver (si lo tienes)
    private final MainMenuView mainMenuController;

    // Controllers (pueden venir del mainMenuController o inyectarse como sea en tu proyecto)
    private ParkingLotController parkingLotController;
    private ParkingSpaceController parkingSpaceController;
    private VehicleController vehicleController;
    private ParkingTicketController parkingTicketController;
    @FXML
    private Label incomeMonth;
    @FXML
    private Label incomeToday;
    @FXML
    private Label incomeTotal;
    @FXML
    private Label avgTicket;
    @FXML
    private Label vehiclesTotal;
    @FXML
    private Label mostFrequentVehicle;
    @FXML
    private Label vehiclesParked;
    @FXML
    private Label vehiclesNotParked;

    public StatisticsView(MainMenuView mainMenuController) {
        this.mainMenuController = mainMenuController;
    }

    @FXML
    private void initialize() {
        this.parkingLotController = mainMenuController.getParkingLotController();
        this.parkingSpaceController = mainMenuController.getParkingSpaceController();
        this.vehicleController = mainMenuController.getVehicleController();
        this.parkingTicketController = mainMenuController.getParkingTicketController();

        updateDashboardStatistics();
    }

    public void updateDashboardStatistics() {
        try {
            if (totalParkingLots != null && parkingLotController != null) {
                List<ParkingLot> parkingLots = parkingLotController.getAllParkingLots();
                long activeLots = parkingLots.stream().filter(ParkingLot::isActive).count();
                totalParkingLots.setText(String.valueOf(activeLots));
            }

            if (totalSpaces != null && parkingSpaceController != null) {
                List<ParkingSpace> spaces = parkingSpaceController.getAllParkingSpaces();
                long availableSpaces = spaces.stream()
                        .filter(space -> !space.isState()) // Estado false = disponible
                        .count();
                totalSpaces.setText(String.valueOf(availableSpaces));
            }

            if (totalActiveVehicles != null && vehicleController != null) {
                List<Vehicle> vehicles = vehicleController.getAllVehicles();
                long parkedVehicles = vehicles.stream()
                        .filter(v -> v.getVehicleStatus() == VehicleStatus.PARKED)
                        .count();
                totalActiveVehicles.setText(String.valueOf(parkedVehicles));
            }

            if (todayUsage != null && parkingTicketController != null) {
                List<ParkingTicket> tickets = parkingTicketController.getAllTickets();
                LocalDateTime today = LocalDateTime.now();

                double todayRevenue = tickets.stream()
                        .filter(ticket -> ticket.getEntryTime() != null)
                        .filter(ticket -> {
                            LocalDateTime entryTime = ticket.getEntryTime();
                            return entryTime.getYear() == today.getYear()
                                    && entryTime.getMonth() == today.getMonth()
                                    && entryTime.getDayOfMonth() == today.getDayOfMonth();
                        })
                        .filter(ticket -> ticket.getExitTime() != null) // Solo tickets cerrados
                        .mapToDouble(ParkingTicket::getAmountPaid)
                        .sum();

                todayUsage.setText(String.format("%.0f", todayRevenue));
            }

        } catch (Exception e) {
            System.err.println("Error actualizando estadísticas del dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void onGenerateGeneralPdf(ActionEvent actionEvent) {
        // tomamos directo de los labels
        String lots = totalParkingLots != null ? totalParkingLots.getText() : "0";
        String spaces = totalSpaces != null ? totalSpaces.getText() : "0";
        String active = totalActiveVehicles != null ? totalActiveVehicles.getText() : "0";
        String income = todayUsage != null ? todayUsage.getText() : "0";

        FileChooser fc = new FileChooser();
        fc.setTitle("Guardar reporte PDF");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF (*.pdf)", "*.pdf"));
        fc.setInitialFileName("reporte_estadisticas_parqueos.pdf");

        Window w = totalParkingLots.getScene().getWindow();
        File out = fc.showSaveDialog(w);
        if (out == null) return;

        try {
            PdfReportService.createGeneralStatsPdf(out, lots, spaces, active, income);
            Alert ok = new Alert(Alert.AlertType.INFORMATION);
            ok.setTitle("PDF generado");
            ok.setHeaderText(null);
            ok.setContentText("Se generó el PDF correctamente.");
            ok.showAndWait();
        } catch (Exception ex) {
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setTitle("Error");
            err.setHeaderText("No se pudo generar el PDF");
            err.setContentText(ex.getMessage());
            err.showAndWait();
        }
    }

    @FXML
    public void onGenerateIncomePdf(ActionEvent e) {
        List<ParkingTicket> tickets = parkingTicketController.getAllTickets();

        FileChooser fc = new FileChooser();
        fc.setTitle("Guardar reporte de ingresos");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF (*.pdf)", "*.pdf"));
        fc.setInitialFileName("reporte_ingresos.pdf");

        File out = fc.showSaveDialog(totalParkingLots.getScene().getWindow());
        if (out == null) return;

        try {
            IncomeReportService.generateIncomeReportPdf(out, tickets);
        } catch (Exception ex) {
            ex.printStackTrace();
            // aquí puedes mostrar Alert
        }
    }

    @FXML
    public void goBack(ActionEvent actionEvent) {
        if (actionEvent != null && actionEvent.getSource() instanceof Node node) {
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    public void onGenerateVehiclePdf(ActionEvent actionEvent) {
        if (vehicleController == null || parkingTicketController == null) {
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setTitle("Error");
            err.setHeaderText("No se pudo generar el PDF de vehículos");
            err.setContentText("Los controladores de Vehicle y/o ParkingTicket no están disponibles.");
            err.showAndWait();
            return;
        }

        List<Vehicle> vehicles = vehicleController.getAllVehicles();
        List<ParkingTicket> tickets = parkingTicketController.getAllTickets();

        FileChooser fc = new FileChooser();
        fc.setTitle("Guardar reporte de vehículos");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF (*.pdf)", "*.pdf"));
        fc.setInitialFileName("reporte_vehiculos.pdf");

        Window w = totalParkingLots.getScene().getWindow(); // o cualquier nodo visible
        File out = fc.showSaveDialog(w);
        if (out == null) return;

        try {
            VehicleReportService.generateVehicleReportPdf(out, vehicles, tickets);

            Alert ok = new Alert(Alert.AlertType.INFORMATION);
            ok.setTitle("PDF generado");
            ok.setHeaderText(null);
            ok.setContentText("Se generó el PDF de vehículos correctamente.");
            ok.showAndWait();

        } catch (Exception ex) {
            ex.printStackTrace();
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setTitle("Error");
            err.setHeaderText("No se pudo generar el PDF de vehículos");
            err.setContentText(ex.getMessage());
            err.showAndWait();
        }
    }
}