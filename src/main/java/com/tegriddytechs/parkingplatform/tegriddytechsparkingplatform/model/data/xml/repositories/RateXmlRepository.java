package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.repositories;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.DatabasePaths;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.PersistenceXMLManager;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.mappers.RateXmlMapper;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Rate;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class RateXmlRepository implements DataRepository<Rate>{
    private PersistenceXMLManager<Rate> xml;

    public RateXmlRepository() throws IOException, JDOMException {
     xml=PersistenceXMLManager.openXMLFile(DatabasePaths.RATE_FILE.getPath(), "rates",new RateXmlMapper());
    }

    @Override
    public List<Rate> findAll() {
        return xml.load();
    }

    @Override
    public Optional<Rate> findById(int id) {
        return xml.findById(id);
    }

    @Override
    public void update(Rate rate) throws IOException {
        xml.update(rate);
    }

    @Override
    public void insert(Rate rate) throws IOException {
        xml.insert(rate);
    }

    @Override
    public boolean deleteById(int id) throws IOException {
        return xml.deleteById(id);
    }

    @Override
    public boolean delete(Rate rate) throws IOException {
        return xml.delete(rate);
    }
}
