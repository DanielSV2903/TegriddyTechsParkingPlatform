package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.repositories;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.DatabasePaths;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.PersistenceXMLManager;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.mappers.ParkingTicketXmlMapper;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingTicket;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ParkingTicketXmlRepository implements DataRepository<ParkingTicket>{
    PersistenceXMLManager<ParkingTicket> xml;

    public ParkingTicketXmlRepository() throws IOException, JDOMException {
     xml=PersistenceXMLManager.openXMLFile(DatabasePaths.TICKET_FILE.getPath(), "parkingTickets",new ParkingTicketXmlMapper());
    }
    @Override
    public List<ParkingTicket> findAll() {
        return xml.load();
    }
    public Optional<ParkingTicket> findById(String id) {
        return xml.findById(id);
    }
    @Override
    public Optional<ParkingTicket> findById(int id) {
        return xml.findById(id);
    }

    @Override
    public void insert(ParkingTicket parkingTicket) throws IOException {
        xml.insert(parkingTicket);
    }

    @Override
    public void update(ParkingTicket parkingTicket) throws IOException {
        xml.update(parkingTicket);
    }

    @Override
    public boolean deleteById(int id) throws IOException {
        return xml.deleteById(id);
    }

    @Override
    public boolean delete(ParkingTicket parkingTicket) throws IOException {
        return xml.delete(parkingTicket);
    }
}
