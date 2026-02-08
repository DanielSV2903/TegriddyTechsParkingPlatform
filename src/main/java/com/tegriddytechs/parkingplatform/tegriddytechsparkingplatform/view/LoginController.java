package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view.TegriddyTechsParkingPlatformApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblMessage;

    @FXML
    private void login(ActionEvent event) {
        String user = txtUsername.getText();
        String pass = txtPassword.getText();

        if ("admin".equals(user) && "admin".equals(pass)) {
            loadMenu(event);
        } else {
            lblMessage.setText("Usuario o contraseña incorrectos");
        }
    }

    private void loadMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    TegriddyTechsParkingPlatformApp.class.getResource(
                            "/com/tegriddytechs/parkingplatform/tegriddytechsparkingplatform/menu-view.fxml"
                    )
            );

            Parent root = loader.load();
            Node node = (Node) event.getSource();
            node.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
