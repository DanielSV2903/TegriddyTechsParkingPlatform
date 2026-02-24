package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller.*;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.ParkingSpaceData;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.RateData;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.jdom2.JDOMException;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class MainMenuController {

    private UserController userController;
    private CustomerController customerController;
    private  ParkingLotController parkingLotController;
    private VehicleController vehicleController;
    private  ParkingSpaceController parkingSpaceController;
    private RateController rateController;
    private  ParkingTicketController parkingTicketController;
    private VehicleTypeController vehicleTypeController;
    private ParkingMapController parkingMapController;
    private User user;
    private DataManager dataManager;
    @FXML
    private Label todayUsage;
    @FXML
    private Label totalSpaces;
    @FXML
    private Label adminNameLabel;
    @FXML
    private Label adminMailLabel;
    @FXML
    private Label totalParkingLots;
    @FXML
    private Label totalActiveVehicles;

    @FXML
    private void initialize() {
        try {
            dataManager = new DataManager();
            userController = dataManager.getUserController();
            customerController=dataManager.getCustomerController();
            vehicleController = dataManager.getVehicleController();
            parkingLotController = dataManager.getParkingLotController();
            parkingSpaceController = dataManager.getParkingSpaceController();
            rateController = dataManager.getRateController();
            parkingTicketController = dataManager.getParkingTicketController();
            vehicleTypeController=new VehicleTypeController();
            parkingMapController=new ParkingMapController();
        } catch (IOException | JDOMException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    public void toClients(ActionEvent actionEvent) {
        openCrudWindow("customer-crud-view-improved.fxml", "CRUD Clientes", () -> new CustomerCrudController(this));
    }

    @FXML
    public void toRates(ActionEvent actionEvent) {
        openCrudWindow("rate-crud-view-improved.fxml", "CRUD Tarifas", () -> new RateCrudController(this));
    }

    @FXML
    public void toClerks(ActionEvent actionEvent) {
        openCrudWindow("clerk-crud-view-improved.fxml", "CRUD Cajeros", () -> new ClerkCrudController(this));

    }

    @FXML
    public void toAdmin(ActionEvent actionEvent) {
        openCrudWindow("admin-crud-view-improved.fxml", "CRUD Administradores", () -> new AdminCrudController(this));
    }

    @FXML
    public void toParkings(ActionEvent actionEvent) {
        openCrudWindow("parkinglot-crud-view-improved.fxml", "CRUD Parqueaderos", () -> new ParkingLotCrudController(this));

    }

    @FXML
    public void toUsers(ActionEvent actionEvent) {
        openCrudWindow("user-crud-view-improved.fxml", "CRUD Usuarios", () -> new UserCrudControllerImproved(this));
    }

    @FXML
    public void toTickets(ActionEvent actionEvent) {
        openCrudWindow("ticket-crud-view-improved.fxml", "CRUD Tickets", () -> new TicketCrudController(this));
    }

    @FXML
    public void toVehicles(ActionEvent actionEvent) {
        openCrudWindow("vehicle-crud-view-improved.fxml", "CRUD Vehiculos", () -> new VehicleCrudController(this));
    }

    @FXML
    public void toSpaces(ActionEvent actionEvent) {
        openCrudWindow("parkingspace-crud-view-improved.fxml", "CRUD Espacios", () -> new ParkingSpaceCrudController(this));
    }

    @FXML
    private void openCrud(ActionEvent event) {
        if (!(event.getSource() instanceof Button button)) {
            return;
        }

        String target = button.getUserData() != null ? button.getUserData().toString() : "Entidad";
        switch (target) {
            case "Usuarios":
                openCrudWindow("user-crud-view-improved.fxml", "CRUD Usuarios", () -> new UserCrudControllerImproved(this));
                break;
            case "Administradores":
                openCrudWindow("admin-crud-view-improved.fxml", "CRUD Administradores", () -> new AdminCrudController(this));
                break;
            case "Cajeros":
                openCrudWindow("clerk-crud-view-improved.fxml", "CRUD Cajeros", () -> new ClerkCrudController(this));
                break;
            case "Clientes":
                openCrudWindow("customer-crud-view-improved.fxml", "CRUD Clientes", () -> new CustomerCrudController(this));
                break;
            case "Parqueaderos":
                openCrudWindow("parkinglot-crud-view-improved.fxml", "CRUD Parqueaderos", () -> new ParkingLotCrudController(this));
                break;
            case "Espacios":
                openCrudWindow("parkingspace-crud-view-improved.fxml", "CRUD Espacios", () -> new ParkingSpaceCrudController(this));
                break;
            case "Vehiculos":
                openCrudWindow("vehicle-crud-view-improved.fxml", "CRUD Vehiculos", () -> new VehicleCrudController(this));
                break;
            case "Tickets":
                openCrudWindow("ticket-crud-view-improved.fxml", "CRUD Tickets", () -> new TicketCrudController(this));
                break;
            case "Tarifas":
                openCrudWindow("rate-crud-view-improved.fxml", "CRUD Tarifas", () -> new RateCrudController(this));
                break;
                case "TiposVehiculo":
                    openCrudWindow("vehicleTypeManagementView.fxml", "CRUD Tipo de Vehiculos", () -> new VehicleTypeCrudController(this));
                    break;
            case "OperacionParqueo":
                openCrudWindow("parking-operation-map-view.fxml", "Mapa de Operación de Parqueo", () -> new ParkingOperationMapController(this));
                break;
            default:
                CrudAlertHelper.showWarning("Navegacion", "Sin CRUD configurado para: " + target);
                break;
        }
    }



    public OperationResult createUser(User user) {
        try {
            return userController.addUser(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public User readUserById(int id) {
        return userController.findById(id);
    }

    public OperationResult updateUser(User user) {
        try {
            return userController.updateUser(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public OperationResult deleteUser(User user) {
        try {
            return userController.deleteUser(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public OperationResult createAdministrator(Administrator admin) {
        try {
            return userController.addUser(admin);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Administrator readAdministratorById(int id) {
        User user = userController.findById(id);
        return user instanceof Administrator ? (Administrator) user : null;
    }

    public OperationResult updateAdministrator(Administrator admin) {
        try {
            return userController.updateUser(admin);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public OperationResult deleteAdministrator(Administrator admin) {
        try {
            return userController.deleteUser(admin);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public OperationResult createClerk(Clerk clerk) {
        try {
            return userController.addUser(clerk);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Clerk readClerkById(int id) {
        User user = userController.findById(id);
        return user instanceof Clerk ? (Clerk) user : null;
    }

    public OperationResult updateClerk(Clerk clerk) {
        try {
            return userController.updateUser(clerk);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public OperationResult deleteClerk(Clerk clerk) {
        try {
            return userController.deleteUser(clerk);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public OperationResult createCustomer(Customer customer) {
        try {
            return customerController.registerCustomer(customer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Customer readCustomerById(int id) {
        return customerController.findCustomerById(id);
    }

    public OperationResult updateCustomer(Customer customer) {
        Customer existing = customerController.findCustomerById(customer.getId());
        if (existing == null) {
            return OperationResult.failure("Customer not found");
        }
        try {
            customerController.deleteCustomer(existing);
            customerController.registerCustomer(customer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return OperationResult.success("Customer updated successfully");
    }

    public OperationResult deleteCustomer(Customer customer) {
        try {
            return customerController.deleteCustomer(customer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public OperationResult createParkingLot(ParkingLot parkingLot) {
        try {
            return parkingLotController.registerParkingLot(parkingLot);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ParkingLot readParkingLotById(int id) {
        return parkingLotController.findParkingLotById(id);
    }

    public OperationResult updateParkingLot(ParkingLot parkingLot) {
        try {
            return parkingLotController.editParkingLot(parkingLot);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public OperationResult deleteParkingLot(ParkingLot parkingLot) {
        try {
            return parkingLotController.deleteParkingLot(parkingLot);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public OperationResult createVehicle(Vehicle vehicle) {
        try {
            return vehicleController.registerVehicle(vehicle);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Vehicle readVehicleByPlate(String plate) {
        return vehicleController.findVehicleByPlate(plate);
    }

    public OperationResult updateVehicle(Vehicle vehicle) {
        try {
            return vehicleController.editVehicle(vehicle);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public OperationResult deleteVehicle(Vehicle vehicle) {
        try {
            return vehicleController.deleteVehicle(vehicle);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void openCrudWindow(String fxml, String title, Supplier<Object> controllerSupplier) {
        try {
            java.net.URL url = TegriddyTechsParkingPlatformApp.class.getResource(
                    "/tegriddytechsparkingplatform/" + fxml
            );
            if (url == null) {
                throw new IOException("FXML not found: /tegriddytechsparkingplatform/" + fxml);
            }
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(url); // importante para resolver fx:include relativos
            loader.setControllerFactory(type -> controllerSupplier.get());
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            CrudAlertHelper.showWarning("Navegacion", "No se pudo abrir la ventana: " + title + " - " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void handleCreateUser() {
        Integer id = promptInt("Usuarios", "Crear usuario", "Id");
        String name = promptText("Usuarios", "Crear usuario", "Nombre");
        String username = promptText("Usuarios", "Crear usuario", "Usuario");
        String password = promptText("Usuarios", "Crear usuario", "Contrasena");
        UserRole role = promptChoice("Usuarios", "Crear usuario", "Rol", UserRole.values());
        if (id == null || name == null || username == null || password == null || role == null) {
            return;
        }
        User user = role == UserRole.ADMIN
                ? new Administrator(id, name, username, password)
                : new Clerk(id, name, username, password);
        showResult("Usuarios", createUser(user));
    }

    private void handleReadUser() {
        Integer id = promptInt("Usuarios", "Consultar usuario", "Id");
        if (id == null) {
            return;
        }
        User user = readUserById(id);
        showEntity("Usuarios", user);
    }

    private void handleUpdateUser() {
        Integer id = promptInt("Usuarios", "Actualizar usuario", "Id");
        String name = promptText("Usuarios", "Actualizar usuario", "Nombre");
        String username = promptText("Usuarios", "Actualizar usuario", "Usuario");
        String password = promptText("Usuarios", "Actualizar usuario", "Contrasena");
        UserRole role = promptChoice("Usuarios", "Actualizar usuario", "Rol", UserRole.values());
        if (id == null || name == null || username == null || password == null || role == null) {
            return;
        }
        User user = role == UserRole.ADMIN
                ? new Administrator(id, name, username, password)
                : new Clerk(id, name, username, password);
        showResult("Usuarios", updateUser(user));
    }

    private void handleDeleteUser() {
        Integer id = promptInt("Usuarios", "Eliminar usuario", "Id");
        if (id == null) {
            return;
        }
        User user = readUserById(id);
        if (user == null) {
            showAlert("Usuarios", "Eliminar usuario", "Usuario no encontrado", Alert.AlertType.WARNING);
            return;
        }
        showResult("Usuarios", deleteUser(user));
    }

    private void handleCreateAdministrator() {
        Integer id = promptInt("Administradores", "Crear administrador", "Id");
        String name = promptText("Administradores", "Crear administrador", "Nombre");
        String username = promptText("Administradores", "Crear administrador", "Usuario");
        String password = promptText("Administradores", "Crear administrador", "Contrasena");
        if (id == null || name == null || username == null || password == null) {
            return;
        }
        Administrator admin = new Administrator(id, name, username, password);
        showResult("Administradores", createAdministrator(admin));
    }

    private void handleReadAdministrator() {
        Integer id = promptInt("Administradores", "Consultar administrador", "Id");
        if (id == null) {
            return;
        }
        Administrator admin = readAdministratorById(id);
        showEntity("Administradores", admin);
    }

    private void handleUpdateAdministrator() {
        Integer id = promptInt("Administradores", "Actualizar administrador", "Id");
        String name = promptText("Administradores", "Actualizar administrador", "Nombre");
        String username = promptText("Administradores", "Actualizar administrador", "Usuario");
        String password = promptText("Administradores", "Actualizar administrador", "Contrasena");
        if (id == null || name == null || username == null || password == null) {
            return;
        }
        Administrator admin = new Administrator(id, name, username, password);
        showResult("Administradores", updateAdministrator(admin));
    }

    private void handleDeleteAdministrator() {
        Integer id = promptInt("Administradores", "Eliminar administrador", "Id");
        if (id == null) {
            return;
        }
        Administrator admin = readAdministratorById(id);
        if (admin == null) {
            showAlert("Administradores", "Eliminar administrador", "Administrador no encontrado", Alert.AlertType.WARNING);
            return;
        }
        showResult("Administradores", deleteAdministrator(admin));
    }

    private void handleCreateClerk() {
        Integer id = promptInt("Cajeros", "Crear cajero", "Id");
        String name = promptText("Cajeros", "Crear cajero", "Nombre");
        String username = promptText("Cajeros", "Crear cajero", "Usuario");
        String password = promptText("Cajeros", "Crear cajero", "Contrasena");
        if (id == null || name == null || username == null || password == null) {
            return;
        }
        Clerk clerk = new Clerk(id, name, username, password);
        showResult("Cajeros", createClerk(clerk));
    }

    private void handleReadClerk() {
        Integer id = promptInt("Cajeros", "Consultar cajero", "Id");
        if (id == null) {
            return;
        }
        Clerk clerk = readClerkById(id);
        showEntity("Cajeros", clerk);
    }

    private void handleUpdateClerk() {
        Integer id = promptInt("Cajeros", "Actualizar cajero", "Id");
        String name = promptText("Cajeros", "Actualizar cajero", "Nombre");
        String username = promptText("Cajeros", "Actualizar cajero", "Usuario");
        String password = promptText("Cajeros", "Actualizar cajero", "Contrasena");
        if (id == null || name == null || username == null || password == null) {
            return;
        }
        Clerk clerk = new Clerk(id, name, username, password);
        showResult("Cajeros", updateClerk(clerk));
    }

    private void handleDeleteClerk() {
        Integer id = promptInt("Cajeros", "Eliminar cajero", "Id");
        if (id == null) {
            return;
        }
        Clerk clerk = readClerkById(id);
        if (clerk == null) {
            showAlert("Cajeros", "Eliminar cajero", "Cajero no encontrado", Alert.AlertType.WARNING);
            return;
        }
        showResult("Cajeros", deleteClerk(clerk));
    }

    private void handleCreateCustomer() {
        Integer id = promptInt("Clientes", "Crear cliente", "Id");
        String name = promptText("Clientes", "Crear cliente", "Nombre");
        Integer age = promptInt("Clientes", "Crear cliente", "Edad");
        Boolean disability = promptBoolean("Clientes", "Crear cliente", "Discapacidad");
        if (id == null || name == null || age == null || disability == null) {
            return;
        }
        Customer customer = new Customer();
        customer.setId(id);
        customer.setName(name);
        customer.setAge(age);
        customer.setDisability(disability);
        showResult("Clientes", createCustomer(customer));
    }

    private void handleReadCustomer() {
        Integer id = promptInt("Clientes", "Consultar cliente", "Id");
        if (id == null) {
            return;
        }
        Customer customer = readCustomerById(id);
        showEntity("Clientes", customer);
    }

    private void handleUpdateCustomer() {
        Integer id = promptInt("Clientes", "Actualizar cliente", "Id");
        String name = promptText("Clientes", "Actualizar cliente", "Nombre");
        Integer age = promptInt("Clientes", "Actualizar cliente", "Edad");
        Boolean disability = promptBoolean("Clientes", "Actualizar cliente", "Discapacidad");
        if (id == null || name == null || age == null || disability == null) {
            return;
        }
        Customer customer = new Customer();
        customer.setId(id);
        customer.setName(name);
        customer.setAge(age);
        customer.setDisability(disability);
        showResult("Clientes", updateCustomer(customer));
    }

    private void handleDeleteCustomer() {
        Integer id = promptInt("Clientes", "Eliminar cliente", "Id");
        if (id == null) {
            return;
        }
        Customer customer = readCustomerById(id);
        if (customer == null) {
            showAlert("Clientes", "Eliminar cliente", "Cliente no encontrado", Alert.AlertType.WARNING);
            return;
        }
        showResult("Clientes", deleteCustomer(customer));
    }

    private void handleCreateParkingLot() {
        int id = Integer.parseInt(promptText("Parqueaderos", "Crear parqueadero", "Id"));
        String name = promptText("Parqueaderos", "Crear parqueadero", "Nombre");
        if (id == 0 || name == null) {
            return;
        }
        ParkingLot lot = new ParkingLot(id, name);
        showResult("Parqueaderos", createParkingLot(lot));
    }

    private void handleReadParkingLot() {
        int id = Integer.parseInt(promptText("Parqueaderos", "Consultar parqueadero", "Id"));
        if (id == 0) {
            return;
        }
        ParkingLot lot = readParkingLotById(id);
        showEntity("Parqueaderos", lot);
    }

    private void handleUpdateParkingLot() {
        int id = Integer.parseInt(promptText("Parqueaderos", "Actualizar parqueadero", "Id"));
        String name = promptText("Parqueaderos", "Actualizar parqueadero", "Nombre");
        Boolean active = promptBoolean("Parqueaderos", "Actualizar parqueadero", "Activo");
        if (id == 0 || name == null || active == null) {
            return;
        }
        ParkingLot lot = new ParkingLot(id, name);
        lot.setActive(active);
        showResult("Parqueaderos", updateParkingLot(lot));
    }

    private void handleDeleteParkingLot() {
        int id = Integer.parseInt(promptText("Parqueaderos", "Eliminar parqueadero", "Id"));
        if (id == 0) {
            return;
        }
        ParkingLot lot = readParkingLotById(id);
        if (lot == null) {
            showAlert("Parqueaderos", "Eliminar parqueadero", "Parqueadero no encontrado", Alert.AlertType.WARNING);
            return;
        }
        showResult("Parqueaderos", deleteParkingLot(lot));
    }

    private void handleCreateParkingSpace() {
        int lotId = Integer.parseInt(promptText("Espacios", "Crear espacio", "Id parqueadero"));
        Integer number = promptInt("Espacios", "Crear espacio", "Numero");
        SpaceType type = promptChoice("Espacios", "Crear espacio", "Tipo", SpaceType.values());
        Boolean preferential = promptBoolean("Espacios", "Crear espacio", "Preferencial");
        Boolean state = promptBoolean("Espacios", "Crear espacio", "Disponible");
        if (lotId == 0 || number == null || type == null || preferential == null || state == null) {
            return;
        }
        ParkingLot lot = parkingLotController.findParkingLotById(lotId);
        if (lot == null) {
            showAlert("Espacios", "Crear espacio", "Parqueadero no encontrado", Alert.AlertType.WARNING);
            return;
        }
        ParkingSpace space = new ParkingSpace(number, type, preferential, state);
        space.setParkingLot(lot);
        try {
            showResult("Espacios", createParkingSpace(space));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleReadParkingSpace() {
        int lotId = Integer.parseInt(promptText("Espacios", "Consultar espacio", "Id parqueadero"));
        Integer number = promptInt("Espacios", "Consultar espacio", "Numero");
        if (lotId == 0 || number == null) {
            return;
        }
        ParkingLot lot = parkingLotController.findParkingLotById(lotId);
        if (lot == null) {
            showAlert("Espacios", "Consultar espacio", "Parqueadero no encontrado", Alert.AlertType.WARNING);
            return;
        }
        ParkingSpace space = readParkingSpaceByNumber(number, lot);
        showEntity("Espacios", space);
    }

    private void handleUpdateParkingSpace() {
        int lotId = Integer.parseInt(promptText("Espacios", "Actualizar espacio", "Id parqueadero"));
        Integer number = promptInt("Espacios", "Actualizar espacio", "Numero");
        SpaceType type = promptChoice("Espacios", "Actualizar espacio", "Tipo", SpaceType.values());
        Boolean preferential = promptBoolean("Espacios", "Actualizar espacio", "Preferencial");
        Boolean state = promptBoolean("Espacios", "Actualizar espacio", "Disponible");
        if (lotId == 0 || number == null || type == null || preferential == null || state == null) {
            return;
        }
        ParkingLot lot = parkingLotController.findParkingLotById(lotId);
        if (lot == null) {
            showAlert("Espacios", "Actualizar espacio", "Parqueadero no encontrado", Alert.AlertType.WARNING);
            return;
        }
        ParkingSpace space = new ParkingSpace(number, type, preferential, state);
        space.setParkingLot(lot);
        try {
            showResult("Espacios", updateParkingSpace(space));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleDeleteParkingSpace() {
        int lotId = Integer.parseInt(promptText("Espacios", "Eliminar espacio", "Id parqueadero"));
        Integer number = promptInt("Espacios", "Eliminar espacio", "Numero");
        if (lotId == 0 || number == null) {
            return;
        }
        ParkingLot lot = parkingLotController.findParkingLotById(lotId);
        if (lot == null) {
            showAlert("Espacios", "Eliminar espacio", "Parqueadero no encontrado", Alert.AlertType.WARNING);
            return;
        }
        ParkingSpace space = readParkingSpaceByNumber(number, lot);
        if (space == null) {
            showAlert("Espacios", "Eliminar espacio", "Espacio no encontrado", Alert.AlertType.WARNING);
            return;
        }
        try {
            showResult("Espacios", deleteParkingSpace(space));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleCreateVehicle() {
        String plate = promptText("Vehiculos", "Crear vehiculo", "Placa");
        if (plate == null) {
            return;
        }
        VehicleStatus status = promptChoice("Vehiculos", "Crear vehiculo", "Estado", VehicleStatus.values());
        if (status == null) {
            return;
        }
        VehicleType type = new VehicleType(1, "Default", (byte) 4, 0.0, SpaceType.CAR);
        Vehicle vehicle = new Vehicle();
        vehicle.setPlate(plate);
        vehicle.setVehicleStatus(status);
        vehicle.setVehicleType(type);
        showResult("Vehiculos", createVehicle(vehicle));
    }

    private void handleReadVehicle() {
        String plate = promptText("Vehiculos", "Consultar vehiculo", "Placa");
        if (plate == null) {
            return;
        }
        Vehicle vehicle = readVehicleByPlate(plate);
        showEntity("Vehiculos", vehicle);
    }

    private void handleUpdateVehicle() {
        String plate = promptText("Vehiculos", "Actualizar vehiculo", "Placa");
        if (plate == null) {
            return;
        }
        VehicleStatus status = promptChoice("Vehiculos", "Actualizar vehiculo", "Estado", VehicleStatus.values());
        if (status == null) {
            return;
        }
        VehicleType type = new VehicleType(1, "Default", (byte) 4, 0.0, SpaceType.CAR);
        Vehicle vehicle = new Vehicle();
        vehicle.setPlate(plate);
        vehicle.setVehicleStatus(status);
        vehicle.setVehicleType(type);
        showResult("Vehiculos", updateVehicle(vehicle));
    }

    private void handleDeleteVehicle() {
        String plate = promptText("Vehiculos", "Eliminar vehiculo", "Placa");
        if (plate == null) {
            return;
        }
        Vehicle vehicle = readVehicleByPlate(plate);
        if (vehicle == null) {
            showAlert("Vehiculos", "Eliminar vehiculo", "Vehiculo no encontrado", Alert.AlertType.WARNING);
            return;
        }
        showResult("Vehiculos", deleteVehicle(vehicle));
    }

    private void handleCreateTicket() throws IOException {
        String ticketId = promptText("Tickets", "Crear ticket", "Id");
        int lotId = Integer.parseInt(promptText("Tickets", "Crear ticket", "Id parqueadero"));
        Integer spaceNumber = promptInt("Tickets", "Crear ticket", "Numero espacio");
        Integer rateId = promptInt("Tickets", "Crear ticket", "Id tarifa");
        if (ticketId == null || lotId == 0 || spaceNumber == null || rateId == null) {
            return;
        }
        ParkingLot lot = parkingLotController.findParkingLotById(lotId);
        if (lot == null) {
            showAlert("Tickets", "Crear ticket", "Parqueadero no encontrado", Alert.AlertType.WARNING);
            return;
        }
        ParkingSpace space = readParkingSpaceByNumber(spaceNumber, lot);
        if (space == null) {
            showAlert("Tickets", "Crear ticket", "Espacio no encontrado", Alert.AlertType.WARNING);
            return;
        }
        Rate rate = rateController.findRateById(rateId);
        if (rate == null) {
            showAlert("Tickets", "Crear ticket", "Tarifa no encontrada", Alert.AlertType.WARNING);
            return;
        }
        ParkingTicket ticket = new ParkingTicket(ticketId, space, LocalDateTime.now(), rate);
        showResult("Tickets", createTicket(ticket));
    }

    private void handleReadTicket() {
        String ticketId = promptText("Tickets", "Consultar ticket", "Id");
        if (ticketId == null) {
            return;
        }
        ParkingTicket ticket = parkingTicketController.findById(ticketId);
        showEntity("Tickets", ticket);
    }

    private void handleUpdateTicket() throws IOException {
        String ticketId = promptText("Tickets", "Actualizar ticket", "Id");
        Integer rateId = promptInt("Tickets", "Actualizar ticket", "Id tarifa");
        if (ticketId == null || rateId == null) {
            return;
        }
        ParkingTicket existing = parkingTicketController.findById(ticketId);
        if (existing == null) {
            showAlert("Tickets", "Actualizar ticket", "Ticket no encontrado", Alert.AlertType.WARNING);
            return;
        }
        Rate rate = rateController.findRateById(rateId);
        if (rate == null) {
            showAlert("Tickets", "Actualizar ticket", "Tarifa no encontrada", Alert.AlertType.WARNING);
            return;
        }
        existing.setRate(rate);
        existing.setExitTime(LocalDateTime.now());
        showResult("Tickets", updateTicket(existing));
    }

    private void handleDeleteTicket() throws IOException {
        String ticketId = promptText("Tickets", "Eliminar ticket", "Id");
        if (ticketId == null) {
            return;
        }
        ParkingTicket ticket = parkingTicketController.findById(ticketId);
        if (ticket == null) {
            showAlert("Tickets", "Eliminar ticket", "Ticket no encontrado", Alert.AlertType.WARNING);
            return;
        }
        showResult("Tickets", deleteTicket(ticket));
    }

    private void handleCreateRate() throws IOException {
        Integer id = promptInt("Tarifas", "Crear tarifa", "Id");
        Double fee = promptDouble("Tarifas", "Crear tarifa", "Valor");
        TimeUnit unit = promptChoice("Tarifas", "Crear tarifa", "Unidad de tiempo", TimeUnit.values());
        SpaceType spaceType = promptChoice("Tarifas", "Crear tarifa", "Tipo espacio", SpaceType.values());
        if (id == null || fee == null || unit == null || spaceType == null) {
            return;
        }
        VehicleType type = new VehicleType(1, "Default", (byte) 4, fee, spaceType);
        Rate rate = new Rate(id, type, unit, fee);
        showResult("Tarifas", createRate(rate));
    }

    private void handleReadRate() {
        Integer id = promptInt("Tarifas", "Consultar tarifa", "Id");
        if (id == null) {
            return;
        }
        Rate rate = rateController.findRateById(id);
        showEntity("Tarifas", rate);
    }

    private void handleUpdateRate() throws IOException {
        Integer id = promptInt("Tarifas", "Actualizar tarifa", "Id");
        Double fee = promptDouble("Tarifas", "Actualizar tarifa", "Valor");
        TimeUnit unit = promptChoice("Tarifas", "Actualizar tarifa", "Unidad de tiempo", TimeUnit.values());
        SpaceType spaceType = promptChoice("Tarifas", "Actualizar tarifa", "Tipo espacio", SpaceType.values());
        if (id == null || fee == null || unit == null || spaceType == null) {
            return;
        }
        VehicleType type = new VehicleType(1, "Default", (byte) 4, fee, spaceType);
        Rate rate = new Rate(id, type, unit, fee);
        showResult("Tarifas", updateRate(rate));
    }

    private void handleDeleteRate() throws IOException {
        Integer id = promptInt("Tarifas", "Eliminar tarifa", "Id");
        if (id == null) {
            return;
        }
        Rate rate = rateController.findRateById(id);
        if (rate == null) {
            showAlert("Tarifas", "Eliminar tarifa", "Tarifa no encontrada", Alert.AlertType.WARNING);
            return;
        }
        showResult("Tarifas", deleteRate(rate));
    }

    public OperationResult createParkingSpace(ParkingSpace space) throws IOException {
        ParkingSpace existing = parkingSpaceController.findParkingSpaceByNumber(space.getSpaceNumber(), space.getParkingLot());
        if (existing != null) {
            return OperationResult.failure("Parking space already exists");
        }
        parkingSpaceController.registerParkingSpace(space);
        return OperationResult.success("Parking space created");
    }

    public ParkingSpace readParkingSpaceByNumber(int number, ParkingLot lot) {
        return parkingSpaceController.findParkingSpaceByNumber(number, lot);
    }

    public OperationResult updateParkingSpace(ParkingSpace space) throws IOException {
        ParkingSpace existing = parkingSpaceController.findParkingSpaceByNumber(space.getSpaceNumber(), space.getParkingLot());
        if (existing == null) {
            return OperationResult.failure("Parking space not found");
        }
        parkingSpaceController.editParkingSpace(space);
        return OperationResult.success("Parking space updated");
    }

    public OperationResult deleteParkingSpace(ParkingSpace space) throws IOException {
        ParkingSpace existing = parkingSpaceController.findParkingSpaceByNumber(space.getSpaceNumber(), space.getParkingLot());
        if (existing == null) {
            return OperationResult.failure("Parking space not found");
        }
        parkingSpaceController.deleteParkingSpace(existing);
        return OperationResult.success("Parking space deleted");
    }

    public OperationResult createRate(Rate rate) throws IOException {
        if (rateController.findRateById(rate.getRateId()) != null) {
            return OperationResult.failure("Rate already exists");
        }
        return rateController.addRate(rate);
    }

    public OperationResult updateRate(Rate rate) throws IOException {
        Rate existing = rateController.findRateById(rate.getRateId());
        if (existing == null) {
            return OperationResult.failure("Rate not found");
        }
        return rateController.updateRate(rate);
    }

    public OperationResult deleteRate(Rate rate) throws IOException {
        Rate existing = rateController.findRateById(rate.getRateId());
        if (existing == null) {
            return OperationResult.failure("Rate not found");
        }
        return rateController.removeRate(existing);

    }

    public Rate readRateById(int id) {
        return rateController.findRateById(id);
    }

    public OperationResult createTicket(ParkingTicket ticket) throws IOException {
        if (parkingTicketController.findById(ticket.getTicketId()) != null) {
            return OperationResult.failure("Ticket already exists");
        }
        parkingTicketController.addParkingTicket(ticket);
        return OperationResult.success("Ticket created");
    }

    public OperationResult updateTicket(ParkingTicket ticket) throws IOException {
        ParkingTicket existing = parkingTicketController.findById(ticket.getTicketId());
        if (existing == null) {
            return OperationResult.failure("Ticket not found");
        }
        return parkingTicketController.updateParkingTicket(ticket);
    }

    public OperationResult deleteTicket(ParkingTicket ticket) throws IOException {
        ParkingTicket existing = parkingTicketController.findById(ticket.getTicketId());
        if (existing == null) {
            return OperationResult.failure("Ticket not found");
        }
        parkingTicketController.removeParkingTicket(existing);
        return OperationResult.success("Ticket deleted");
    }



    public ParkingTicket readTicketById(String id) {
        return parkingTicketController.findById(id);
    }

    private String promptText(String title, String header, String content) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);
        Optional<String> result = dialog.showAndWait();
        return result.filter(value -> !value.trim().isEmpty()).orElse(null);
    }

    private Integer promptInt(String title, String header, String content) {
        String value = promptText(title, header, content);
        if (value == null) {
            return null;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException ex) {
            showAlert(title, header, "Valor invalido: " + value, Alert.AlertType.WARNING);
            return null;
        }
    }

    private Double promptDouble(String title, String header, String content) {
        String value = promptText(title, header, content);
        if (value == null) {
            return null;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException ex) {
            showAlert(title, header, "Valor invalido: " + value, Alert.AlertType.WARNING);
            return null;
        }
    }

    private Boolean promptBoolean(String title, String header, String content) {
        ChoiceDialog<Boolean> dialog = new ChoiceDialog<>(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);
        Optional<Boolean> result = dialog.showAndWait();
        return result.orElse(null);
    }

    private <T> T promptChoice(String title, String header, String content, T[] options) {
        if (options.length == 0) {
            return null;
        }
        ChoiceDialog<T> dialog = new ChoiceDialog<>(options[0], options);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);
        Optional<T> result = dialog.showAndWait();
        return result.orElse(null);
    }

    private void showResult(String title, OperationResult result) {
        Alert.AlertType type = result != null && result.isSuccessfull() ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR;
        String message = result != null ? result.getMessage() : "Operacion sin resultado";
        showAlert(title, "Resultado", message, type);
    }

    private void showEntity(String title, Object entity) {
        String message = entity != null ? entity.toString() : "No encontrado";
        showAlert(title, "Detalle", message, Alert.AlertType.INFORMATION);
    }

    private void showAlert(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type, content, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }

    public ArrayList<Customer> getAllCustomers() {
        return customerController.getAllCustomers();
    }

    public ArrayList<Vehicle> getAllVehicles() {
        return vehicleController.getAllVehicles();
    }

    public ArrayList<ParkingSpace> getAllParkingSpaces() {
        return parkingSpaceController.getAllParkingSpaces();
    }

    public ArrayList<ParkingLot> getAllParkingLots() {
        return parkingLotController.getAllParkingLots();
    }

    public ArrayList<ParkingTicket> getAllTickets() {
        return (ArrayList<ParkingTicket>) parkingTicketController.getAllTickets();
    }

    public java.util.List<User> getAllUsers() {
        return userController.getAllUsers();
    }

    @Deprecated
    private void openSystemManagementMenu(ActionEvent event) {
        swapRoot(event, "system-management-menu.fxml");
    }

    @Deprecated
    private void openMainMenu(ActionEvent event) {
        swapRoot(event, "menu-view.fxml"); // este es tu menú principal actual
    }

    private void swapRoot(ActionEvent event, String fxml) {
        try {
            java.net.URL url = TegriddyTechsParkingPlatformApp.class.getResource(
                    "/tegriddytechsparkingplatform/" + fxml
            );
            if (url == null) {
                throw new IOException("FXML not found: /tegriddytechsparkingplatform/" + fxml);
            }
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(url); // importante para resolver fx:include relativos

            // Reusa el mismo controller (así no pierdes la lógica existente)
            loader.setControllerFactory(type -> this);

            Parent root = loader.load();

            if (event.getSource() instanceof Node node && node.getScene() != null) {
                node.getScene().setRoot(root); // “recarga” la pestaña / vista actual
            }
        } catch (IOException ex) {
            CrudAlertHelper.showWarning("Navegacion", "No se pudo cargar la vista: " + fxml + " - " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Deprecated
    public void logOut(ActionEvent actionEvent) {
        int respuesta = JOptionPane.showConfirmDialog(
                null,
                "¿Desea cerrar sesión?",
                "Cerrar Sesión",
                JOptionPane.YES_NO_OPTION
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        TegriddyTechsParkingPlatformApp.class.getResource(
                                "/tegriddytechsparkingplatform/login-view-improved.fxml"
                        )
                );

                Parent root = loader.load();

                Node node = (Node) actionEvent.getSource();
                node.getScene().setRoot(root);

            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        null,
                        "Error al cerrar sesión",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserController getUserController() {
        return userController;
    }

    @FXML
    public void logOutOnAction(ActionEvent actionEvent) {
        logOut(actionEvent);
    }

    public int calculatePreferentialSpaces(ParkingSpace[] spaces) {
        int count = 0;
        if (spaces == null) return count;
        for (int i = 0; i < spaces.length; i++) {
            ParkingSpace space = spaces[i];
            if (space.isPreferential())
                count++;
        }
        return count;
    }

    public List<Rate> getAllRates() {
        return rateController.getAllRates();
    }

    public CustomerController getCustomerController() {
        return customerController;
    }

    public ParkingLotController getParkingLotController() {
        return parkingLotController;
    }

    public VehicleController getVehicleController() {
        return vehicleController;
    }

    public ParkingSpaceController getParkingSpaceController() {
        return parkingSpaceController;
    }

    public RateController getRateController() {
        return rateController;
    }

    public ParkingTicketController getParkingTicketController() {
        return parkingTicketController;
    }

    public void setUserController(UserController userController) {
        this.userController = userController;
    }

    public void setCustomerController(CustomerController customerController) {
        this.customerController = customerController;
    }

    public void setParkingLotController(ParkingLotController parkingLotController) {
        this.parkingLotController = parkingLotController;
    }

    public void setVehicleController(VehicleController vehicleController) {
        this.vehicleController = vehicleController;
    }

    public void setParkingSpaceController(ParkingSpaceController parkingSpaceController) {
        this.parkingSpaceController = parkingSpaceController;
    }

    public void setRateController(RateController rateController) {
        this.rateController = rateController;
    }

    public void setParkingTicketController(ParkingTicketController parkingTicketController) {
        this.parkingTicketController = parkingTicketController;
    }

    public void setVehicleTypeController(VehicleTypeController vehicleTypeController) {
        this.vehicleTypeController = vehicleTypeController;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Deprecated
    public void openCrusd(ActionEvent actionEvent) {
    }

    public List<VehicleType> getAllVehicleTypes() {
        return vehicleTypeController.getAllVehicleTypes();
    }

    public VehicleTypeController getVehicleTypeController() {
        return this.vehicleTypeController;
    }
}
