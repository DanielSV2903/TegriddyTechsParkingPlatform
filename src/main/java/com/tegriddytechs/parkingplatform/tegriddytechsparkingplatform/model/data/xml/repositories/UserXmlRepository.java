package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.repositories;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.DatabasePaths;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.PersistenceXMLManager;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.mappers.UserXmlMapper;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.User;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserXmlRepository implements DataRepository<User> {
    protected final PersistenceXMLManager<User> xml;

    public UserXmlRepository() throws JDOMException, IOException {
        this.xml = PersistenceXMLManager.openXMLFile(DatabasePaths.USER_FILE.getPath(), "users", new UserXmlMapper());
    }

    @Override
    public List<User> findAll() {
        return xml.load();
    }

    @Override
    public Optional<User> findById(int id) {
        return xml.findById(String.valueOf(id));
    }

    @Override
    public void upsert(User user) throws IOException {
        xml.upsert(user);
    }

    @Override
    public boolean deleteById(int id) throws IOException {
        return xml.deleteById(String.valueOf(id));
    }

    @Override
    public boolean delete(User user) throws IOException {
        return xml.delete(user);
    }
}
