package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.DatabasePaths;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.UserData;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.repositories.UserXmlRepository;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.*;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.List;

public class UserController {

    private final UserData userData;
    private final UserXmlRepository xmlRepository;

    public UserController() throws IOException, JDOMException {
        this.userData = new UserData();
        this.xmlRepository = new UserXmlRepository();
        List<User> usersFromXml = xmlRepository.findAll();
        this.userData.replaceAll(usersFromXml);
    }

    public OperationResult addUser(User user) throws IOException {
        userData.addUser(user);
        return OperationResult.success("Usuario creado correctamente");
    }

    public OperationResult updateUser(User user) throws IOException {
        userData.update(user);
        return OperationResult.success("Usuario actualizado correctamente");
    }

    public OperationResult deleteUser(User user) throws IOException {
        userData.deleteUser(user);
        return OperationResult.success("Usuario eliminado correctamente");
    }

    public User findById(int id) {
        return userData.findById(id).orElse(null);
    }

    public User findByUsername(String username) {
        return userData.findByUsername(username);
    }

    public List<User> findByRole(UserRole role) {
        return userData.findByRole(role);
    }

    public List<User> getAllUsers() {
        return userData.getAllUsers();
    }



    public int getNextClerkIDByCount() {
        return userData.getNextClerkIDByCount();
    }
    public List<Clerk> getClerks() {
        return userData.getClerks();
    }
    public List<Administrator> getAdmins() {
        return userData.getAdmins();
    }

    public int geNextAdminIDByCount() {
        return userData.getNextAdminIDByCount();
    }
}
