package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Rate;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.SpaceType;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.VehicleType;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class RateCrudController {

    private final MainMenuController mainMenuController;

    @FXML
    private TextField tfRateId;
    @FXML
    private ComboBox<SpaceType> cbVehicleType;
    @FXML
    private TextField tfHourlyRate;
    @FXML
    private TextField tfDailyRate;
    @FXML
    private TextArea taDescription;

    @FXML
    private TextField tfSearch;
    @FXML
    private TableView<Rate> tableRates;
    @FXML
    private TableColumn<Rate, Integer> colRateId;
    @FXML
    private TableColumn<Rate, String> colVehicleType;
    @FXML
    private TableColumn<Rate, Double> colHourlyRate;
    @FXML
    private TableColumn<Rate, Double> colDailyRate;
    @FXML
    private TableColumn<Rate, String> colDescription;
    @FXML
    private Label lblTotalRecords;

    private ObservableList<Rate> rateList = FXCollections.observableArrayList();

    public RateCrudController(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
    }

    @FXML
    private void initialize() {
        if (cbVehicleType != null) cbVehicleType.getItems().setAll(SpaceType.values());

        if (colRateId != null) colRateId.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getRateId()));
        if (colVehicleType != null) colVehicleType.setCellValueFactory(cell -> {
            VehicleType vt = cell.getValue().getVehicleType();
            String desc = vt != null ? vt.getDescription() : "-";
            return new ReadOnlyObjectWrapper<>(desc);
        });
        if (colHourlyRate != null) colHourlyRate.setCellValueFactory(cell -> {
            Rate r = cell.getValue();
            Double v = r.getTimeUnit() == TimeUnit.HOURS ? r.getFee() : null;
            return new ReadOnlyObjectWrapper<>(v);
        });
        if (colDailyRate != null) colDailyRate.setCellValueFactory(cell -> {
            Rate r = cell.getValue();
            Double v = r.getTimeUnit() == TimeUnit.DAYS ? r.getFee() : null;
            return new ReadOnlyObjectWrapper<>(v);
        });
        if (colDescription != null) colDescription.setCellValueFactory(cell -> {
            VehicleType vt = cell.getValue().getVehicleType();
            String desc = vt != null ? vt.getDescription() : "";
            return new ReadOnlyObjectWrapper<>(desc);
        });

        if (tableRates != null) {
            tableRates.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
                if (newSel != null) loadRateToForm(newSel);
            });
        }

        if (tfSearch != null) {
            tfSearch.textProperty().addListener((obs, oldVal, newVal) -> filterTable(newVal));
        }

        loadData();
    }

    private void loadData() {
        List<Rate> all = mainMenuController.getAllRates();
        rateList.setAll(all);
        if (tableRates != null) tableRates.setItems(rateList);
        updateRecordCount();
    }

    private void filterTable(String query) {
        if (query == null || query.trim().isEmpty()) {
            tableRates.setItems(rateList);
            updateRecordCount();
            return;
        }
        String q = query.toLowerCase();
        ObservableList<Rate> filtered = FXCollections.observableArrayList();
        for (Rate r : rateList) {
            String vehicleDesc = r.getVehicleType() != null ? r.getVehicleType().getDescription().toLowerCase() : "";
            String fee = String.valueOf(r.getFee()).toLowerCase();
            if (vehicleDesc.contains(q) || fee.contains(q)) filtered.add(r);
        }
        tableRates.setItems(filtered);
        lblTotalRecords.setText(String.valueOf(filtered.size()));
    }

    private void loadRateToForm(Rate r) {
        tfRateId.setText(String.valueOf(r.getRateId()));
        if (r.getVehicleType() != null) {
            SpaceType st = r.getVehicleType().getSpaceType();
            cbVehicleType.setValue(st);
            taDescription.setText(r.getVehicleType().getDescription());
        }
        if (r.getTimeUnit() == TimeUnit.HOURS) tfHourlyRate.setText(String.valueOf(r.getFee()));
        else if (r.getTimeUnit() == TimeUnit.DAYS) tfDailyRate.setText(String.valueOf(r.getFee()));
    }

    @FXML
    private void onCreate() {
        try {
            int id = Integer.parseInt(tfRateId.getText().trim());
            SpaceType st = cbVehicleType.getValue();
            if (st == null) {
                showAlert("Error", "Seleccione un tipo de vehículo", Alert.AlertType.ERROR);
                return;
            }

            String desc = taDescription.getText() != null ? taDescription.getText().trim() : "Default";

            if (tfHourlyRate.getText() != null && !tfHourlyRate.getText().trim().isEmpty()) {
                double hourly = Double.parseDouble(tfHourlyRate.getText().trim());
                Rate r = new Rate(id, defaultVehicleType(hourly, st, desc), TimeUnit.HOURS, hourly);
                CrudAlertHelper.showResult("Tarifas", mainMenuController.createRate(r));
            }
            if (tfDailyRate.getText() != null && !tfDailyRate.getText().trim().isEmpty()) {
                double daily = Double.parseDouble(tfDailyRate.getText().trim());
                Rate r2 = new Rate(id, defaultVehicleType(daily, st, desc), TimeUnit.DAYS, daily);
                CrudAlertHelper.showResult("Tarifas", mainMenuController.createRate(r2));
            }

            onClear();
            loadData();
        } catch (NumberFormatException ex) {
            showAlert("Error", "Valores numéricos inválidos", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onUpdate() {
        try {
            int id = Integer.parseInt(tfRateId.getText().trim());
            SpaceType st = cbVehicleType.getValue();
            if (st == null) {
                showAlert("Error", "Seleccione un tipo de vehículo", Alert.AlertType.ERROR);
                return;
            }
            String desc = taDescription.getText() != null ? taDescription.getText().trim() : "Default";

            if (tfHourlyRate.getText() != null && !tfHourlyRate.getText().trim().isEmpty()) {
                double hourly = Double.parseDouble(tfHourlyRate.getText().trim());
                Rate r = new Rate(id, defaultVehicleType(hourly, st, desc), TimeUnit.HOURS, hourly);
                CrudAlertHelper.showResult("Tarifas", mainMenuController.updateRate(r));
            }
            if (tfDailyRate.getText() != null && !tfDailyRate.getText().trim().isEmpty()) {
                double daily = Double.parseDouble(tfDailyRate.getText().trim());
                Rate r2 = new Rate(id, defaultVehicleType(daily, st, desc), TimeUnit.DAYS, daily);
                CrudAlertHelper.showResult("Tarifas", mainMenuController.updateRate(r2));
            }

            onClear();
            loadData();
        } catch (NumberFormatException ex) {
            showAlert("Error", "Valores numéricos inválidos", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onDelete() {
        try {
            int id = Integer.parseInt(tfRateId.getText().trim());
            Rate existing = mainMenuController.readRateById(id);
            if (existing == null) {
                CrudAlertHelper.showWarning("Tarifas", "Tarifa no encontrada");
                return;
            }
            CrudAlertHelper.showResult("Tarifas", mainMenuController.deleteRate(existing));
            onClear();
            loadData();
        } catch (NumberFormatException ex) {
            showAlert("Error", "Id inválido", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onClear() {
        tfRateId.clear();
        cbVehicleType.setValue(null);
        tfHourlyRate.clear();
        tfDailyRate.clear();
        taDescription.clear();
        tableRates.getSelectionModel().clearSelection();
    }

    @FXML
    private void onRefresh() {
        loadData();
    }

    @FXML
    private void goBack() {
        if (tfRateId.getScene() != null) ((Stage) tfRateId.getScene().getWindow()).close();
    }

    private VehicleType defaultVehicleType(double fee, SpaceType spaceType, String desc) {
        return new VehicleType(1, desc, (byte) 4, fee, spaceType);
    }

    private void updateRecordCount() {
        tfRateId.setText(String.valueOf(mainMenuController.getAllRates().size()+1));
        lblTotalRecords.setText(String.valueOf((tableRates.getItems() == null) ? 0 : tableRates.getItems().size()));
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
