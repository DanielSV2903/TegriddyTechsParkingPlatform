package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;


import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Administrator;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AdminCrudController {

    private final MainMenuController mainMenuController;
    @FXML
    private TextField tfId;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfUsername;
    @FXML
    private TextField tfPassword;

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
}
