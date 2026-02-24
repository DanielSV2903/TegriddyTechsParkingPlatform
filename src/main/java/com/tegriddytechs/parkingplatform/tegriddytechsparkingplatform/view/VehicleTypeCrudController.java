package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.OperationResult;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.SpaceType;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.VehicleType;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class VehicleTypeCrudController {

    private final MainMenuController mainMenuController;

    @FXML private TextField tfId;
    @FXML private TextField tfDescription;
    @FXML private TextField tfAmountOfTyres;
    @FXML private TextField tfFee;
    @FXML private ComboBox<SpaceType> cbSpaceType;

    @FXML private TextField tfSearch;
    @FXML private TableView<VehicleType> tableVehicleTypes;
    @FXML private TableColumn<VehicleType, Integer> colId;
    @FXML private TableColumn<VehicleType, String> colDescription;
    @FXML private TableColumn<VehicleType, Integer> colTyres;
    @FXML private TableColumn<VehicleType, Double> colFee;
    @FXML private TableColumn<VehicleType, String> colSpaceType;
    @FXML private Label lblTotalRecords;

    private final ObservableList<VehicleType> masterList = FXCollections.observableArrayList();

    public VehicleTypeCrudController(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
    }

    @FXML
    private void initialize() {
        if (cbSpaceType != null) cbSpaceType.getItems().setAll(SpaceType.values());

        if (colId != null) colId.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getId()));
        if (colDescription != null) colDescription.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getDescription()));
        if (colTyres != null) colTyres.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>((int) cell.getValue().getAmountOfTyres()));
        if (colFee != null) colFee.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getFee()));
        if (colSpaceType != null) colSpaceType.setCellValueFactory(cell -> {
            SpaceType st = cell.getValue().getSpaceType();
            return new ReadOnlyObjectWrapper<>(st != null ? st.name() : "");
        });

        if (tableVehicleTypes != null) {
            tableVehicleTypes.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
                if (newSel != null) loadToForm(newSel);
            });
        }

        if (tfSearch != null) {
            tfSearch.textProperty().addListener((obs, oldV, newV) -> filter(newV));
        }

        loadData();
    }

    private void loadData() {
        masterList.setAll(mainMenuController.getAllVehicleTypes());
        if (tableVehicleTypes != null) tableVehicleTypes.setItems(masterList);
        updateRecordCount();
    }

    private void updateRecordCount() {
        if (lblTotalRecords != null) {
            int count = tableVehicleTypes != null && tableVehicleTypes.getItems() != null
                    ? tableVehicleTypes.getItems().size()
                    : 0;
            lblTotalRecords.setText(String.valueOf(count));
        }
    }

    private void loadToForm(VehicleType vt) {
        if (vt == null) return;

        if (tfId != null) tfId.setText(String.valueOf(vt.getId()));
        if (tfDescription != null) tfDescription.setText(vt.getDescription() != null ? vt.getDescription() : "");
        if (tfAmountOfTyres != null) tfAmountOfTyres.setText(String.valueOf(vt.getAmountOfTyres()));
        if (tfFee != null) tfFee.setText(String.valueOf(vt.getFee()));
        if (cbSpaceType != null) cbSpaceType.setValue(vt.getSpaceType());
    }

    private void filter(String query) {
        if (tableVehicleTypes == null) return;

        if (query == null || query.trim().isEmpty()) {
            tableVehicleTypes.setItems(masterList);
            updateRecordCount();
            return;
        }

        String q = query.toLowerCase().trim();
        ObservableList<VehicleType> filtered = FXCollections.observableArrayList();

        for (VehicleType vt : masterList) {
            if (vt == null) continue;

            String id = String.valueOf(vt.getId());
            String desc = vt.getDescription() != null ? vt.getDescription().toLowerCase() : "";
            String tyres = String.valueOf(vt.getAmountOfTyres());
            String fee = String.valueOf(vt.getFee());
            String st = vt.getSpaceType() != null ? vt.getSpaceType().name().toLowerCase() : "";

            if (id.contains(q) || desc.contains(q) || tyres.contains(q) || fee.contains(q) || st.contains(q)) {
                filtered.add(vt);
            }
        }

        tableVehicleTypes.setItems(filtered);
        if (lblTotalRecords != null) lblTotalRecords.setText(String.valueOf(filtered.size()));
    }

    @FXML
    private void onCreate() {
        VehicleType vt = readForm();
        if (vt == null) return;

        try {
            OperationResult result = mainMenuController.getVehicleTypeController().addVehicleType(vt);
            CrudAlertHelper.showResult("Tipos de Vehículo", result);
            onClear();
            loadData();
        } catch (IOException e) {
            CrudAlertHelper.showWarning("Tipos de Vehículo", "No se pudo registrar: " + e.getMessage());
        }
    }

    @FXML
    private void onUpdate() {
        VehicleType vt = readForm();
        if (vt == null) return;

        try {
            OperationResult result = mainMenuController.getVehicleTypeController().updateVehicleType(vt);
            CrudAlertHelper.showResult("Tipos de Vehículo", result);
            onClear();
            loadData();
        } catch (IOException e) {
            CrudAlertHelper.showWarning("Tipos de Vehículo", "No se pudo actualizar: " + e.getMessage());
        }
    }

    @FXML
    private void onDelete() {
        Integer id = readInt(tfId, "ID");
        if (id == null) return;

        VehicleType existing = mainMenuController.getVehicleTypeController().findById(id);
        if (existing == null) {
            CrudAlertHelper.showWarning("Tipos de Vehículo", "No existe un tipo con ID: " + id);
            return;
        }

        try {
            OperationResult result = mainMenuController.getVehicleTypeController().removeVehicleType(existing);
            CrudAlertHelper.showResult("Tipos de Vehículo", result);
            onClear();
            loadData();
        } catch (IOException e) {
            CrudAlertHelper.showWarning("Tipos de Vehículo", "No se pudo eliminar: " + e.getMessage());
        }
    }

    @FXML
    private void onClear() {
        if (tfId != null) tfId.clear();
        if (tfDescription != null) tfDescription.clear();
        if (tfAmountOfTyres != null) tfAmountOfTyres.clear();
        if (tfFee != null) tfFee.clear();
        if (cbSpaceType != null) cbSpaceType.setValue(null);
        if (tableVehicleTypes != null) tableVehicleTypes.getSelectionModel().clearSelection();
    }

    @FXML
    public void onRefresh() {
        loadData();
    }

    @FXML
    public void goBack(javafx.event.ActionEvent actionEvent) {
        if (actionEvent != null && actionEvent.getSource() instanceof Node node) {
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
        }
    }

    private VehicleType readForm() {
        Integer id = readInt(tfId, "ID");
        if (id == null) return null;

        String description = tfDescription != null ? tfDescription.getText().trim() : "";
        if (description.isBlank()) {
            CrudAlertHelper.showWarning("Tipos de Vehículo", "La descripción es obligatoria");
            return null;
        }

        Integer tyresInt = readInt(tfAmountOfTyres, "Cantidad de llantas");
        if (tyresInt == null) return null;
        if (tyresInt < 0 || tyresInt > 127) {
            CrudAlertHelper.showWarning("Tipos de Vehículo", "La cantidad de llantas debe estar entre 0 y 127");
            return null;
        }
        byte tyres = (byte) tyresInt.intValue();

        Double fee = readDouble(tfFee, "Tarifa (fee)");
        if (fee == null) return null;

        SpaceType st = cbSpaceType != null ? cbSpaceType.getValue() : null;
        if (st == null) {
            CrudAlertHelper.showWarning("Tipos de Vehículo", "Debe seleccionar un SpaceType");
            return null;
        }

        return new VehicleType(id, description, tyres, fee, st);
    }

    private Integer readInt(TextField tf, String fieldName) {
        if (tf == null) return null;
        String raw = tf.getText() != null ? tf.getText().trim() : "";
        if (raw.isBlank()) {
            CrudAlertHelper.showWarning("Tipos de Vehículo", "Campo obligatorio: " + fieldName);
            return null;
        }
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException ex) {
            CrudAlertHelper.showWarning("Tipos de Vehículo", "Valor inválido para " + fieldName + ": " + raw);
            return null;
        }
    }

    private Double readDouble(TextField tf, String fieldName) {
        if (tf == null) return null;
        String raw = tf.getText() != null ? tf.getText().trim() : "";
        if (raw.isBlank()) {
            CrudAlertHelper.showWarning("Tipos de Vehículo", "Campo obligatorio: " + fieldName);
            return null;
        }
        try {
            return Double.parseDouble(raw);
        } catch (NumberFormatException ex) {
            CrudAlertHelper.showWarning("Tipos de Vehículo", "Valor inválido para " + fieldName + ": " + raw);
            return null;
        }
    }
}
