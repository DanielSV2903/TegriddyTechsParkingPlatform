package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;


import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Administrator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

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
    private TableView tableAdmins;
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
    }

    @FXML
    public void onClear(ActionEvent actionEvent) {
        if (tfId != null) tfId.clear();
        if (tfName != null) tfName.clear();
        if (tfUsername != null) tfUsername.clear();
        if (tfPassword != null) tfPassword.clear();
        if (tfSearch != null) tfSearch.clear();
        if (tableAdmins != null) tableAdmins.getItems().clear();
    }
}
