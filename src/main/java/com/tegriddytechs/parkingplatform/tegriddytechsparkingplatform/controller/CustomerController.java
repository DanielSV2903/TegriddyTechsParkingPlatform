package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.CustomerData;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Customer;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.OperationResult;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.ArrayList;

public class CustomerController {
    private CustomerData customerData;
    public CustomerController() throws IOException, JDOMException {
        this.customerData =new CustomerData();
    }

    public ArrayList<Customer> getAllCustomers() {
        return customerData.getAllCustomers();
    }
    public Customer findCustomerById(int id) {
        return customerData.findCustomerById(id);
    }

    public OperationResult registerCustomer(Customer customer) throws IOException {
        if (customerData.findCustomerById(customer.getId()) != null) {
            return OperationResult.failure("Customer already exists");
        }
        customerData.registerCustomer(customer);
            return OperationResult.success("Customer registered successfully");
        }

    public OperationResult deleteCustomer(Customer customer) throws IOException {
        if (customerData.findCustomerById(customer.getId()) == null) {
            return OperationResult.failure("Customer not found");
        }
        customerData.deleteCustomer(customer);
            return OperationResult.success("Customer removed successfully");

    }
}
