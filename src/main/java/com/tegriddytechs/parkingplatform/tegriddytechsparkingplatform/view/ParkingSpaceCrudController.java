package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingLot;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingSpace;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.SpaceType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class ParkingSpaceCrudController {

    private final MainMenuController mainMenuController;
    @FXML
    private TableColumn<ParkingSpace, String> colParkingLotId;
    @FXML
    private Label lblTotalRecords;
    @FXML
    private ComboBox<SpaceType> cbSpaceType;
    @FXML
    private ComboBox<String> cbStatus;
    @FXML
    private TextField tfSearch;
    @FXML
    private TableColumn<ParkingSpace, Integer> colSpaceId;
    @FXML
    private TextField tfParkingLotId;
    @FXML
    private TextField tfSpaceId;
    @FXML
    private TableView<ParkingSpace> tableSpaces;
    @FXML
    private TableColumn<ParkingSpace, Integer> colSpaceType;
    @FXML
    private TableColumn<ParkingSpace, String> colStatus;
    @FXML
    private TextField tfLotId;
    @FXML
    private TextField tfNumber;
    @FXML
    private ComboBox<SpaceType> cbType;
    @FXML
    private CheckBox cbPreferential;
    @FXML
    private CheckBox cbAvailable;

    private ObservableList<ParkingSpace> masterList = FXCollections.observableArrayList();
    private FilteredList<ParkingSpace> filteredList = new FilteredList<>(masterList, p -> true);

    public ParkingSpaceCrudController(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
    }

    @FXML
    private void initialize() {
        try {
            cbType.getItems().setAll(SpaceType.values());
            cbStatus.getItems().setAll("Disponible", "Ocupado");

            colSpaceType.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("spaceNumber"));
            colStatus.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().isState() ? "Disponible" : "Ocupado"));

            tableSpaces.setItems(filteredList);

            if (tfSearch != null) {
                tfSearch.textProperty().addListener((obs, oldV, newV) -> {
                    String lower = newV == null ? "" : newV.toLowerCase();
                    filteredList.setPredicate(s -> {
                        if (lower.isBlank()) return true;
                        return String.valueOf(s.getSpaceNumber()).contains(lower) || (s.getParkingLot() != null && String.valueOf(s.getParkingLot().getParkingLotId()).contains(lower));
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
        masterList.setAll(mainMenuController.getAllParkingSpaces());
        updateRecordCount();
    }

    private void updateRecordCount() {
        if (lblTotalRecords != null) lblTotalRecords.setText(String.valueOf(filteredList.size()));
    }

    @FXML
    private void onCreate(ActionEvent actionEvent) {
        ParkingLot lot = loadLot();
        Integer number = CrudFormUtils.readInt(tfNumber, "Espacios", "Numero");
        SpaceType type = CrudFormUtils.readSelection(cbType, "Espacios", "Tipo");
        if (lot == null || number == null || type == null) {
            return;
        }
        ParkingSpace space = new ParkingSpace(number, type, cbPreferential.isSelected(), cbAvailable.isSelected());
        space.setParkingLot(lot);
        try {
            CrudAlertHelper.showResult("Espacios", mainMenuController.createParkingSpace(space));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        loadData();
    }

    @FXML
    private void onRead(ActionEvent actionEvent) {
        ParkingLot lot = loadLot();
        Integer number = CrudFormUtils.readInt(tfNumber, "Espacios", "Numero");
        if (lot == null || number == null) {
            return;
        }
        ParkingSpace space = mainMenuController.readParkingSpaceByNumber(number, lot);
        CrudAlertHelper.showEntity("Espacios", space);
    }

    @FXML
    private void onUpdate(ActionEvent actionEvent) {
        ParkingLot lot = loadLot();
        Integer number = CrudFormUtils.readInt(tfNumber, "Espacios", "Numero");
        SpaceType type = CrudFormUtils.readSelection(cbType, "Espacios", "Tipo");
        if (lot == null || number == null || type == null) {
            return;
        }
        ParkingSpace space = new ParkingSpace(number, type, cbPreferential.isSelected(), cbAvailable.isSelected());
        space.setParkingLot(lot);
        try {
            CrudAlertHelper.showResult("Espacios", mainMenuController.updateParkingSpace(space));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        loadData();
    }

    @FXML
    private void onDelete(ActionEvent actionEvent) {
        ParkingLot lot = loadLot();
        Integer number = CrudFormUtils.readInt(tfNumber, "Espacios", "Numero");
        if (lot == null || number == null) {
            return;
        }
        ParkingSpace space = mainMenuController.readParkingSpaceByNumber(number, lot);
        if (space == null) {
            CrudAlertHelper.showWarning("Espacios", "Espacio no encontrado");
            return;
        }
        try {
            CrudAlertHelper.showResult("Espacios", mainMenuController.deleteParkingSpace(space));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        loadData();
    }

    private ParkingLot loadLot() {
        int lotId = Integer.parseInt(CrudFormUtils.readRequired(tfLotId, "Espacios", "Id parqueadero"));
        if (lotId == 0) {
            return null;
        }
        ParkingLot lot = mainMenuController.readParkingLotById(lotId);
        if (lot == null) {
            CrudAlertHelper.showWarning("Espacios", "Parqueadero no encontrado");
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
        if (tfSpaceId != null) tfSpaceId.clear();
        if (tfParkingLotId != null) tfParkingLotId.clear();
        if (tfLotId != null) tfLotId.clear();
        if (tfNumber != null) tfNumber.clear();
        if (cbType != null) cbType.setValue(null);
        if (cbStatus != null) cbStatus.setValue(null);
        if (cbPreferential != null) cbPreferential.setSelected(false);
        if (cbAvailable != null) cbAvailable.setSelected(false);
    }
}
