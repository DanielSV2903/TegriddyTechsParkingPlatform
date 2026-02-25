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

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RateCrudView {

    private final MainMenuView mainMenuController;

    @FXML private TextField tfRateId;
    @FXML private ComboBox<VehicleType> cbVehicleType;

    @FXML private ComboBox<TimeUnit> cbTimeUnit;
    @FXML private TextField tfFee;

    @FXML private TextArea taDescription;

    @FXML private TextField tfSearch;
    @FXML private TableView<Rate> tableRates;
    @FXML private TableColumn<Rate, Integer> colRateId;
    @FXML private TableColumn<Rate, String> colVehicleType;

    @FXML private TableColumn<Rate, String> colTimeUnit;
    @FXML private TableColumn<Rate, Double> colFee;

    @FXML private TableColumn<Rate, String> colDescription;
    @FXML private Label lblTotalRecords;

    private final ObservableList<Rate> rateList = FXCollections.observableArrayList();

    public RateCrudView(MainMenuView mainMenuController) {
        this.mainMenuController = mainMenuController;
    }

    @FXML
    private void initialize() {
        if (cbVehicleType != null) cbVehicleType.getItems().setAll(mainMenuController.getAllVehicleTypes());

        if (cbTimeUnit != null) {
            cbTimeUnit.getItems().setAll(TimeUnit.HOURS, TimeUnit.DAYS,TimeUnit.MINUTES);
        }

        if (colRateId != null) colRateId.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getRateId()));

        if (colVehicleType != null) colVehicleType.setCellValueFactory(cell -> {
            VehicleType vt = cell.getValue().getVehicleType();
            String desc = vt != null ? vt.getDescription() : "-";
            return new ReadOnlyObjectWrapper<>(desc);
        });

        if (colTimeUnit != null) colTimeUnit.setCellValueFactory(cell -> {
            TimeUnit tu = cell.getValue().getTimeUnit();
            return new ReadOnlyObjectWrapper<>(tu != null ? tu.name() : "-");
        });

        if (colFee != null) colFee.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getFee()));

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
            String vehicleDesc = r.getVehicleType() != null && r.getVehicleType().getDescription() != null
                    ? r.getVehicleType().getDescription().toLowerCase()
                    : "";
            String fee = String.valueOf(r.getFee()).toLowerCase();
            String unit = r.getTimeUnit() != null ? r.getTimeUnit().name().toLowerCase() : "";
            if (vehicleDesc.contains(q) || fee.contains(q) || unit.contains(q)) filtered.add(r);
        }
        tableRates.setItems(filtered);
        lblTotalRecords.setText(String.valueOf(filtered.size()));
    }

    private void loadRateToForm(Rate r) {
        tfRateId.setText(String.valueOf(r.getRateId()));
        if (r.getVehicleType() != null) {
            VehicleType st = r.getVehicleType();
            cbVehicleType.setValue(st);
            taDescription.setText(r.getVehicleType().getDescription());
        } else {
            cbVehicleType.setValue(null);
        }

        cbTimeUnit.setValue(r.getTimeUnit());
        tfFee.setText(String.valueOf(r.getFee()));
    }

    @FXML
    private void onCreate() {
        try {
            int id = Integer.parseInt(tfRateId.getText().trim());

            VehicleType st = cbVehicleType.getValue();
            if (st == null) {
                showAlert("Error", "Seleccione un tipo de vehículo", Alert.AlertType.ERROR);
                return;
            }

            TimeUnit unit = cbTimeUnit.getValue();
            if (unit == null) {
                showAlert("Error", "Seleccione la unidad de tiempo", Alert.AlertType.ERROR);
                return;
            }

            String feeText = tfFee.getText() != null ? tfFee.getText().trim() : "";
            if (feeText.isEmpty()) {
                showAlert("Error", "Ingrese el precio", Alert.AlertType.ERROR);
                return;
            }

            double fee = Double.parseDouble(feeText);
            if (fee < 0) {
                showAlert("Error", "El precio no puede ser negativo", Alert.AlertType.ERROR);
                return;
            }

            Rate r = new Rate(id, st, unit, fee);
            CrudAlertHelper.showResult("Tarifas", mainMenuController.createRate(r));

            onClear();
            loadData();
        } catch (NumberFormatException ex) {
            showAlert("Error", "Valores numéricos inválidos (ID/Precio)", Alert.AlertType.ERROR);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onUpdate() {
        try {
            int id = Integer.parseInt(tfRateId.getText().trim());

            VehicleType st = cbVehicleType.getValue();
            if (st == null) {
                showAlert("Error", "Seleccione un tipo de vehículo", Alert.AlertType.ERROR);
                return;
            }

            TimeUnit unit = cbTimeUnit.getValue();
            if (unit == null) {
                showAlert("Error", "Seleccione la unidad de tiempo", Alert.AlertType.ERROR);
                return;
            }

            String feeText = tfFee.getText() != null ? tfFee.getText().trim() : "";
            if (feeText.isEmpty()) {
                showAlert("Error", "Ingrese el precio", Alert.AlertType.ERROR);
                return;
            }

            double fee = Double.parseDouble(feeText);
            if (fee < 0) {
                showAlert("Error", "El precio no puede ser negativo", Alert.AlertType.ERROR);
                return;
            }

            Rate r = new Rate(id, st, unit, fee);
            CrudAlertHelper.showResult("Tarifas", mainMenuController.updateRate(r));

            onClear();
            loadData();
        } catch (NumberFormatException ex) {
            showAlert("Error", "Valores numéricos inválidos (ID/Precio)", Alert.AlertType.ERROR);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onClear() {
        if (tfRateId != null) tfRateId.clear();
        if (cbVehicleType != null) cbVehicleType.setValue(null);
        if (cbTimeUnit != null) cbTimeUnit.setValue(null);
        if (tfFee != null) tfFee.clear();
        if (taDescription != null) taDescription.clear();
        if (tableRates != null) tableRates.getSelectionModel().clearSelection();
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
        int vehicleTypeId;
        byte tyres;
        switch (spaceType) {
            case BICYCLE:
                vehicleTypeId = 1;
                tyres = 2;
                break;
            case MOTORCYCLE:
                vehicleTypeId = 2;
                tyres = 2;
                break;
            case CAR:
                vehicleTypeId = 3;
                tyres = 4;
                break;
            case HEAVY:
                vehicleTypeId = 4;
                tyres = 6;
                break;
            default:
                vehicleTypeId = 1;
                tyres = 4;
        }
        return new VehicleType(vehicleTypeId, desc, tyres, fee, spaceType);
    }

    private void updateRecordCount() {
        // No pises el ID si el usuario está editando uno existente.
        if (tfRateId != null && (tfRateId.getText() == null || tfRateId.getText().isBlank())) {
            tfRateId.setText(String.valueOf(mainMenuController.getAllRates().size() + 1));
        }
        if (lblTotalRecords != null) {
            lblTotalRecords.setText(String.valueOf((tableRates.getItems() == null) ? 0 : tableRates.getItems().size()));
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
