package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Clerk;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ClerkCrudController {

    private final MainMenuController mainMenuController;

    @FXML
    private TextField tfId;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfUsername;
    @FXML
    private TextField tfPassword;

    public ClerkCrudController(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
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
        Clerk clerk = new Clerk(id, name, username, password);
        CrudAlertHelper.showResult("Cajeros", mainMenuController.createClerk(clerk));
    }

    @FXML
    private void onRead() {
        Integer id = CrudFormUtils.readInt(tfId, "Cajeros", "Id");
        if (id == null) {
            return;
        }
        Clerk clerk = mainMenuController.readClerkById(id);
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
        Clerk clerk = new Clerk(id, name, username, password);
        CrudAlertHelper.showResult("Cajeros", mainMenuController.updateClerk(clerk));
    }

    @FXML
    private void onDelete() {
        Integer id = CrudFormUtils.readInt(tfId, "Cajeros", "Id");
        if (id == null) {
            return;
        }
        Clerk clerk = mainMenuController.readClerkById(id);
        if (clerk == null) {
            CrudAlertHelper.showWarning("Cajeros", "Cajero no encontrado");
            return;
        }
        CrudAlertHelper.showResult("Cajeros", mainMenuController.deleteClerk(clerk));
    }
}
