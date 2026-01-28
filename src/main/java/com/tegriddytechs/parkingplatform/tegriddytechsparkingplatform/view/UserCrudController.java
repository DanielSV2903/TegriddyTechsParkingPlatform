package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Administrator;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Clerk;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.User;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.UserRole;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class UserCrudController {

    private final MainMenuController mainMenuController;

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

    public UserCrudController(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
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
        CrudAlertHelper.showResult("Usuarios", mainMenuController.createUser(user));
    }

    @FXML
    private void onRead() {
        Integer id = CrudFormUtils.readInt(tfId, "Usuarios", "Id");
        if (id == null) {
            return;
        }
        User user = mainMenuController.readUserById(id);
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
        CrudAlertHelper.showResult("Usuarios", mainMenuController.updateUser(user));
    }

    @FXML
    private void onDelete() {
        Integer id = CrudFormUtils.readInt(tfId, "Usuarios", "Id");
        if (id == null) {
            return;
        }
        User user = mainMenuController.readUserById(id);
        if (user == null) {
            CrudAlertHelper.showWarning("Usuarios", "Usuario no encontrado");
            return;
        }
        CrudAlertHelper.showResult("Usuarios", mainMenuController.deleteUser(user));
    }
}
