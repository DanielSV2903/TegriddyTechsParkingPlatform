package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Administrator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.stage.Stage;

import java.util.List;

public class AdminSelectDialogController {

    @FXML
    private ComboBox<Administrator> cbAdmins;

    private Administrator selected;

    @FXML
    private void initialize() {
        // Para mostrar algo legible en el ComboBox (si Administrator no tiene toString())
        cbAdmins.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Administrator item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName() + " (" + item.getUserName() + ")");
            }
        });
        cbAdmins.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Administrator item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName() + " (" + item.getUserName() + ")");
            }
        });
    }

    public void setAdministrators(List<Administrator> administrators) {
        cbAdmins.setItems(FXCollections.observableArrayList(administrators));
        if (!cbAdmins.getItems().isEmpty()) {
            cbAdmins.getSelectionModel().selectFirst();
        }
    }

    public Administrator getSelected() {
        return selected;
    }

    @FXML
    private void onOk() {
        selected = cbAdmins.getSelectionModel().getSelectedItem();
        close();
    }

    @FXML
    private void onCancel() {
        selected = null;
        close();
    }

    private void close() {
        Stage stage = (Stage) cbAdmins.getScene().getWindow();
        stage.close();
    }
}