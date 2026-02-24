package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller.UserController;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.jdom2.JDOMException;

import java.io.IOException;

public class UserCrudControllerImproved {

    private final MainMenuController mainMenuController;
    private UserController userController;
    private ObservableList<User> userList;
    private ObservableList<User> filteredList;

    @FXML
    private TextField tfId;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private ComboBox<UserRole> cbRole;
    @FXML
    private TextField tfSearch;
    @FXML
    private TableView<User> tableUsers;
    @FXML
    private TableColumn<User, Integer> colId;
    @FXML
    private TableColumn<User, String> colName;
    @FXML
    private TableColumn<User, String> colUsername;
    @FXML
    private TableColumn<User, UserRole> colRole;
    @FXML
    private Label lblTotalRecords;

    public UserCrudControllerImproved(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
    }

    @FXML
    private void initialize() {
        try {
            this.userController = new UserController();
        } catch (IOException | JDOMException e) {
            throw new RuntimeException(e);
        }
        // Configurar ComboBox de roles
        cbRole.getItems().setAll(UserRole.values());
        
        // Configurar columnas de la tabla
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("userName")); // Corregido
        colRole.setCellValueFactory(new PropertyValueFactory<>("userRole")); // Corregido

        // Listener para selección en tabla
        tableUsers.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadUserToForm(newSelection);
            }
        });
        
        // Listener para búsqueda
        tfSearch.textProperty().addListener((obs, oldValue, newValue) -> {
            filterTable(newValue);
        });
        
        // Cargar datos iniciales
        loadData();
    }

    @FXML
    private void onCreate() {
        try {
            Integer id = Integer.parseInt(tfId.getText().trim());
            String name = tfName.getText().trim();
            String username = tfUsername.getText().trim();
            String password = tfPassword.getText().trim();
            UserRole role = cbRole.getValue();

            if (name.isEmpty() || username.isEmpty() || password.isEmpty() || role == null) {
                showAlert("Error", "Todos los campos son obligatorios", Alert.AlertType.ERROR);
                return;
            }

            User user = role == UserRole.ADMIN
                    ? new Administrator(id, name, username, password)
                    : new Clerk(id, name, username, password);

            OperationResult result = mainMenuController.createUser(user);
            
            if (result.isSuccessfull()) {
                showAlert("Éxito", "Usuario creado correctamente", Alert.AlertType.INFORMATION);
                onClear();
                loadData();
            } else {
                showAlert("Error", result.getMessage(), Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "El ID debe ser un número válido", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Error al crear usuario: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onUpdate() {
        try {
            Integer id = Integer.parseInt(tfId.getText().trim());
            String name = tfName.getText().trim();
            String username = tfUsername.getText().trim();
            String password = tfPassword.getText().trim();
            UserRole role = cbRole.getValue();

            if (name.isEmpty() || username.isEmpty() || password.isEmpty() || role == null) {
                showAlert("Error", "Todos los campos son obligatorios", Alert.AlertType.ERROR);
                return;
            }

            User user = role == UserRole.ADMIN
                    ? new Administrator(id, name, username, password)
                    : new Clerk(id, name, username, password);

            OperationResult result = mainMenuController.updateUser(user);
            
            if (result.isSuccessfull()) {
                showAlert("Éxito", "Usuario actualizado correctamente", Alert.AlertType.INFORMATION);
                onClear();
                loadData();
            } else {
                showAlert("Error", result.getMessage(), Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "El ID debe ser un número válido", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Error al actualizar usuario: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onDelete() {
        try {
            Integer id = Integer.parseInt(tfId.getText().trim());
            User user = mainMenuController.readUserById(id);
            
            if (user == null) {
                showAlert("Error", "Usuario no encontrado", Alert.AlertType.ERROR);
                return;
            }

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmar eliminación");
            confirmAlert.setHeaderText("¿Está seguro de eliminar este usuario?");
            confirmAlert.setContentText("Usuario: " + user.getName());
            
            if (confirmAlert.showAndWait().get() == ButtonType.OK) {
                OperationResult result = mainMenuController.deleteUser(user);
                
                if (result.isSuccessfull()) {
                    showAlert("Éxito", "Usuario eliminado correctamente", Alert.AlertType.INFORMATION);
                    onClear();
                    loadData();
                } else {
                    showAlert("Error", result.getMessage(), Alert.AlertType.ERROR);
                }
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "El ID debe ser un número válido", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Error al eliminar usuario: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onClear() {
        tfId.clear();
        tfName.clear();
        tfUsername.clear();
        tfPassword.clear();
        cbRole.setValue(null);
        tableUsers.getSelectionModel().clearSelection();
    }

    @FXML
    private void onRefresh() {
        loadData();
    }

    @FXML
    private void goBack() {
        Stage stage = (Stage) tfId.getScene().getWindow();
        stage.close();
    }

    private void loadData() {
        try {
            userList = FXCollections.observableArrayList(userController.getAllUsers());
            filteredList = FXCollections.observableArrayList(userList);
            tableUsers.setItems(filteredList);
            updateRecordCount();
        } catch (Exception e) {
            showAlert("Error", "Error al cargar usuarios: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void filterTable(String query) {
        if (query == null || query.trim().isEmpty()) {
            filteredList.setAll(userList);
        } else {
            String lowerQuery = query.toLowerCase();
            filteredList.setAll(userList.stream()
                    .filter(user -> 
                        String.valueOf(user.getId()).contains(lowerQuery) ||
                        user.getName().toLowerCase().contains(lowerQuery) ||
                        user.getUserName().toLowerCase().contains(lowerQuery) ||
                        user.getUserRole().toString().toLowerCase().contains(lowerQuery))
                    .toList());
        }
        updateRecordCount();
    }

    private void loadUserToForm(User user) {
        tfId.setText(String.valueOf(user.getId()));
        tfName.setText(user.getName());
        tfUsername.setText(user.getUserName());
        tfPassword.setText(user.getPassword());
        cbRole.setValue(user.getUserRole());
    }

    private void updateRecordCount() {
        lblTotalRecords.setText(String.valueOf(filteredList.size()));
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
