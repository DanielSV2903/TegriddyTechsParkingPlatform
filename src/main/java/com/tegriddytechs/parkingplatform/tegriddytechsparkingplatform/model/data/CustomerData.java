package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Customer;

import java.util.ArrayList;

public class CustomerData {
    private ArrayList <Customer> customers;
    private transient PersistenceManager persistenceManager;

    public CustomerData() {
        this.customers = new ArrayList<>();
        this.persistenceManager = new PersistenceManager();
    }
    public void save() {
        try {
            persistenceManager.saveCustomerData(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public ArrayList <Customer> getAllCustomers() {
        return customers;
    }
    public Customer findCustomerById(String id) {
        Customer customer=null;
        for (Customer actualCustomer : customers) {
            if (actualCustomer.getId().equals(id)) {
                customer= actualCustomer;
            }
        }
        return customer;
    }

    public void registerCustomer(Customer customer) {
        this.customers.add(customer);
        save();
    }
    public void deleteCustomer(Customer customer) {
        this.customers.remove(customer);
        save();
    }
}
