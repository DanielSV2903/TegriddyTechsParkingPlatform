package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TegriddyTechsParkingPlatformController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("El sistema de parqueo de Tegriddy Techs está en construcción.");
    }
}
