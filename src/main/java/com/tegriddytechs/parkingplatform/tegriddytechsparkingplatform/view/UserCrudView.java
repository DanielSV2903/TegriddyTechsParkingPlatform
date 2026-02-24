package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Administrator;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Clerk;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.User;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.UserRole;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class UserCrudView {

    private final MainMenuView mainMenuView;

    @FXML
    private TextField tfId;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfUsername;
    @FXML
    private TextField tfPassword;
    @FXML
    private ComboBox<UserRole> cbRole;
    @FXML
    private TextField tfSearch;
    @FXML
    private TableView tableUsers;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colName;
    @FXML
    private TableColumn colUsername;
    @FXML
    private Label lblTotalRecords;

    public UserCrudView(MainMenuView mainMenuView) {
        this.mainMenuView = mainMenuView;
    }

    @FXML
    private void initialize() {
        cbRole.getItems().setAll(UserRole.values());
    }

    @FXML
    private void onCreate() {
        Integer id = CrudFormUtils.readInt(tfId, "Usuarios", "Id");
        String name = CrudFormUtils.readRequired(tfName, "Usuarios", "Nombre");
        String username = CrudFormUtils.readRequired(tfUsername, "Usuarios", "Usuario");
        String password = CrudFormUtils.readRequired(tfPassword, "Usuarios", "Contrasena");
        UserRole role = CrudFormUtils.readSelection(cbRole, "Usuarios", "Rol");
        if (id == null || name == null || username == null || password == null || role == null) {
            return;
        }
        User user = role == UserRole.ADMIN
                ? new Administrator(id, name, username, password)
                : new Clerk(id, name, username, password);
        CrudAlertHelper.showResult("Usuarios", mainMenuView.createUser(user));
    }

    @FXML
    private void onRead() {
        Integer id = CrudFormUtils.readInt(tfId, "Usuarios", "Id");
        if (id == null) {
            return;
        }
        User user = mainMenuView.readUserById(id);
        CrudAlertHelper.showEntity("Usuarios", user);
    }

    @FXML
    private void onUpdate() {
        Integer id = CrudFormUtils.readInt(tfId, "Usuarios", "Id");
        String name = CrudFormUtils.readRequired(tfName, "Usuarios", "Nombre");
        String username = CrudFormUtils.readRequired(tfUsername, "Usuarios", "Usuario");
        String password = CrudFormUtils.readRequired(tfPassword, "Usuarios", "Contrasena");
        UserRole role = CrudFormUtils.readSelection(cbRole, "Usuarios", "Rol");
        if (id == null || name == null || username == null || password == null || role == null) {
            return;
        }
        User user = role == UserRole.ADMIN
                ? new Administrator(id, name, username, password)
                : new Clerk(id, name, username, password);
        CrudAlertHelper.showResult("Usuarios", mainMenuView.updateUser(user));
    }

    @FXML
    private void onDelete() {
        Integer id = CrudFormUtils.readInt(tfId, "Usuarios", "Id");
        if (id == null) {
            return;
        }
        User user = mainMenuView.readUserById(id);
        if (user == null) {
            CrudAlertHelper.showWarning("Usuarios", "Usuario no encontrado");
            return;
        }
        CrudAlertHelper.showResult("Usuarios", mainMenuView.deleteUser(user));
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
        if (cbRole != null) cbRole.setValue(null);
        if (tfSearch != null) tfSearch.clear();
        if (tableUsers != null) tableUsers.getItems().clear();
    }
}
