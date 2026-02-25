package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;


import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Administrator;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingLot;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AdminCrudView {

    private final MainMenuView mainMenuView;
    @FXML
    private TextField tfId;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private TableColumn colName;
    @FXML
    private Label lblTotalRecords;
    @FXML
    private TableView<Administrator> tableAdmins;
    @FXML
    private TextField tfSearch;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colUsername;

    // NUEVO: parqueos a cargo
    @FXML private Button btnConfigureParkingLots;
    @FXML private Label lblParkingLotsSummary;
    private final ObservableList<Administrator> adminsData = FXCollections.observableArrayList();
    private final ObservableList<ParkingLot> selectedParkingLots = FXCollections.observableArrayList();

    public AdminCrudView(MainMenuView mainMenuView) {
        this.mainMenuView = mainMenuView;
    }

    @FXML
    private void initialize() {
        setUpTableColumns();

        // Setear items una sola vez y luego solo actualizar con setAll(...)
        tableAdmins.setItems(adminsData);

        reloadAdminsIntoTable();

        updateRecordCount();
        refreshParkingLotsSummary();
    }
    private void reloadAdminsIntoTable() {
        List<Administrator> admins = mainMenuView.getUserController().getAdmins();
        adminsData.setAll(admins != null ? admins : List.of());
        tableAdmins.refresh();
    }


    private void updateRecordCount() {
        tfId.setText(String.valueOf(mainMenuView.getUserController().geNextAdminIDByCount()));
        if (lblTotalRecords != null) {
            lblTotalRecords.setText("Total: " + mainMenuView.getUserController().getAdmins().size() + " registros");
        }
    }

    private void setUpTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("userName"));
    }

    @FXML
    private void onCreate() {
        Integer id = CrudFormUtils.readInt(tfId, "Administradores", "Id");
        String name = CrudFormUtils.readRequired(tfName, "Administradores", "Nombre");
        String username = CrudFormUtils.readRequired(tfUsername, "Administradores", "Usuario");
        String password = CrudFormUtils.readRequired(tfPassword, "Administradores", "Contrasena");

        if (id == null || name == null || username == null || password == null) return;

        Administrator admin = new Administrator(id, name, username, password);

        CrudAlertHelper.showResult(
                "Administradores",
                mainMenuView.createAdministrator(admin)
        );

        // 🔥 sincronizar parqueos después de crear
        syncAdminParkingLots(admin, selectedParkingLots);

        reloadAdminsIntoTable();
        updateRecordCount();
    }


    @FXML
    private void onUpdate() {
        Integer id = CrudFormUtils.readInt(tfId, "Administradores", "Id");
        String name = CrudFormUtils.readRequired(tfName, "Administradores", "Nombre");
        String username = CrudFormUtils.readRequired(tfUsername, "Administradores", "Usuario");
        String password = CrudFormUtils.readRequired(tfPassword, "Administradores", "Contrasena");

        if (id == null || name == null || username == null || password == null) return;

        Administrator admin = mainMenuView.readAdministratorById(id);
        if (admin == null) return;

        admin.setName(name);
        admin.setUserName(username);
        admin.setPassword(password);

        CrudAlertHelper.showResult(
                "Administradores",
                mainMenuView.updateAdministrator(admin)
        );

        // 🔥 sincronización bidireccional REAL
        syncAdminParkingLots(admin, selectedParkingLots);

        reloadAdminsIntoTable();
    }

    @FXML
    private void onDelete() {
        Integer id = CrudFormUtils.readInt(tfId, "Administradores", "Id");
        if (id == null) return;

        Administrator admin = mainMenuView.readAdministratorById(id);
        if (admin == null) {
            CrudAlertHelper.showWarning("Administradores", "Administrador no encontrado");
            return;
        }

        // 🔥 quitar admin de todos sus parqueos
        if (admin.getParkingLots() != null) {
            for (ParkingLot lot : admin.getParkingLots()) {
                if (lot != null) {
                    lot.setAdministrator(null);
                    mainMenuView.updateParkingLot(lot);
                }
            }
        }

        CrudAlertHelper.showResult(
                "Administradores",
                mainMenuView.deleteAdministrator(admin)
        );

        reloadAdminsIntoTable();
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
        filterTable();
    }

    @FXML
    public void onClear(ActionEvent actionEvent) {
        if (tfId != null) tfId.clear();
        if (tfName != null) tfName.clear();
        if (tfUsername != null) tfUsername.clear();
        if (tfPassword != null) tfPassword.clear();
        if (tfSearch != null) tfSearch.clear();
        initialize();
    }
    private void filterTable() {
        String text = tfSearch.getText();
        List<Administrator> administrators = mainMenuView.getUserController().getAdmins();
        if (text != null && !text.isEmpty()) {
            administrators = administrators.stream()
                    .filter(c -> String.valueOf(c.getId()).contains(text)
                            || c.getName().toLowerCase().contains(text.toLowerCase())
                            || c.getUserName().toLowerCase().contains(text.toLowerCase()))
                    .toList();
        }
        tableAdmins.setItems(FXCollections.observableArrayList(administrators));
        updateRecordCount();
        tfSearch.clear();
    }
    private void fillFields() {
        Administrator administrator = tableAdmins.getSelectionModel().getSelectedItem();
        if (administrator == null) {
            return;
        }

        tfId.setText(String.valueOf(administrator.getId()));
        tfName.setText(administrator.getName());
        tfUsername.setText(administrator.getUserName());
        tfPassword.setText(administrator.getPassword());

        selectedParkingLots.clear();
        if (administrator.getParkingLots() != null) {
            selectedParkingLots.addAll(administrator.getParkingLots());
        }
        refreshParkingLotsSummary();
    }

    @FXML
    public void selectClerkOnMouseClicked(Event event) {
        fillFields();
    }

    @FXML
    private void onConfigureParkingLots() {
        List<ParkingLot> allLots = mainMenuView.getAllParkingLots();
        allLots.sort(Comparator.comparingInt(ParkingLot::getParkingLotId));

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Configurar parqueos a cargo");

        // Tabla de parqueos registrados
        TableView<ParkingLot> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        TableColumn<ParkingLot, Integer> colId = new TableColumn<>("ID");
        // IMPORTANT: ParkingLot exposes getParkingLotId(), not getId()
        colId.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getParkingLotId()));

        TableColumn<ParkingLot, String> colName = new TableColumn<>("Nombre");
        colName.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().getName()));

        table.getColumns().addAll(colId, colName);
        ObservableList<ParkingLot> available = FXCollections.observableArrayList(allLots);
        table.setItems(available);

        // Seleccionados
        ListView<ParkingLot> selectedList = new ListView<>(FXCollections.observableArrayList(selectedParkingLots));
        selectedList.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(ParkingLot item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String name = item.getName() != null ? item.getName() : "Parqueadero";
                    setText(name + " (" + item.getParkingLotId() + ")");
                }
            }
        });

        Button btnAdd = new Button("Agregar →");
        btnAdd.setMaxWidth(Double.MAX_VALUE);
        btnAdd.setOnAction(e -> {
            ParkingLot selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            if (containsParkingLotById(selectedList.getItems(), selected.getParkingLotId())) return;
            selectedList.getItems().add(selected);
        });

        Button btnRemove = new Button("← Quitar");
        btnRemove.setMaxWidth(Double.MAX_VALUE);
        btnRemove.setOnAction(e -> {
            ParkingLot selected = selectedList.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            selectedList.getItems().removeIf(l -> l != null && l.getParkingLotId() == selected.getParkingLotId());
        });

        Button btnOk = new Button("OK");
        btnOk.setDefaultButton(true);
        btnOk.setOnAction(e -> {
            selectedParkingLots.setAll(selectedList.getItems());
            refreshParkingLotsSummary();
            dialog.close();
        });

        Button btnCancel = new Button("Cancelar");
        btnCancel.setCancelButton(true);
        btnCancel.setOnAction(e -> dialog.close());

        VBox centerButtons = new VBox(10, btnAdd, btnRemove);
        centerButtons.setFillWidth(true);

        VBox left = new VBox(8, new Label("Parqueos registrados"), table);
        VBox right = new VBox(8, new Label("Parqueos a cargo del admin"), selectedList);

        HBox content = new HBox(12, left, centerButtons, right);
        HBox.setHgrow(left, Priority.ALWAYS);
        HBox.setHgrow(right, Priority.ALWAYS);
        left.setPrefWidth(420);
        right.setPrefWidth(340);

        HBox footer = new HBox(10);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        footer.getChildren().addAll(spacer, btnCancel, btnOk);

        BorderPane root = new BorderPane();
        root.setCenter(content);
        root.setBottom(footer);
        BorderPane.setMargin(content, new javafx.geometry.Insets(12));
        BorderPane.setMargin(footer, new javafx.geometry.Insets(12));

        dialog.setScene(new Scene(root, 860, 520));
        dialog.showAndWait();
    }

    private void refreshParkingLotsSummary() {
        if (lblParkingLotsSummary == null) return;

        if (selectedParkingLots.isEmpty()) {
            lblParkingLotsSummary.setText("Sin parqueos asignados");
            return;
        }

        String summary = selectedParkingLots.stream()
                .filter(l -> l != null)
                .map(l -> {
                    String name = l.getName() != null ? l.getName() : "Parqueadero";
                    return name + " (" + l.getParkingLotId() + ")";
                })
                .collect(Collectors.joining(", "));

        lblParkingLotsSummary.setText(summary);
    }

    private static boolean containsParkingLotById(List<ParkingLot> list, int id) {
        if (list == null) return false;
        for (ParkingLot l : list) {
            if (l != null && l.getParkingLotId() == id) return true;
        }
        return false;
    }

    private void syncAdminParkingLots(
            Administrator admin,
            List<ParkingLot> newLots
    ) {
        Administrator managedAdmin =
                mainMenuView.readAdministratorById(admin.getId());

        if (managedAdmin == null) return;

        // 🔥 Asegurar lista inicializada
        if (managedAdmin.getParkingLots() == null) {
            managedAdmin.setParkingLots(new ArrayList<>());
        }

        List<ParkingLot> previousLots =
                new ArrayList<>(managedAdmin.getParkingLots());

        // 1) Quitar parqueos que ya no le pertenecen
        for (ParkingLot lot : previousLots) {
            if (lot == null) continue;

            boolean stillAssigned = newLots != null && newLots.stream()
                    .anyMatch(l -> l != null && l.getParkingLotId() == lot.getParkingLotId());

            if (!stillAssigned) {
                ParkingLot managedLot =
                        mainMenuView.readParkingLotById(lot.getParkingLotId());

                if (managedLot != null) {
                    managedLot.setAdministrator(null);
                    mainMenuView.updateParkingLot(managedLot);
                }

                managedAdmin.getParkingLots().removeIf(
                        l -> l != null && l.getParkingLotId() == lot.getParkingLotId()
                );
            }
        }

        // 2) Asignar nuevos parqueos
        if (newLots != null) {
            for (ParkingLot lot : newLots) {
                if (lot == null) continue;

                ParkingLot managedLot =
                        mainMenuView.readParkingLotById(lot.getParkingLotId());

                if (managedLot == null) continue;

                Administrator oldAdmin = managedLot.getAdministrator();

                // Si tenía otro admin, quitarlo
                if (oldAdmin != null && oldAdmin.getId() != managedAdmin.getId()) {
                    if (oldAdmin.getParkingLots() == null) {
                        oldAdmin.setParkingLots(new ArrayList<>());
                    }

                    oldAdmin.getParkingLots().removeIf(
                            l -> l != null && l.getParkingLotId() == managedLot.getParkingLotId()
                    );

                    mainMenuView.updateAdministrator(oldAdmin);
                }

                managedLot.setAdministrator(managedAdmin);

                boolean alreadyPresent = managedAdmin.getParkingLots().stream()
                        .anyMatch(l -> l != null && l.getParkingLotId() == managedLot.getParkingLotId());

                if (!alreadyPresent) {
                    managedAdmin.getParkingLots().add(managedLot);
                }

                mainMenuView.updateParkingLot(managedLot);
            }
        }

        mainMenuView.updateAdministrator(managedAdmin);
    }
}
