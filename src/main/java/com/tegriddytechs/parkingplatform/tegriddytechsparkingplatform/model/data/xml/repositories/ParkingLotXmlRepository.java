package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.repositories;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.DatabasePaths;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.PersistenceXMLManager;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.mappers.CustomerXmlMapper;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.mappers.ParkingLotXmlMapper;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingLot;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ParkingLotXmlRepository implements DataRepository<ParkingLot>{
    protected PersistenceXMLManager<ParkingLot> xml;

    public ParkingLotXmlRepository() throws IOException, JDOMException {
        this.xml = PersistenceXMLManager.openXMLFile(DatabasePaths.PARKING_LOT_FILE.getPath(), "parkingLots",new ParkingLotXmlMapper());
    }

    @Override
    public List<ParkingLot> findAll() {
        return xml.load();
    }

    @Override
    public Optional<ParkingLot> findById(int id) {
        return xml.findById(id);
    }

    @Override
    public void update(ParkingLot lot) throws IOException {
        xml.update(lot);
    }

    @Override
    public void insert(ParkingLot lot) throws IOException {
        xml.insert(lot);
    }

    @Override
    public boolean deleteById(int id) throws IOException {
        return xml.deleteById(id);
    }

    @Override
    public boolean delete(ParkingLot lot) throws IOException {
        return xml.delete(lot);
    }
}
