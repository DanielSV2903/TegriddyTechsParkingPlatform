package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.UserData;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Administrator;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Clerk;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.User;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.UserRole;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class TegriddyTechsParkingPlatformController {

    @FXML
    private TextField tfPassword;
    @FXML
    private TextField tFid;
    @FXML
    private TextField tfUserName;
    @FXML
    private TextField tfName;
    @FXML
    private ComboBox<UserRole> cbRole;
    private UserData userData;
    @FXML
    private void initialize() {
        userData = new UserData();
        cbRole.getItems().setAll(UserRole.values());
    }
    public void insertOnAction(ActionEvent actionEvent) {
        int id = Integer.parseInt(tFid.getText());
        String userName = tfUserName.getText();
        String password = tfPassword.getText();
        String name = tfName.getText();
        UserRole role = cbRole.getValue();

        User user = null;
        switch (role) {
            case ADMIN:
                user = new Administrator(id, name, userName, password);
                break;
            case CLERK:
                user=new Clerk(id, name, userName, password );
                    break;
        }
        userData.addUser(user);

        System.out.println("ID: " + id);
        System.out.println("Username: " + userName);
        System.out.println("Password: " + password);
        System.out.println("Name: " + name);
        System.out.println("Role: " + role);
        System.out.println("User added successfully");
        System.out.println(userData.getAllUsers());
    }
}
