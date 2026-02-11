package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Clerk;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingLot;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ParkingLotCrudController {

    private final MainMenuController mainMenuController;

    @FXML
    private TextField tfId;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfAddress;
    @FXML
    private TextField tfCapacity;
    @FXML
    private TextField tfSearch;
    @FXML
    private TableView<ParkingLot> tableParkingLots;
    @FXML
    private TableColumn<ParkingLot, String> colId;
    @FXML
    private TableColumn<ParkingLot, String> colName;
    @FXML
    private TableColumn<ParkingLot, String> colAddress;
    @FXML
    private TableColumn<ParkingLot, Integer> colCapacity;
    @FXML
    private Label lblTotalRecords;

    private ObservableList<ParkingLot> masterList = FXCollections.observableArrayList();
    private FilteredList<ParkingLot> filteredList = new FilteredList<>(masterList, p -> true);

    public ParkingLotCrudController(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
    }

    @FXML
    private void initialize() {
        try {
            colId.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("parkingLotId"));
            colName.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("name"));
            colAddress.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("address"));
            colCapacity.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("capacity"));

            tableParkingLots.setItems(filteredList);

            if (tfSearch != null) {
                tfSearch.textProperty().addListener((obs, oldV, newV) -> {
                    String lower = newV == null ? "" : newV.toLowerCase();
                    filteredList.setPredicate(l -> {
                        if (lower.isBlank()) return true;
                        return (l.getParkingLotId() != null && l.getParkingLotId().toLowerCase().contains(lower))
                                || (l.getName() != null && l.getName().toLowerCase().contains(lower));
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
        masterList.setAll(mainMenuController.getAllParkingLots());
        updateRecordCount();
    }

    private void updateRecordCount() {
        if (lblTotalRecords != null) lblTotalRecords.setText(String.valueOf(filteredList.size()));
    }

    @FXML
    private void onCreate(ActionEvent actionEvent) {
        String id = CrudFormUtils.readRequired(tfId, "Parqueaderos", "Id");
        String name = CrudFormUtils.readRequired(tfName, "Parqueaderos", "Nombre");
        if (id == null || name == null) {
            return;
        }
        ParkingLot lot = new ParkingLot(id, name);
//        lot.setActive(cbActive.isSelected());
        CrudAlertHelper.showResult("Parqueaderos", mainMenuController.createParkingLot(lot));
    }

    @Deprecated
    private void onRead(ActionEvent actionEvent) {
        String id = CrudFormUtils.readRequired(tfId, "Parqueaderos", "Id");
        if (id == null) {
            return;
        }
        ParkingLot lot = mainMenuController.readParkingLotById(id);
        CrudAlertHelper.showEntity("Parqueaderos", lot);
    }

    @FXML
    private void onUpdate(ActionEvent actionEvent) {
        String id = CrudFormUtils.readRequired(tfId, "Parqueaderos", "Id");
        String name = CrudFormUtils.readRequired(tfName, "Parqueaderos", "Nombre");
        if (id == null || name == null) {
            return;
        }
        ParkingLot lot = new ParkingLot(id, name);
//        lot.setActive(cbActive.isSelected());
        CrudAlertHelper.showResult("Parqueaderos", mainMenuController.updateParkingLot(lot));
    }

    @FXML
    private void onDelete(ActionEvent actionEvent) {
        String id = CrudFormUtils.readRequired(tfId, "Parqueaderos", "Id");
        if (id == null) {
            return;
        }
        ParkingLot lot = mainMenuController.readParkingLotById(id);
        if (lot == null) {
            CrudAlertHelper.showWarning("Parqueaderos", "Parqueadero no encontrado");
            return;
        }
        CrudAlertHelper.showResult("Parqueaderos", mainMenuController.deleteParkingLot(lot));
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
        if (tfId != null) tfId.clear();
        if (tfName != null) tfName.clear();
        if (tfAddress != null) tfAddress.clear();
        if (tfCapacity != null) tfCapacity.clear();
//        if (cbActive != null) cbActive.setSelected(false);
    }

    private void fillFields() {
        ParkingLot parkingLot= tableParkingLots.getSelectionModel().getSelectedItem();
        tfId.setText(String.valueOf(parkingLot.getParkingLotId()));
        tfName.setText(parkingLot.getName());
        tfCapacity.setText(String.valueOf(parkingLot.getSpaces().size()));
    }

    @FXML
    public void selectParkingLotOnMouseClicked(Event event) {
        fillFields();
    }
}
