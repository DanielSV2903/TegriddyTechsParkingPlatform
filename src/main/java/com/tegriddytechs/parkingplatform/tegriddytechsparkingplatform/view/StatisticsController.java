package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingLot;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingTicket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class StatisticsController {
    @FXML
    private Label lblPeakHourDetail;
    @FXML
    private TableColumn colKey;
    @FXML
    private Label lblMostRotatedSpace;
    @FXML
    private Label lblTotalRows;
    @FXML
    private Label lblTopClient;
    @FXML
    private TableColumn colExtra;
    @FXML
    private TableColumn colValue;
    @FXML
    private Label lblPeakHour;
    @FXML
    private Label lblTopClientDetail;
    @FXML
    private TableView tableDetail;
    @FXML
    private TextField tfSearch;
    @FXML
    private Label lblMostRotatedSpaceDetail;

    private MainMenuController mainMenuController;
    private ObservableList<ParkingLot> masterList = FXCollections.observableArrayList();
    @FXML
    private ComboBox cbParkingLot;

    public StatisticsController(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
    }

    @javafx.fxml.FXML
    private void initialize() {
        loadData();
    }

    private void loadData() {
        masterList.setAll(mainMenuController.getAllParkingLots());
        cbParkingLot.getItems().setAll(masterList);
        updateRecordCount();
    }

    private void updateRecordCount() {

    }

    @javafx.fxml.FXML
    public void goBack(ActionEvent actionEvent) {
        if (actionEvent != null && actionEvent.getSource() instanceof Node node) {
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
        }
    }

    @javafx.fxml.FXML
    public void onRefresh(ActionEvent actionEvent) {
        loadData();
    }
}
