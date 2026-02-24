package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller.DataManager;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller.ParkingLotController;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller.ParkingOperationController;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller.ParkingSpaceController;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.List;

public class ParkingOperationMapView {

    @FXML
    private ComboBox<ParkingLot> cbParkingLot;
    @FXML
    private GridPane gridParkingMap;
    @FXML
    private Label lblParkingName;
    @FXML
    private Label lblTotalSpaces;
    @FXML
    private Label lblOccupiedSpaces;
    @FXML
    private Label lblAvailableSpaces;
    @FXML
    private Pane legendPanel;
    
    @FXML
    private TextField tfPlatePark;
    @FXML
    private TextField tfPlateExit;
    @FXML
    private TextField tfSpaceNumber;
    @FXML
    private Label lblResult;
    @FXML
    private Label lblSpaceDetail;

    private ParkingLotController parkingLotController;
    private ParkingSpaceController parkingSpaceController;
    private ParkingOperationController parkingOperationController;
    private DataManager dataManager;
    private User loggedUser;
    private MainMenuView mainMenuView;

    private ParkingLot selectedParkingLot;
    private ParkingSpace selectedSpace;
    
    private StackPane[][] matrizPaneles;
    private int filas;
    private int columnas;
    
    private static final String COLOR_DISPONIBLE = "#90EE90";    // Verde claro
    private static final String COLOR_OCUPADO = "#FF6B6B";        // Rojo
    private static final String COLOR_PREFERENCIAL = "#87CEEB";   // Azul cielo
    private static final String COLOR_SELECCIONADO = "#FFD700";   // Dorado (cuando se hace click)

    public ParkingOperationMapView() throws IOException, JDOMException {
        this.parkingLotController = new ParkingLotController();
        this.parkingSpaceController = new ParkingSpaceController();
    }

    public ParkingOperationMapView(MainMenuView mainMenuView) {
        this.mainMenuView = mainMenuView;
        this.dataManager = mainMenuView.getDataManager();
        this.loggedUser = mainMenuView.getUser();
        this.parkingLotController = dataManager.getParkingLotController();
        this.parkingSpaceController = dataManager.getParkingSpaceController();

        try {
            this.parkingOperationController = new ParkingOperationController(mainMenuView);
        } catch (IOException | JDOMException e) {
            showError("Error inicializando controlador de operaciones: " + e.getMessage());
        }
    }

    @FXML
    private void initialize() {
        loadParkingLots();
        setupLegend();
        
        cbParkingLot.setOnAction(e -> onParkingLotSelected());
    }

    public void setContext(DataManager dataManager, User loggedUser) {
        this.dataManager = dataManager;
        this.loggedUser = loggedUser;
        
        if (this.parkingLotController == null) {
            this.parkingLotController = dataManager.getParkingLotController();
        }
        if (this.parkingSpaceController == null) {
            this.parkingSpaceController = dataManager.getParkingSpaceController();
        }
        
        loadParkingLots();
        applyDefaultSelectionByRole();
    }

