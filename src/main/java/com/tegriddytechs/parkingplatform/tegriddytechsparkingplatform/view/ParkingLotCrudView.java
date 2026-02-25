package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller.VehicleTypeController;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ParkingLotCrudView {

    private final MainMenuView mainMenuView;

    @FXML
    private TextField tfId;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfSearch;
    @FXML
    private TableView<ParkingLot> tableParkingLots;
    @FXML
    private TableColumn<ParkingLot, String> colId;
    @FXML
    private TableColumn<ParkingLot, String> colName;
    @FXML
    private TableColumn<ParkingLot, Integer> colCapacity;
    @FXML
    private Label lblTotalRecords;
    private ParkingSpace [] spaces;

    private ObservableList<ParkingLot> masterList = FXCollections.observableArrayList();
    private FilteredList<ParkingLot> filteredList = new FilteredList<>(masterList, p -> true);
    private Administrator administrator;
    @FXML
    private CheckBox cbActive;
    @FXML
    private Label lblAdmin;
    @FXML
    private TableColumn<ParkingLot,Integer> colPreferenciales;

    public ParkingLotCrudView(MainMenuView mainMenuView) {
        this.mainMenuView = mainMenuView;
    }

    @FXML
    private void initialize() {
        try {
            colId.setCellValueFactory(new PropertyValueFactory<>("parkingLotId"));
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colCapacity.setCellValueFactory(cell-> new SimpleIntegerProperty(cell.getValue().getSpaces()!=null?cell.getValue().getSpaces().length:0).asObject());

            tableParkingLots.setItems(filteredList);

            if (tfSearch != null) {
                tfSearch.textProperty().addListener((obs, oldV, newV) -> {
                    String lower = newV == null ? "" : newV.toLowerCase();
                    filteredList.setPredicate(l -> {
                        if (lower.isBlank()) return true;
                        return (l.getParkingLotId() != 0 && String.valueOf(l.getParkingLotId()).toLowerCase().contains(lower))
                                || (l.getName() != null && l.getName().toLowerCase().contains(lower));
                    });
                    updateRecordCount();
                });
            }

            loadData();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadData() {
        tfId.setText(mainMenuView.getAllParkingLots().size() +1+"");
        masterList.setAll(mainMenuView.getAllParkingLots());
        updateRecordCount();
        colPreferenciales.setCellValueFactory(cell-> new SimpleIntegerProperty(mainMenuView.calculatePreferentialSpaces(cell.getValue().getSpaces())).asObject());
    }

    private void updateRecordCount() {
        if (lblTotalRecords != null) lblTotalRecords.setText(String.valueOf(filteredList.size()));
    }

    @FXML
    private void onCreate(ActionEvent actionEvent) {
        int id = Integer.parseInt(CrudFormUtils.readRequired(tfId, "Parqueaderos", "Id"));
        String name = CrudFormUtils.readRequired(tfName, "Parqueaderos", "Nombre");

        if (id == 0 || name == null) return;

        ParkingLot lot = new ParkingLot(id, name);
        lot.setActive(true);

        try {
            changeAdministrator(lot, administrator);
            CrudAlertHelper.showResult(
                    "Parqueaderos",
                    mainMenuView.createParkingLot(lot)
            );
            loadData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    @FXML
    private void onUpdate(ActionEvent actionEvent) {
        int id = Integer.parseInt(CrudFormUtils.readRequired(tfId, "Parqueaderos", "Id"));
        String name = CrudFormUtils.readRequired(tfName, "Parqueaderos", "Nombre");

        if (id == 0 || name == null) return;

        ParkingLot existing = mainMenuView.readParkingLotById(id);
        if (existing == null) return;

        existing.setName(name);
        existing.setSpaces(spaces);
        existing.setActive(cbActive.isSelected());

        try {
            changeAdministrator(existing, administrator);
            saveSpaces(spaces);

            CrudAlertHelper.showResult(
                    "Parqueaderos",
                    mainMenuView.updateParkingLot(existing)
            );
            loadData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onDelete(ActionEvent actionEvent) {
        int id = Integer.parseInt(CrudFormUtils.readRequired(tfId, "Parqueaderos", "Id"));
        if (id == 0) return;

        ParkingLot lot = mainMenuView.readParkingLotById(id);
        if (lot == null) {
            CrudAlertHelper.showWarning("Parqueaderos", "Parqueadero no encontrado");
            return;
        }

        try {
            changeAdministrator(lot, null);

            OperationResult deleted = mainMenuView.deleteParkingLot(lot);
            CrudAlertHelper.showResult("Parqueaderos", deleted);

            if (deleted.isSuccessfull()) {
                CrudAlertHelper.showResult(
                        "Espacios",
                        mainMenuView.getParkingSpaceController()
                                .deleteParkingSpaces(lot.getSpaces())
                );
            }

            loadData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void goBack(ActionEvent actionEvent) {
        if (actionEvent != null && actionEvent.getSource() instanceof Node node) {
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    public void onRefresh(ActionEvent actionEvent) {
        loadData();
    }



    @FXML
    public void onClear(ActionEvent actionEvent) {
        if (tfId != null) tfId.clear();
        if (tfName != null) tfName.clear();
        if (cbActive != null) cbActive.setSelected(false);
    }

    @FXML
    public void selectAdminOnAction(ActionEvent actionEvent) {
        try {
            List<Administrator> administrators = mainMenuView.getUserController().getAdmins();

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/tegriddytechsparkingplatform/adminSelectionAlert.fxml")
            );
            Parent root = loader.load();

            AdminSelectDialogView controller = loader.getController();
            controller.setAdministrators(administrators);

            Stage dialog = new Stage();
            dialog.setTitle("Seleccionar administrador");
            dialog.initModality(Modality.APPLICATION_MODAL);

            if (actionEvent != null && actionEvent.getSource() instanceof Node node) {
                dialog.initOwner(node.getScene().getWindow());
            }

            dialog.setScene(new Scene(root));
            dialog.setResizable(false);
            dialog.showAndWait();

            Administrator selected = controller.getSelected();
            if (selected != null) {
                this.administrator = selected;
                lblAdmin.setText("Administrador: " + selected.getName());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


   @FXML
    public void configParkingSpacesOnAction(ActionEvent actionEvent) {
           //1-Se selecciona un parqueo para trabajar
           ParkingLot selectedLot = tableParkingLots != null ? tableParkingLots.getSelectionModel().getSelectedItem() : null;

           //2-Si no hay selección, se crea desde 0
           boolean creatingNewLot = false;
           ParkingLot targetLot = selectedLot;

           if (targetLot == null) {
               String rawId = tfId != null ? tfId.getText() : null;
               String rawName = tfName != null ? tfName.getText() : null;

               if (rawId == null || rawId.trim().isEmpty() || rawName == null || rawName.trim().isEmpty()) {
                   new Alert(
                           Alert.AlertType.WARNING,
                           "No hay parqueaderos para seleccionar.\n" +
                                   "Para configurar espacios primero, completa al menos Id y Nombre del parqueadero.",
                           ButtonType.OK
                   ).showAndWait();
                   return;
               }

               int id;
               try {
                   id = Integer.parseInt(rawId.trim());
               } catch (NumberFormatException ex) {
                   new Alert(Alert.AlertType.WARNING, "Id inválido: " + rawId, ButtonType.OK).showAndWait();
                   return;
               }

               targetLot = new ParkingLot(id, rawName.trim());
               targetLot.setActive(true);
               targetLot.setAdministrator(administrator);

               creatingNewLot = true;
           }

           try {
               //3-Cargar tipos de vehículo
               VehicleTypeController vehicleTypeController = new VehicleTypeController();
               List<VehicleType> vehicleTypes = vehicleTypeController.getAllVehicleTypes();

               if (vehicleTypes == null || vehicleTypes.isEmpty()) {
                   new Alert(
                           Alert.AlertType.WARNING,
                           "No hay tipos de vehículo registrados. Primero registra tipos de vehículo para poder configurar espacios.",
                           ButtonType.OK
                   ).showAndWait();
                   return;
               }

               //4-Cargar el fxml para configurar los espacios
               URL fxmlUrl = getClass().getResource("/tegriddytechsparkingplatform/space-config-dialog.fxml");
               if (fxmlUrl == null) {
                   throw new IOException("FXML not found: /tegriddytechsparkingplatform/space-config-dialog.fxml");
               }

               FXMLLoader loader = new FXMLLoader(fxmlUrl);
               Parent root = loader.load();

               ParkingSpaceConfigDialogView controller = loader.getController();
               controller.init(targetLot, vehicleTypes);

               Stage dialog = new Stage();
               dialog.setTitle("Configurar espacios - " + targetLot.getName());
               dialog.initModality(Modality.APPLICATION_MODAL);
               if (actionEvent != null && actionEvent.getSource() instanceof Node node) {
                   dialog.initOwner(node.getScene().getWindow());
               }
               dialog.setScene(new Scene(root));
               dialog.setResizable(false);

               dialog.showAndWait();

               //5-Se guarda la información de los espacios y se asigna al parqueo
               if (controller.isApplied()) {
                   spaces = controller.getResultSpaces();
                   targetLot.setSpaces(spaces);
                   saveSpaces(spaces);
                   if (creatingNewLot) {
                       CrudAlertHelper.showResult("Parqueaderos", mainMenuView.createParkingLot(targetLot));
                   } else {
                       CrudAlertHelper.showResult("Parqueaderos", mainMenuView.updateParkingLot(targetLot));
                   }

                   loadData();
               }
           } catch (Exception ex) {
               ex.printStackTrace();
               new Alert(Alert.AlertType.ERROR, "No se pudo abrir la configuración de espacios: " + ex.getMessage(), ButtonType.OK).showAndWait();
           }
       }

    private void saveSpaces(ParkingSpace[] spaces) throws IOException {
        if (spaces == null) return;
        for (ParkingSpace space : spaces) {
            if (space == null) continue;
            try {
                // Normalizar la referencia al ParkingLot usando la instancia gestionada por el controlador
                ParkingLot lot = space.getParkingLot();
                if (lot != null) {
                    ParkingLot managedLot = mainMenuView.getParkingLotController().findParkingLotById(lot.getParkingLotId());
                    if (managedLot != null) {
                        space.setParkingLot(managedLot);
                    }
                }

                ParkingSpace existing = mainMenuView.getParkingSpaceController()
                        .findParkingSpaceByNumber(space.getSpaceNumber(), space.getParkingLot());

                OperationResult result;
                if (existing == null) {
                    // Si no existe, crear nuevo espacio
                    result = mainMenuView.getParkingSpaceController().registerParkingSpace(space);
                } else {
                    // Si existe, actualizar propiedades del espacio existente
                    existing.setSpaceType(space.getSpaceType());
                    existing.setPreferential(space.isPreferential());
                    existing.setState(space.isState());
                    result = mainMenuView.getParkingSpaceController().editParkingSpace(existing);
                }

                if (!result.isSuccessfull()) {
                    throw new IOException(result.getMessage());
                }
            } catch (IOException e) {
                // Propagar IOExceptions tal cual para mantener el mensaje original
                throw e;
            } catch (Exception e) {
                throw new IOException("Error al guardar el espacio " + space.getSpaceNumber() + ": " + e.getMessage(), e);
            }
        }
    }


    private void fillFields() {
        ParkingLot lot= tableParkingLots.getSelectionModel().getSelectedItem();
        tfId.setText(String.valueOf(lot.getParkingLotId()));
        tfName.setText(lot.getName());
        administrator=lot.getAdministrator();
        cbActive.setSelected(lot.isActive());
        spaces=lot.getSpaces();
        lblAdmin.setText(
                administrator != null
                        ? "Administrador: " + administrator.getName()
                        : "Administrador: No asignado"
        );    }

    @FXML
    public void fillFieldsOnAction(Event event) {
        fillFields();
    }
    private void changeAdministrator(ParkingLot lot, Administrator newAdmin) throws IOException {

        Administrator oldAdmin = lot.getAdministrator();

        // 1) Quitar el parqueo del admin anterior
        if (oldAdmin != null) {
            oldAdmin.getParkingLots().removeIf(
                    p -> p.getParkingLotId() == lot.getParkingLotId()
            );
            mainMenuView.getUserController().updateUser(oldAdmin);
        }

        // 2) Asignar nuevo admin al parqueo
        lot.setAdministrator(newAdmin);

        // 3) Agregar parqueo al nuevo admin
        if (newAdmin != null) {
            if (!newAdmin.getParkingLots().contains(lot)) {
                newAdmin.getParkingLots().add(lot);
            }
            mainMenuView.getUserController().updateUser(newAdmin);
        }
    }
}

