package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Rate;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.SpaceType;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.VehicleType;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.concurrent.TimeUnit;

public class RateCrudController {

    private final MainMenuController mainMenuController;

    @FXML
    private TextField tfId;
    @FXML
    private TextField tfFee;
    @FXML
    private ComboBox<TimeUnit> cbUnit;
    @FXML
    private ComboBox<SpaceType> cbSpaceType;

    public RateCrudController(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
    }

    @FXML
    private void initialize() {
        cbUnit.getItems().setAll(TimeUnit.values());
        cbSpaceType.getItems().setAll(SpaceType.values());
    }

    @FXML
    private void onCreate() {
        Integer id = CrudFormUtils.readInt(tfId, "Tarifas", "Id");
        Double fee = CrudFormUtils.readDouble(tfFee, "Tarifas", "Valor");
        TimeUnit unit = CrudFormUtils.readSelection(cbUnit, "Tarifas", "Unidad de tiempo");
        SpaceType spaceType = CrudFormUtils.readSelection(cbSpaceType, "Tarifas", "Tipo espacio");
        if (id == null || fee == null || unit == null || spaceType == null) {
            return;
        }
        Rate rate = new Rate(id, defaultVehicleType(fee, spaceType), unit, fee);
        CrudAlertHelper.showResult("Tarifas", mainMenuController.createRate(rate));
    }

    @FXML
    private void onRead() {
        Integer id = CrudFormUtils.readInt(tfId, "Tarifas", "Id");
        if (id == null) {
            return;
        }
        Rate rate = mainMenuController.readRateById(id);
        CrudAlertHelper.showEntity("Tarifas", rate);
    }

    @FXML
    private void onUpdate() {
        Integer id = CrudFormUtils.readInt(tfId, "Tarifas", "Id");
        Double fee = CrudFormUtils.readDouble(tfFee, "Tarifas", "Valor");
        TimeUnit unit = CrudFormUtils.readSelection(cbUnit, "Tarifas", "Unidad de tiempo");
        SpaceType spaceType = CrudFormUtils.readSelection(cbSpaceType, "Tarifas", "Tipo espacio");
        if (id == null || fee == null || unit == null || spaceType == null) {
            return;
        }
        Rate rate = new Rate(id, defaultVehicleType(fee, spaceType), unit, fee);
        CrudAlertHelper.showResult("Tarifas", mainMenuController.updateRate(rate));
    }

    @FXML
    private void onDelete() {
        Integer id = CrudFormUtils.readInt(tfId, "Tarifas", "Id");
        if (id == null) {
            return;
        }
        Rate rate = mainMenuController.readRateById(id);
        if (rate == null) {
            CrudAlertHelper.showWarning("Tarifas", "Tarifa no encontrada");
            return;
        }
        CrudAlertHelper.showResult("Tarifas", mainMenuController.deleteRate(rate));
    }

    private VehicleType defaultVehicleType(double fee, SpaceType spaceType) {
        return new VehicleType(1, "Default", (byte) 4, fee, spaceType);
    }
}
