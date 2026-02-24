package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller.DataManager;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller.ParkingOperationController;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.List;
    public class ParkingOperationViewController {

        @FXML private ComboBox<ParkingLot> cbParkingLot;
        @FXML private TilePane tileSpaces;

        @FXML private TextField tfPlatePark;
        @FXML private TextField tfPlateExit;

        @FXML private Label lblResult;
        @FXML private Label lblLotSummary;
        @FXML private Label lblSpaceDetail;

        private DataManager dataManager;
        private User loggedUser;
        private  MainMenuController mainMenuController;

        private ParkingOperationController parkingOperationController;

        public ParkingOperationViewController(MainMenuController mainMenuController) {
            this.mainMenuController = mainMenuController;
        }

        @FXML
        private void initialize() {
            setContext(mainMenuController.getDataManager(),mainMenuController.getUser());
            cbParkingLot.setOnAction(e -> refreshSpacesGrid());
        }

        public void setContext(DataManager dataManager, User loggedUser) {
            this.dataManager = dataManager;
            this.loggedUser = loggedUser;

            try {
                this.parkingOperationController = new ParkingOperationController(mainMenuController);
            } catch (IOException | JDOMException ex) {
                setResultError("No se pudo inicializar ParkingOperationController: " + ex.getMessage());
                return;
            }

            loadParkingLots();
            applyDefaultSelectionByRole();
            refreshSpacesGrid();
        }

        private void loadParkingLots() {


            if (loggedUser == null) return;

            // Clerk: SOLO su parqueo asignado
            if (loggedUser.getUserRole() == UserRole.CLERK && loggedUser instanceof Clerk clerk) {
                ParkingLot assigned = clerk.getParkingLot();
                if (assigned != null) {
                    // Traer la instancia "canónica" desde DataManager por id (opcional pero recomendado)
                    ParkingLot canonical = null;
                    List<ParkingLot> lots = dataManager.getParkingLotController().getAllParkingLots();
                    if (lots != null) {
                        for (ParkingLot lot : lots) {
                            if (lot != null && lot.getParkingLotId() == assigned.getParkingLotId()) {
                                canonical = lot;
                                break;
                            }
                        }
                    }
                    cbParkingLot.getItems().add(canonical != null ? canonical : assigned);
                    cbParkingLot.getSelectionModel().selectFirst();
                }

                cbParkingLot.setDisable(true); // no puede cambiarlo
                return;
            }

            // Admin: ve todos los parqueos
            List<ParkingLot> lots = dataManager.getParkingLotController().getAllParkingLots();
            if (lots != null) cbParkingLot.getItems().addAll(lots);
            cbParkingLot.setDisable(false);
        }

        private void applyDefaultSelectionByRole() {
            // Ya no hace falta lógica especial para Clerk (loadParkingLots lo deja seleccionado y bloqueado)
            if (cbParkingLot.getSelectionModel().getSelectedItem() == null && !cbParkingLot.getItems().isEmpty()) {
                cbParkingLot.getSelectionModel().selectFirst();
            }
        }

        @FXML
        private void onRefreshLots(ActionEvent e) {
            loadParkingLots();
            applyDefaultSelectionByRole();
            refreshSpacesGrid();
            setResultInfo("Parqueos recargados.");
        }

        @FXML
        private void onRefreshSpaces(ActionEvent e) {
            refreshSpacesGrid();
        }

        private void refreshSpacesGrid() {
            tileSpaces.getChildren().clear();
            lblSpaceDetail.setText("Seleccione un parqueo y luego un espacio.");

            ParkingLot selectedLot = cbParkingLot.getSelectionModel().getSelectedItem();
            if (selectedLot == null) {
                lblLotSummary.setText("Sin parqueo seleccionado");
                return;
            }

            ParkingSpace[] spaces = selectedLot.getSpaces();
            int total = (spaces == null) ? 0 : spaces.length;
            int occupied = 0;
            int free = 0;

            if (spaces != null) {
                for (ParkingSpace s : spaces) {
                    if (s == null) continue;

                    if (s.isState()) occupied++; else free++;

                    Button b = new Button("E-" + s.getSpaceNumber());
                    b.setPrefSize(66, 46);

                    String baseColor = s.isState() ? "#f56565" : "#48bb78";
                    String borderColor = s.isPreferential() ? "#805ad5" : "#cbd5e0";

                    b.setStyle(
                            "-fx-background-color: " + baseColor + ";" +
                                    "-fx-text-fill: white;" +
                                    "-fx-font-weight: 700;" +
                                    "-fx-background-radius: 10;" +
                                    "-fx-border-radius: 10;" +
                                    "-fx-border-width: 2;" +
                                    "-fx-border-color: " + borderColor + ";" +
                                    "-fx-cursor: hand;"
                    );

                    b.setOnAction(evt -> showSpaceDetail(s));
                    tileSpaces.getChildren().add(b);
                }
            }

            lblLotSummary.setText("Parqueo #" + selectedLot.getParkingLotId()
                    + " | Total: " + total + " | Libres: " + free + " | Ocupados: " + occupied);
        }

        private void showSpaceDetail(ParkingSpace s) {
            String plate = (s.getParkedVehicle() != null) ? safePlate(s.getParkedVehicle()) : "";
            lblSpaceDetail.setText(
                    "Espacio: " + s.getSpaceNumber() +
                            " | Tipo: " + s.getSpaceType() +
                            " | Preferencial: " + (s.isPreferential() ? "Sí" : "No") +
                            " | Estado: " + (s.isState() ? "Ocupado" : "Libre") +
                            (plate.isBlank() ? "" : " | Placa: " + plate)
            );
        }

        @FXML
        private void onPark(ActionEvent e) {
            ParkingLot selectedLot = cbParkingLot.getSelectionModel().getSelectedItem();
            if (selectedLot == null) {
                setResultError("Seleccione un parqueo.");
                return;
            }

            String plate = tfPlatePark.getText() == null ? "" : tfPlatePark.getText().trim();
            if (plate.isBlank()) {
                setResultError("Ingrese la placa para parquear.");
                return;
            }

            Vehicle vehicle = findVehicleByPlate(plate);
            if (vehicle == null) {
                setResultError("Vehículo no encontrado: " + plate);
                return;
            }

            try {
                OperationResult result = parkingOperationController.parkVehicle(selectedLot.getParkingLotId(), vehicle);

                dataManager.connectAll(); // refresca referencias (tickets/spaces/vehicle)

                if (result.isSuccessfull()) {
                    setResultOk(result.getMessage());
                    refreshSpacesGrid();
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

                dataManager.connectAll();

                if (result.isSuccessfull()) {
                    setResultOk(result.getMessage());
                    refreshSpacesGrid();
                } else {
                    setResultError(result.getMessage());
                }
            } catch (IOException ex) {
                setResultError("Error registrando salida: " + ex.getMessage());
            }
        }

        @FXML
        private void onClear(ActionEvent e) {
            tfPlatePark.clear();
            tfPlateExit.clear();
            setResultInfo("Formulario limpio.");
            lblSpaceDetail.setText("Seleccione un parqueo y luego un espacio.");
        }

        @FXML
        private void goBack(ActionEvent e) {
            if (e != null && e.getSource() instanceof Node node) {
                Stage stage = (Stage) node.getScene().getWindow();
                stage.close();
            }        }

        private Vehicle findVehicleByPlate(String plate) {
            return mainMenuController.getVehicleController().findVehicleByPlate(plate);
        }

        private String safePlate(Vehicle v) {
            if (v == null) return "";
            String plate = v.getPlate();
            if (plate == null || plate.isBlank()) plate = v.getLicensePlate();
            if (plate == null) return "";
            return plate.trim();
        }

        private void setResultOk(String msg) {
            lblResult.setStyle("-fx-text-fill: #2f855a; -fx-font-weight: 700;");
            lblResult.setText(msg);
        }

        private void setResultError(String msg) {
            lblResult.setStyle("-fx-text-fill: #c53030; -fx-font-weight: 700;");
            lblResult.setText(msg);
        }

        private void setResultInfo(String msg) {
            lblResult.setStyle("-fx-text-fill: #4a5568; -fx-font-weight: 700;");
            lblResult.setText(msg);
        }
    }
