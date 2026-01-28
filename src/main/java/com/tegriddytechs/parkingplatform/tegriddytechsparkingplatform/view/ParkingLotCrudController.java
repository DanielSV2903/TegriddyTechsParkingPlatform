package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingLot;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class ParkingLotCrudController {

    private final MainMenuController mainMenuController;

    @FXML
    private TextField tfId;
    @FXML
    private TextField tfName;
    @FXML
    private CheckBox cbActive;

    public ParkingLotCrudController(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
    }

    @FXML
    private void onCreate() {
        String id = CrudFormUtils.readRequired(tfId, "Parqueaderos", "Id");
        String name = CrudFormUtils.readRequired(tfName, "Parqueaderos", "Nombre");
        if (id == null || name == null) {
            return;
        }
        ParkingLot lot = new ParkingLot(id, name);
        lot.setActive(cbActive.isSelected());
        CrudAlertHelper.showResult("Parqueaderos", mainMenuController.createParkingLot(lot));
    }

    @FXML
    private void onRead() {
        String id = CrudFormUtils.readRequired(tfId, "Parqueaderos", "Id");
        if (id == null) {
            return;
        }
        ParkingLot lot = mainMenuController.readParkingLotById(id);
        CrudAlertHelper.showEntity("Parqueaderos", lot);
    }

    @FXML
    private void onUpdate() {
        String id = CrudFormUtils.readRequired(tfId, "Parqueaderos", "Id");
        String name = CrudFormUtils.readRequired(tfName, "Parqueaderos", "Nombre");
        if (id == null || name == null) {
            return;
        }
        ParkingLot lot = new ParkingLot(id, name);
        lot.setActive(cbActive.isSelected());
        CrudAlertHelper.showResult("Parqueaderos", mainMenuController.updateParkingLot(lot));
    }

    @FXML
    private void onDelete() {
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
}
