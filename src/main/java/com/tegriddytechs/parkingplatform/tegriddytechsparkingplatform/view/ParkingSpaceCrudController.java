package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingLot;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingSpace;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.SpaceType;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class ParkingSpaceCrudController {

    private final MainMenuController mainMenuController;

    @FXML
    private TextField tfLotId;
    @FXML
    private TextField tfNumber;
    @FXML
    private ComboBox<SpaceType> cbType;
    @FXML
    private CheckBox cbPreferential;
    @FXML
    private CheckBox cbAvailable;

    public ParkingSpaceCrudController(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
    }

    @FXML
    private void initialize() {
        cbType.getItems().setAll(SpaceType.values());
    }

    @FXML
    private void onCreate() {
        ParkingLot lot = loadLot();
        Integer number = CrudFormUtils.readInt(tfNumber, "Espacios", "Numero");
        SpaceType type = CrudFormUtils.readSelection(cbType, "Espacios", "Tipo");
        if (lot == null || number == null || type == null) {
            return;
        }
        ParkingSpace space = new ParkingSpace(number, type, cbPreferential.isSelected(), cbAvailable.isSelected());
        space.setParkingLot(lot);
        CrudAlertHelper.showResult("Espacios", mainMenuController.createParkingSpace(space));
    }

    @FXML
    private void onRead() {
        ParkingLot lot = loadLot();
        Integer number = CrudFormUtils.readInt(tfNumber, "Espacios", "Numero");
        if (lot == null || number == null) {
            return;
        }
        ParkingSpace space = mainMenuController.readParkingSpaceByNumber(number, lot);
        CrudAlertHelper.showEntity("Espacios", space);
    }

    @FXML
    private void onUpdate() {
        ParkingLot lot = loadLot();
        Integer number = CrudFormUtils.readInt(tfNumber, "Espacios", "Numero");
        SpaceType type = CrudFormUtils.readSelection(cbType, "Espacios", "Tipo");
        if (lot == null || number == null || type == null) {
            return;
        }
        ParkingSpace space = new ParkingSpace(number, type, cbPreferential.isSelected(), cbAvailable.isSelected());
        space.setParkingLot(lot);
        CrudAlertHelper.showResult("Espacios", mainMenuController.updateParkingSpace(space));
    }

    @FXML
    private void onDelete() {
        ParkingLot lot = loadLot();
        Integer number = CrudFormUtils.readInt(tfNumber, "Espacios", "Numero");
        if (lot == null || number == null) {
            return;
        }
        ParkingSpace space = mainMenuController.readParkingSpaceByNumber(number, lot);
        if (space == null) {
            CrudAlertHelper.showWarning("Espacios", "Espacio no encontrado");
            return;
        }
        CrudAlertHelper.showResult("Espacios", mainMenuController.deleteParkingSpace(space));
    }

    private ParkingLot loadLot() {
        String lotId = CrudFormUtils.readRequired(tfLotId, "Espacios", "Id parqueadero");
        if (lotId == null) {
            return null;
        }
        ParkingLot lot = mainMenuController.readParkingLotById(lotId);
        if (lot == null) {
            CrudAlertHelper.showWarning("Espacios", "Parqueadero no encontrado");
        }
        return lot;
    }
}
