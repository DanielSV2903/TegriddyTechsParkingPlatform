package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.CustomerData;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.PersistenceManager;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Customer;
import java.util.ArrayList;

public class CustomerController {
    private CustomerData customerData;

    public CustomerController() {
        this.customerData = new PersistenceManager().loadCustomerData();
    }

    public ArrayList<Customer> getAllCustomers() {
        return customerData.getAllCustomers();
    }
    public Customer findCustomerById(String id) {
        return customerData.findCustomerById(id);
    }

    public OperationResult  registerCustomer(Customer customer) {
        if (customerData.findCustomerById(customer.getCustomerId()) != null) {
            return OperationResult.failure("Customer already exists");
        }
        customerData.registerCustomer(customer);
        return OperationResult.success("Customer registered successfully");
    }
    public OperationResult  deleteCustomer(Customer customer) {
        if (customerData.findCustomerById(customer.getCustomerId()) == null) {
            return OperationResult.failure("Customer not found");
        }
        customerData.deleteCustomer(customer);
        return OperationResult.success("Customer removed successfully");
    }
}
