package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;


import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Administrator;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Clerk;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class AdminCrudController {

    private final MainMenuController mainMenuController;
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
    private TableView<Administrator> tableAdmins;
    @FXML
    private TextField tfSearch;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colUsername;

    public AdminCrudController(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
    }

    @FXML
    private void initialize() {
        updateRecordCount();
        fillTable();
    }

    private void fillTable() {
        setUpTableColumns();
        List<Administrator> clerks=mainMenuController.getUserController().getAdmins();
        tableAdmins.setItems(FXCollections.observableArrayList(clerks));
        updateRecordCount();
    }

    private void updateRecordCount() {
        tfId.setText(String.valueOf(mainMenuController.getUserController().geNextAdminIDByCount()));
        if (lblTotalRecords != null) {
            lblTotalRecords.setText("Total: " + mainMenuController.getUserController().getAdmins().size() + " registros");
        }
    }

    private void setUpTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("userName"));
    }

    @FXML
    private void onCreate() {
        Integer id = CrudFormUtils.readInt(tfId, "Administradores", "Id");
        String name = CrudFormUtils.readRequired(tfName, "Administradores", "Nombre");
        String username = CrudFormUtils.readRequired(tfUsername, "Administradores", "Usuario");
        String password = CrudFormUtils.readRequired(tfPassword, "Administradores", "Contrasena");
        if (id == null || name == null || username == null || password == null) {
            return;
        }
        Administrator admin = new Administrator(id, name, username, password);
        CrudAlertHelper.showResult("Administradores", mainMenuController.createAdministrator(admin));
        updateRecordCount();
    }

    @FXML
    private void onRead() {
        Integer id = CrudFormUtils.readInt(tfId, "Administradores", "Id");
        if (id == null) {
            return;
        }
        Administrator admin = mainMenuController.readAdministratorById(id);
        CrudAlertHelper.showEntity("Administradores", admin);
    }

    @FXML
    private void onUpdate() {
        Integer id = CrudFormUtils.readInt(tfId, "Administradores", "Id");
        String name = CrudFormUtils.readRequired(tfName, "Administradores", "Nombre");
        String username = CrudFormUtils.readRequired(tfUsername, "Administradores", "Usuario");
        String password = CrudFormUtils.readRequired(tfPassword, "Administradores", "Contrasena");
        if (id == null || name == null || username == null || password == null) {
            return;
        }
        Administrator admin = new Administrator(id, name, username, password);
        CrudAlertHelper.showResult("Administradores", mainMenuController.updateAdministrator(admin));
        initialize();
    }

    @FXML
    private void onDelete() {
        Integer id = CrudFormUtils.readInt(tfId, "Administradores", "Id");
        if (id == null) {
            return;
        }
        Administrator admin = mainMenuController.readAdministratorById(id);
        if (admin == null) {
            CrudAlertHelper.showWarning("Administradores", "Administrador no encontrado");
            return;
        }
        CrudAlertHelper.showResult("Administradores", mainMenuController.deleteAdministrator(admin));
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
    private void filterTable() {
        String text = tfSearch.getText();
        List<Administrator> administrators = mainMenuController.getUserController().getAdmins();
        if (text != null && !text.isEmpty()) {
            administrators = administrators.stream()
                    .filter(c -> String.valueOf(c.getId()).contains(text)
                            || c.getName().toLowerCase().contains(text.toLowerCase())
                            || c.getUserName().toLowerCase().contains(text.toLowerCase()))
                    .toList();
        }
        tableAdmins.setItems(FXCollections.observableArrayList(administrators));
        updateRecordCount();
        tfSearch.clear();
    }
    private void fillFields() {
        Administrator administrator= tableAdmins.getSelectionModel().getSelectedItem();
        tfId.setText(String.valueOf(administrator.getId()));
        tfName.setText(administrator.getName());
        tfUsername.setText(administrator.getUserName());
        tfPassword.setText(administrator.getPassword());
    }

    @FXML
    public void selectClerkOnMouseClicked(Event event) {
        fillFields();
    }
}
