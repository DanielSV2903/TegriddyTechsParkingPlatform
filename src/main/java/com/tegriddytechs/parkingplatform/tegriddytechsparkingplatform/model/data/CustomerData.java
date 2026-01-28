package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Customer;

import java.util.ArrayList;

public class CustomerData {
    private ArrayList <Customer> customers;
    private transient PersistenceManager persistenceManager;

    public CustomerData() {
        this.persistenceManager = new PersistenceManager();
    }
    public void save() {
        try {
            persistenceManager.saveCustomerData(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Customer> getAllCustomers() {
        if (customers == null) {
            customers = new ArrayList<>();
        }
        return customers;
    }

    public Customer findCustomerById(int id) {
        for (Customer c : getAllCustomers()) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    public boolean registerCustomer(Customer customer) {
        if (findCustomerById(customer.getId()) != null) {
            return false;
        }
        getAllCustomers().add(customer);
        save();
        return true;
    }

    public boolean deleteCustomer(Customer customer) {
        boolean removed = getAllCustomers().remove(customer);
        if (removed) {
            save();
        }
        return removed;
    }
}
