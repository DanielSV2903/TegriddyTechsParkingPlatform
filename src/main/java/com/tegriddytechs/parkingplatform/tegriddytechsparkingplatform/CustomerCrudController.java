package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class CustomerCrudController {

    private final MainMenuController mainMenuController;

    @FXML
    private TextField tfId;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfAge;
    @FXML
    private CheckBox cbDisability;

    public CustomerCrudController(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
    }

    @FXML
    private void onCreate() {
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
        CrudAlertHelper.showResult("Clientes", mainMenuController.createCustomer(customer));
    }

    @FXML
    private void onRead() {
        Integer id = CrudFormUtils.readInt(tfId, "Clientes", "Id");
        if (id == null) {
            return;
        }
        Customer customer = mainMenuController.readCustomerById(id);
        CrudAlertHelper.showEntity("Clientes", customer);
    }

    @FXML
    private void onUpdate() {
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
        CrudAlertHelper.showResult("Clientes", mainMenuController.updateCustomer(customer));
    }

    @FXML
    private void onDelete() {
        Integer id = CrudFormUtils.readInt(tfId, "Clientes", "Id");
        if (id == null) {
            return;
        }
        Customer customer = mainMenuController.readCustomerById(id);
        if (customer == null) {
            CrudAlertHelper.showWarning("Clientes", "Cliente no encontrado");
            return;
        }
        CrudAlertHelper.showResult("Clientes", mainMenuController.deleteCustomer(customer));
    }
}
