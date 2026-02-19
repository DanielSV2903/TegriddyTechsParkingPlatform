package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.repositories.CustomerXmlRepository;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Customer;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerData extends CustomerXmlRepository {

    private final ArrayList<Customer> customers = new ArrayList<>();

    public CustomerData() throws IOException, JDOMException {
        super();
        reload();
    }

    public void reload() {
        customers.clear();
        customers.addAll(super.findAll()); // carga desde XML al cache
    }

    public ArrayList<Customer> getAllCustomers() {
        return customers;
    }

    public Customer findCustomerById(int id) {
        return findById(id).orElse(null);
    }

    private Optional<Customer> findInCacheById(int id) {
        for (Customer c : customers) {
            if (c.getId() == id) return Optional.of(c);
        }
        return Optional.empty();
    }

    @Override
    public List<Customer> findAll() {
        return customers; // cache
    }

    @Override
    public Optional<Customer> findById(int id) {
        return findInCacheById(id); // cache
    }

    @Override
    public void insert(Customer customer) throws IOException {
        if (customer == null) throw new IllegalArgumentException("customer cannot be null");
        if (findCustomerById(customer.getId()) != null) throw new IllegalArgumentException("Customer with ID " + customer.getId() + " already exists.");
        super.insert(customer);
        customers.add(customer);
    }

    @Override
    public void update(Customer customer) throws IOException {
        if (customer == null) throw new IllegalArgumentException("customer cannot be null");

        super.update(customer);

        findInCacheById(customer.getId()).ifPresent(customers::remove);
        customers.add(customer);
    }

    @Override
    public boolean deleteById(int id) throws IOException {
        boolean deleted = super.deleteById(id); // XML primero
        if (!deleted) return false;

        findInCacheById(id).ifPresent(customers::remove);
        return true;
    }

    @Override
    public boolean delete(Customer customer) throws IOException {
        if (customer == null) return false;
        return deleteById(customer.getId());
    }

    public boolean registerCustomer(Customer customer) throws IOException {
        if (findCustomerById(customer.getId()) != null) return false;
        this.insert(customer);
        return true;
    }

    public boolean deleteCustomer(Customer customer) throws IOException {
        return delete(customer);
    }
}