    //Carga los parqueos
    private void loadParkingLots() {
        cbParkingLot.getItems().clear();
        
        if (loggedUser == null) {
            //si no hay usuario se cargan todos
            List<ParkingLot> lots = parkingLotController.getAllParkingLots();
            if (lots != null) {
                cbParkingLot.getItems().addAll(lots);
            }
        } else if (loggedUser.getUserRole() == UserRole.CLERK && loggedUser instanceof Clerk clerk) {
            // Clerk: SOLO su parqueo asignado
            ParkingLot assigned = clerk.getParkingLot();
            if (assigned != null) {
                cbParkingLot.getItems().add(assigned);
                cbParkingLot.getSelectionModel().selectFirst();
                cbParkingLot.setDisable(true);
            }
        } else {
            // Admin: todos los parqueos
            List<ParkingLot> lots = parkingLotController.getAllParkingLots();
            if (lots != null) {
                cbParkingLot.getItems().addAll(lots);
            }
            cbParkingLot.setDisable(false);
        }
        
        //configurar como se muestra cada parqueo
        cbParkingLot.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(ParkingLot item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Seleccione un parqueo" : item.getName());
            }
        });
        
        cbParkingLot.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(ParkingLot item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getName());
            }
        });
    }

    private void applyDefaultSelectionByRole() {
        if (cbParkingLot.getSelectionModel().getSelectedItem() == null && !cbParkingLot.getItems().isEmpty()) {
            cbParkingLot.getSelectionModel().selectFirst();
            onParkingLotSelected();
        }
    }

    //Visualización dek parqueo
    @FXML
    private void onParkingLotSelected() {
        selectedParkingLot = cbParkingLot.getValue();
        selectedSpace = null;
        
        if (selectedParkingLot == null) {
            return;
        }
        
        //Cargar espacios del parqueo
        ParkingSpace[] spaces = selectedParkingLot.getSpaces();
        
        if (spaces == null || spaces.length == 0) {
            showWarning("Este parqueadero no tiene espacios configurados");
            return;
        }
        
        calculateGridDimensions(spaces.length);
        
        crearMatrizPaneles(spaces);
        
        updateStatistics(spaces);
        
        setResultInfo("Parqueadero cargado: " + selectedParkingLot.getName());
    }

    private void calculateGridDimensions(int totalSpaces) {
        columnas = (int) Math.ceil(Math.sqrt(totalSpaces));
        filas = (int) Math.ceil((double) totalSpaces / columnas);
        
        if (totalSpaces <= 10) {
            filas = 2;
            columnas = (int) Math.ceil((double) totalSpaces / 2);
        }
    }

    private void crearMatrizPaneles(ParkingSpace[] spaces) {
        gridParkingMap.getChildren().clear();
        gridParkingMap.getColumnConstraints().clear();
        gridParkingMap.getRowConstraints().clear();
        
        for (int i = 0; i < columnas; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPrefWidth(80);
            gridParkingMap.getColumnConstraints().add(col);
        }
        
        for (int i = 0; i < filas; i++) {
            RowConstraints row = new RowConstraints();
            row.setPrefHeight(80);
            gridParkingMap.getRowConstraints().add(row);
        }
        
        matrizPaneles = new StackPane[filas][columnas];
        
        int spaceIndex = 0;
        
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                
                if (spaceIndex >= spaces.length) {
                    StackPane emptyPanel = createEmptyPanel();
                    matrizPaneles[i][j] = emptyPanel;
                    gridParkingMap.add(emptyPanel, j, i);
                } else {
                    ParkingSpace space = spaces[spaceIndex];
                    if (space == null) {
                        StackPane emptyPanel = createEmptyPanel();
                        matrizPaneles[i][j] = emptyPanel;
                        gridParkingMap.add(emptyPanel, j, i);
                    } else {
                        StackPane panel = createSpacePanel(space);
                        matrizPaneles[i][j] = panel;
                        gridParkingMap.add(panel, j, i);
                    }
                    spaceIndex++;
                }
            }
        }
    }

    private StackPane createSpacePanel(ParkingSpace space) {
        StackPane panel = new StackPane();
        panel.setPrefSize(80, 80);
        panel.setAlignment(Pos.CENTER);
        
        panel.setBorder(new Border(new BorderStroke(
                Color.BLACK,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(2)
        )));
        
        String backgroundColor = getSpaceColor(space);
        panel.setStyle("-fx-background-color: " + backgroundColor + ";");
        
        Label lblNumber = new Label(String.valueOf(space.getSpaceNumber()));
        lblNumber.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2d3748;");
        
        VBox info = new VBox(2);
        info.setAlignment(Pos.CENTER);
        
        Label lblType = new Label(space.getSpaceType().toString());
        lblType.setStyle("-fx-font-size: 10px; -fx-text-fill: #4a5568;");
        
        info.getChildren().addAll(lblNumber, lblType);
        panel.getChildren().add(info);
        
        panel.setOnMouseClicked(e -> onSpaceClicked(space, panel));
        panel.setStyle(panel.getStyle() + "-fx-cursor: hand;");
        
        panel.setUserData(space);
        
        return panel;
    }


    private String getSpaceColor(ParkingSpace space) {
        if (space == null) {
            return "#f0f0f0"; // Gris claro para espacios null
        }

        if (space.isParked()) {
            return COLOR_OCUPADO;  // Rojo si está ocupado
        } else if (space.isPreferential()) {
            return COLOR_PREFERENCIAL;  // Azul si es preferencial
        } else {
            return COLOR_DISPONIBLE;  // Verde si está disponible
        }
    }

    //Si sea clickea el espacio
    private void onSpaceClicked(ParkingSpace space, StackPane panel) {
        // Deseleccionar espacio anterior
        if (selectedSpace != null) {
            refreshSingleSpace(selectedSpace);
        }
        
        // Seleccionar nuevo espacio
        selectedSpace = space;
        
        // Cambiar color a seleccionado
        panel.setStyle("-fx-background-color: " + COLOR_SELECCIONADO + "; -fx-cursor: hand;");
        
        // Actualizar campo de número
        if (tfSpaceNumber != null) {
            tfSpaceNumber.setText(String.valueOf(space.getSpaceNumber()));
        }
        
        // Mostrar detalles
        showSpaceDetail(space);
    }


    private void refreshSingleSpace(ParkingSpace space) {
        if (matrizPaneles == null || space == null) return;
        
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                StackPane panel = matrizPaneles[i][j];
                if (panel != null && panel.getUserData() instanceof ParkingSpace ps) {
                    if (ps.getSpaceNumber() == space.getSpaceNumber()) {
                        // Actualizar color según estado actual
                        String color = getSpaceColor(ps);
                        panel.setStyle("-fx-background-color: " + color + "; -fx-cursor: hand;");
                        return;
                    }
                }
            }
        }
    }

    private StackPane createEmptyPanel() {
        StackPane panel = new StackPane();
        panel.setPrefSize(80, 80);
        panel.setStyle("-fx-background-color: #f0f0f0; -fx-opacity: 0.3;");
        panel.setBorder(new Border(new BorderStroke(
                Color.LIGHTGRAY,
                BorderStrokeStyle.DASHED,
                CornerRadii.EMPTY,
                new BorderWidths(1)
        )));
        return panel;
    }

    //OPERACIONES DE PARQUEO Y SALIDA
    @FXML
    private void onPark(ActionEvent e) {
        if (selectedParkingLot == null) {
            setResultError("Seleccione un parqueadero.");
            return;
        }

        String plate = tfPlatePark.getText() == null ? "" : tfPlatePark.getText().trim();
        if (plate.isBlank()) {
            setResultError("Ingrese la placa para parquear.");
            return;
        }

        //Buscar vehículo
        Vehicle vehicle = findVehicleByPlate(plate);
        if (vehicle == null) {
            setResultError("Vehículo no encontrado: " + plate);
            return;
        }

        try {
            //Verificar si se especificó número de espacio
            Integer spaceNumber = null;
            if (tfSpaceNumber != null && !tfSpaceNumber.getText().trim().isEmpty()) {
                try {
                    spaceNumber = Integer.parseInt(tfSpaceNumber.getText().trim());
                } catch (NumberFormatException ex) {
                    setResultError("Número de espacio inválido");
                    return;
                }
            } else if (selectedSpace != null) {
                spaceNumber = selectedSpace.getSpaceNumber();
            }

            OperationResult result;
            
            if (spaceNumber != null) {
                //Parquear en espacio especIfico
                result = parkingOperationController.parkVehicleInSpecificSpace(
                    selectedParkingLot.getParkingLotId(),
                    vehicle,
                    spaceNumber
                );
            } else {
                //Parquear en el espacio libre más cercano
                result = parkingOperationController.parkVehicle(
                    selectedParkingLot.getParkingLotId(), 
                    vehicle
                );
            }

            //Reconectar datos para asegurar consistencia
            if (dataManager != null) {
                dataManager.connectAll();
            }

            if (result.isSuccessfull()) {
                setResultOk(result.getMessage());
                onClear();
                refreshView();
            } else {
                setResultError(result.getMessage());
            }
        } catch (IOException ex) {
            setResultError("Error parqueando vehículo: " + ex.getMessage());
        }
    }

    @FXML
    private void onExit(ActionEvent e) {
        String plate = tfPlateExit.getText() == null ? "" : tfPlateExit.getText().trim();
        if (plate.isBlank()) {
            setResultError("Ingrese la placa para registrar la salida.");
            return;
        }

        try {
            OperationResult result = parkingOperationController.exitVehicle(plate);

            if (dataManager != null) {
                dataManager.connectAll();
            }

            if (result.isSuccessfull()) {
                //mostrar Alert con el total a pagar
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Salida de Vehículo");
                alert.setHeaderText("El vehículo ha salido exitosamente");
                alert.setContentText(result.getMessage());
                alert.showAndWait();

                setResultOk("Salida registrada correctamente");
                refreshView();

                if (tfPlateExit != null) {
                    tfPlateExit.clear();
                }
            } else {
                setResultError(result.getMessage());
            }
        } catch (IOException ex) {
            setResultError("Error registrando salida: " + ex.getMessage());
        }
    }


    @FXML
    private void onRefresh() {
        refreshView();
        setResultInfo("Vista actualizada");
    }

    private void refreshView() {
        if (selectedParkingLot != null) {
            //Recargar el parqueadero desde el controlador
            ParkingLot updated = parkingLotController.findParkingLotById(selectedParkingLot.getParkingLotId());
            if (updated != null) {
                selectedParkingLot = updated;
                int selectedIndex = cbParkingLot.getSelectionModel().getSelectedIndex();
                cbParkingLot.getItems().set(selectedIndex, updated);
            }
            
            onParkingLotSelected();
        }
    }

    @FXML
    private void onClear() {
        if (tfPlatePark != null) tfPlatePark.clear();
        if (tfPlateExit != null) tfPlateExit.clear();
        if (tfSpaceNumber != null) tfSpaceNumber.clear();
        
        if (selectedSpace != null) {
            refreshSingleSpace(selectedSpace);
            selectedSpace = null;
        }
        
        setResultInfo("Formulario limpio.");
        if (lblSpaceDetail != null) {
            lblSpaceDetail.setText("Seleccione un espacio o escriba el número.");
        }
    }

    @FXML
    private void goBack(ActionEvent e) {
        if (e != null && e.getSource() instanceof Node node) {
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();
        }
    }

    //Métodos auxiliares
    
    private void showSpaceDetail(ParkingSpace s) {
        if (lblSpaceDetail == null) return;
        
        String plate = (s.getParkedVehicle() != null) ? safePlate(s.getParkedVehicle()) : "";
        lblSpaceDetail.setText(
            "Espacio: " + s.getSpaceNumber() +
            " | Tipo: " + s.getSpaceType() +
            " | Preferencial: " + (s.isPreferential() ? "Sí" : "No") +
            " | Estado: " + (s.isParked() ? "OCUPADO" : "LIBRE") +
            (plate.isBlank() ? "" : " | Placa: " + plate)
        );
    }

    private void updateStatistics(ParkingSpace[] spaces) {
        if (lblParkingName != null) {
            lblParkingName.setText(selectedParkingLot.getName());
        }
        
        if (lblTotalSpaces != null) {
            lblTotalSpaces.setText(String.valueOf(spaces.length));
        }
        
        int occupied = 0;
        for (ParkingSpace space : spaces) {
            //Se valida que el espacio no sea null antes de verificar si esta ocupado
            if (space != null && space.isParked()) {
                occupied++;
            }
        }
        
        if (lblOccupiedSpaces != null) {
            lblOccupiedSpaces.setText(String.valueOf(occupied));
        }
        
        if (lblAvailableSpaces != null) {
            lblAvailableSpaces.setText(String.valueOf(spaces.length - occupied));
        }
    }

    private void setupLegend() {
        if (legendPanel == null) return;
        
        HBox legend = new HBox(20);
        legend.setAlignment(Pos.CENTER);
        legend.setStyle("-fx-padding: 10;");
        
        legend.getChildren().addAll(
            createLegendItem(COLOR_DISPONIBLE, "Disponible"),
            createLegendItem(COLOR_OCUPADO, "Ocupado"),
            createLegendItem(COLOR_PREFERENCIAL, "Preferencial"),
            createLegendItem(COLOR_SELECCIONADO, "Seleccionado")
        );
        
        legendPanel.getChildren().clear();
        legendPanel.getChildren().add(legend);
    }

    private HBox createLegendItem(String color, String text) {
        HBox item = new HBox(8);
        item.setAlignment(Pos.CENTER_LEFT);
        
        StackPane colorBox = new StackPane();
        colorBox.setPrefSize(25, 25);
        colorBox.setStyle("-fx-background-color: " + color + "; -fx-border-color: black; -fx-border-width: 1;");
        
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 12px;");
        
        item.getChildren().addAll(colorBox, label);
        return item;
    }

    private Vehicle findVehicleByPlate(String plate) {
        if (dataManager == null) return null;
        
        String target = plate.trim().toUpperCase();
        List<Vehicle> vehicles = dataManager.getVehicleController().getAllVehicles();
        if (vehicles == null) return null;

        for (Vehicle v : vehicles) {
            String vp = safePlate(v).toUpperCase();
            if (vp.equals(target)) return v;
        }
        return null;
    }

    private String safePlate(Vehicle v) {
        if (v == null) return "";
        String plate = v.getPlate();
        if (plate == null || plate.isBlank()) plate = v.getLicensePlate();
        if (plate == null) return "";
        return plate.trim();
    }

    private void setResultOk(String msg) {
        if (lblResult == null) return;
        lblResult.setStyle("-fx-text-fill: #2f855a; -fx-font-weight: 700;");
        lblResult.setText(msg);
    }

    private void setResultError(String msg) {
        if (lblResult == null) return;
        lblResult.setStyle("-fx-text-fill: #c53030; -fx-font-weight: 700;");
        lblResult.setText(msg);
    }

    private void setResultInfo(String msg) {
        if (lblResult == null) return;
        lblResult.setStyle("-fx-text-fill: #4a5568; -fx-font-weight: 700;");
        lblResult.setText(msg);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
