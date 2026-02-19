package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.repositories;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.DatabasePaths;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.PersistenceXMLManager;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.mappers.CustomerXmlMapper;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Customer;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class CustomerXmlRepository implements DataRepository<Customer> {

    protected PersistenceXMLManager<Customer> xml;

    public CustomerXmlRepository() throws IOException, JDOMException {
        this.xml = PersistenceXMLManager.openXMLFile(DatabasePaths.CUSTOMER_FILE.getPath(), "customers",new CustomerXmlMapper());
    }

    @Override
    public List<Customer> findAll() {
        return xml.load();
    }

    @Override
    public Optional<Customer> findById(int id) {
        return xml.findById(id);
    }

    @Override
    public void insert(Customer customer) throws IOException {
        xml.insert(customer);
    }

    @Override
    public void update(Customer customer) throws IOException {
        xml.update(customer);
    }

    @Override
    public boolean deleteById(int id) throws IOException {
        return xml.deleteById(id);
    }

    @Override
    public boolean delete(Customer customer) throws IOException {
        return xml.delete(customer);
    }
}
