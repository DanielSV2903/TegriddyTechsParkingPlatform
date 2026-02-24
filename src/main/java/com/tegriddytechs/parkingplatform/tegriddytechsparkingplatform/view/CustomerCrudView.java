package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class CustomerCrudView {

    private final MainMenuView mainMenuView;

    @FXML
    private TextField tfId;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfAge;
    @FXML
    private CheckBox cbDisability;
    @FXML
    private TextField tfSearch;
    @FXML
    private TableView<Customer> tableCustomers;
    @FXML
    private TableColumn<Customer, Integer> colId;
    @FXML
    private TableColumn<Customer, String> colName;
    @FXML
    private TableColumn<Customer, Integer> colAge;
    @FXML
    private TableColumn<Customer, Boolean> colDisability;
    @FXML
    private Label lblTotalRecords;

    private ObservableList<Customer> masterList = FXCollections.observableArrayList();
    private FilteredList<Customer> filteredList = new FilteredList<>(masterList, p -> true);

    public CustomerCrudView(MainMenuView mainMenuView) {
        this.mainMenuView = mainMenuView;
    }

    @FXML
    private void initialize() {
        try {
            colId.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("id"));
            colName.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("name"));
            colAge.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("age"));

            //Mostrar sí o no en vez de true o false para discapacidad
            colDisability.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("disability"));
            colDisability.setCellFactory(column -> new TableCell<Customer, Boolean>() {
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item ? "Sí" : "No");
                    }
                }
            });

            tableCustomers.setItems(filteredList);

            tableCustomers.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    loadCustomerToForm(newSelection);
                }
            });

            if (tfSearch != null) {
                tfSearch.textProperty().addListener((obs, oldV, newV) -> {
                    String lower = newV == null ? "" : newV.toLowerCase();
                    filteredList.setPredicate(c -> {
                        if (lower.isBlank()) return true;
                        return String.valueOf(c.getId()).contains(lower) || c.getName().toLowerCase().contains(lower);
                    });
                    updateRecordCount();
                });
            }

            loadData();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadCustomerToForm(Customer customer) {
        if (customer == null) return;

        if (tfId != null) tfId.setText(String.valueOf(customer.getId()));
        if (tfName != null) tfName.setText(customer.getName() != null ? customer.getName() : "");
        if (tfAge != null) tfAge.setText(String.valueOf(customer.getAge()));
        if (cbDisability != null) cbDisability.setSelected(customer.isDisability());
    }

    private void loadData() {
        masterList.setAll(mainMenuView.getAllCustomers());
        updateRecordCount();
    }

    private void updateRecordCount() {
        if (lblTotalRecords != null) {
            lblTotalRecords.setText(String.valueOf(filteredList.size()));
        }
    }

    @FXML
    private void onCreate(ActionEvent actionEvent) {
        Integer id = CrudFormUtils.readInt(tfId, "Clientes", "Id");
        String name = CrudFormUtils.readRequired(tfName, "Clientes", "Nombre");
        Integer age = CrudFormUtils.readInt(tfAge, "Clientes", "Edad");
        if (id == null || name == null || age == null) {
            return;
        }
        Customer customer = new Customer();
        customer.setId(id);
        customer.setName(name);
        customer.setAge(age);
        customer.setDisability(cbDisability.isSelected());
        CrudAlertHelper.showResult("Clientes", mainMenuView.createCustomer(customer));
        loadData();
    }

    @FXML
    private void onRead(ActionEvent actionEvent) {
        Integer id = CrudFormUtils.readInt(tfId, "Clientes", "Id");
        if (id == null) {
            return;
        }
        Customer customer = mainMenuView.readCustomerById(id);
        CrudAlertHelper.showEntity("Clientes", customer);
    }

    @FXML
    private void onUpdate(ActionEvent actionEvent) {
        Integer id = CrudFormUtils.readInt(tfId, "Clientes", "Id");
        String name = CrudFormUtils.readRequired(tfName, "Clientes", "Nombre");
        Integer age = CrudFormUtils.readInt(tfAge, "Clientes", "Edad");
        if (id == null || name == null || age == null) {
            return;
        }
        Customer customer = new Customer();
        customer.setId(id);
        customer.setName(name);
        customer.setAge(age);
        customer.setDisability(cbDisability.isSelected());
        CrudAlertHelper.showResult("Clientes", mainMenuView.updateCustomer(customer));
        loadData();
    }

    @FXML
    private void onDelete(ActionEvent actionEvent) {
        Integer id = CrudFormUtils.readInt(tfId, "Clientes", "Id");
        if (id == null) {
            return;
        }
        Customer customer = mainMenuView.readCustomerById(id);
        if (customer == null) {
            CrudAlertHelper.showWarning("Clientes", "Cliente no encontrado");
            return;
        }
        CrudAlertHelper.showResult("Clientes", mainMenuView.deleteCustomer(customer));
        loadData();
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
        // Clear form fields
        if (tfId != null) tfId.clear();
        if (tfName != null) tfName.clear();
        if (tfAge != null) tfAge.clear();
        if (cbDisability != null) cbDisability.setSelected(false);
    }
}
