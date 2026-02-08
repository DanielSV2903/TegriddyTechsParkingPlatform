package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.UserXmlRepository;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.User;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.UserRole;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class LoginController {

    public LoginController() {
    }

    private UserXmlRepository userXmlRepository;
    private List<User> users;
    private User actualUser;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblMessage;

    @FXML
    private void login(ActionEvent event) {
        userXmlRepository = new UserXmlRepository();
        users = userXmlRepository.loadAll();
        String username = txtUsername.getText();
        String pass = txtPassword.getText();
        Object[] userInfo = userExists(username);

        if (userInfo[0].equals(true)) {
            User user = (User) userInfo[1];
            if (passwordMatches(user, pass)){
                actualUser = user;
                JOptionPane.showMessageDialog(null, "Bienvenido " + user.getName(), "Bienvenido", JOptionPane.INFORMATION_MESSAGE);
                loadMenu(event, getUserRole(username));
            } else {
                JOptionPane.showMessageDialog(null, "Contraseña incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Usuario inexistente", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private UserRole getUserRole(String user) {
        UserRole role = null;
        for (User u : users) {
            if (u.getUserName().equals(user)){
                role = u.getUserRole();
            }
        }
        return role;
    }

    private void loadMenu(ActionEvent event, UserRole role) {
        try {
            FXMLLoader loader;
            if (role == UserRole.ADMIN){
                loader = new FXMLLoader(
                        TegriddyTechsParkingPlatformApp.class.getResource(
                                "/com/tegriddytechs/parkingplatform/tegriddytechsparkingplatform/menu-view.fxml"
                        )
                );
            } else {
                loader = new FXMLLoader(
                        TegriddyTechsParkingPlatformApp.class.getResource(
                                "/com/tegriddytechs/parkingplatform/tegriddytechsparkingplatform/clerk-view.fxml"
                        )
                );
            }

            Parent root = loader.load();
            Node node = (Node) event.getSource();
            node.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Object[] userExists(String user) {
        Object[] obj = new Object[2];
        for (User u : users) {
            if (u.getUserName().equals(user)) {
                obj[0] = true;
                obj[1] = u;
                break;
            } else {
                obj[0] = false;
            }
        }
        return obj;
    }

    private boolean passwordMatches(User user, String pass) {
        return user.getPassword().equals(pass);
    }

    public User getActualUser() {
        return actualUser;
    }
}
