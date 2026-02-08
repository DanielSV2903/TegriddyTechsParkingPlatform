package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.UserData;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.UserXmlRepository;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.OperationResult;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.User;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.UserRole;

import java.util.List;

public class UserController {

    private final UserData userData;
    private final UserXmlRepository xmlRepository;

    public UserController() {
        this.userData = new UserData();
        this.xmlRepository = new UserXmlRepository();

        List<User> usersFromXml = xmlRepository.loadAll();
        this.userData.replaceAll(usersFromXml);
    }

    public OperationResult addUser(User user) {
        userData.addUser(user);
        persist();
        return OperationResult.success("Usuario creado correctamente");
    }

    public OperationResult updateUser(User user) {
        userData.update(user);
        persist();
        return OperationResult.success("Usuario actualizado correctamente");
    }

    public OperationResult deleteUser(User user) {
        userData.deleteUser(user);
        persist();
        return OperationResult.success("Usuario eliminado correctamente");
    }

    public User findById(int id) {
        return userData.findById(id);
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

    private void persist() {
        xmlRepository.saveAll(userData.getAllUsers());
    }
}
