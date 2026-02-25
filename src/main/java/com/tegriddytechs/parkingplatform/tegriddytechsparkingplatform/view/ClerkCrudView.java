package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Clerk;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingLot;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class ClerkCrudView {

    private final MainMenuView mainMenuView;

    @FXML
    private TextField tfId;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private TableColumn colName;
    @FXML
    private Label lblTotalRecords;
    @FXML
    private TextField tfSearch;
    @FXML
    private TableColumn colId;
    @FXML
    private TableView<Clerk> tableClerks;
    @FXML
    private TableColumn colUsername;
    @FXML
    private ComboBox<ParkingLot> cbParkingLot;

    public ClerkCrudView(MainMenuView mainMenuView) {
        this.mainMenuView = mainMenuView;
    }

    @FXML
    private void initialize() {
        updateRecordCount();
        fillTable();
        setUpComboBoxParkingLot();
        cbParkingLot.setItems(FXCollections.observableArrayList(mainMenuView.getAllParkingLots()));
    }

    private void setUpComboBoxParkingLot() {
        this.cbParkingLot.setCellFactory(cellData-> new ListCell<>() {
            @Override
            protected void updateItem(ParkingLot item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getName());
            }

        });
        this.cbParkingLot.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(ParkingLot item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Seleccione un parqueo" : item.getName());
            }
        });
    }

    private void fillTable() {
        setUpTableColumns();
        List<Clerk> clerks= mainMenuView.getUserController().getClerks();
        tableClerks.setItems(FXCollections.observableArrayList(clerks));
       updateRecordCount();
//        tableClerks.refresh();
    }

    private void updateRecordCount() {
        tfId.setText(String.valueOf(mainMenuView.getUserController().getNextClerkIDByCount()));
        if (lblTotalRecords != null) {
            lblTotalRecords.setText("Total: " + mainMenuView.getUserController().getClerks().size() + " registros");
        }
    }

    private void setUpTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("userName"));
    }

    @FXML
    private void onCreate() {
        Integer id = CrudFormUtils.readInt(tfId, "Cajeros", "Id");
        String name = CrudFormUtils.readRequired(tfName, "Cajeros", "Nombre");
        String username = CrudFormUtils.readRequired(tfUsername, "Cajeros", "Usuario");
        String password = CrudFormUtils.readRequired(tfPassword, "Cajeros", "Contrasena");
        if (id == null || name == null || username == null || password == null) {
            return;
        }

        ParkingLot selectedLot = cbParkingLot != null ? cbParkingLot.getSelectionModel().getSelectedItem() : null;
        if (selectedLot == null) {
            CrudAlertHelper.showWarning("Cajeros", "Seleccione el parqueo asignado para el cajero.");
            return;
        }

        Clerk clerk = new Clerk(id, name, username, password);
        clerk.setParkingLot(selectedLot);
        CrudAlertHelper.showResult("Cajeros", mainMenuView.createClerk(clerk));
        updateRecordCount();
    }

    @FXML
    private void onRead() {
        Integer id = CrudFormUtils.readInt(tfId, "Cajeros", "Id");
        if (id == null) {
            return;
        }
        Clerk clerk = mainMenuView.readClerkById(id);
        CrudAlertHelper.showEntity("Cajeros", clerk);
    }

    @FXML
    private void onUpdate() {
        Integer id = CrudFormUtils.readInt(tfId, "Cajeros", "Id");
        String name = CrudFormUtils.readRequired(tfName, "Cajeros", "Nombre");
        String username = CrudFormUtils.readRequired(tfUsername, "Cajeros", "Usuario");
        String password = CrudFormUtils.readRequired(tfPassword, "Cajeros", "Contrasena");
        if (id == null || name == null || username == null || password == null) {
            return;
        }
        ParkingLot selectedLot = cbParkingLot != null ? cbParkingLot.getSelectionModel().getSelectedItem() : null;
        if (selectedLot == null) {
            CrudAlertHelper.showWarning("Cajeros", "Seleccione el parqueo asignado para el cajero.");
            return;
        }
        Clerk clerk = new Clerk(id, name, username, password);
        clerk.setParkingLot(selectedLot);
        CrudAlertHelper.showResult("Cajeros", mainMenuView.updateClerk(clerk));
        initialize();
    }


    @FXML
    private void onDelete() {
        Integer id = CrudFormUtils.readInt(tfId, "Cajeros", "Id");
        if (id == null) {
            return;
        }
        Clerk clerk = mainMenuView.readClerkById(id);
        if (clerk == null) {
            CrudAlertHelper.showWarning("Cajeros", "Cajero no encontrado");
            return;
        }
        CrudAlertHelper.showResult("Cajeros", mainMenuView.deleteClerk(clerk));
        initialize();
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
        filterTable();
    }

    @FXML
    public void onClear(ActionEvent actionEvent) {
        if (tfId != null) tfId.clear();
        if (tfName != null) tfName.clear();
        if (tfUsername != null) tfUsername.clear();
        if (tfPassword != null) tfPassword.clear();
        if (tfSearch != null) tfSearch.clear();
        initialize();
    }

    @FXML
    public void selectClerkOnMouseClicked(Event event) {
        fillFields();
    }

    private void filterTable() {
        String text = tfSearch.getText();
        List<Clerk> clerks = mainMenuView.getUserController().getClerks();
        if (text != null && !text.isEmpty()) {
            clerks = clerks.stream()
                    .filter(c -> String.valueOf(c.getId()).contains(text)
                            || c.getName().toLowerCase().contains(text.toLowerCase())
                            || c.getUserName().toLowerCase().contains(text.toLowerCase()))
                    .toList();
        }
        tableClerks.setItems(FXCollections.observableArrayList(clerks));
        updateRecordCount();
        tfSearch.clear();
    }
    private void fillFields() {
        Clerk clerk= tableClerks.getSelectionModel().getSelectedItem();
        tfId.setText(String.valueOf(clerk.getId()));
        tfName.setText(clerk.getName());
        tfUsername.setText(clerk.getUserName());
        tfPassword.setText(clerk.getPassword());
        cbParkingLot.setValue(clerk.getParkingLot());
    }
}
