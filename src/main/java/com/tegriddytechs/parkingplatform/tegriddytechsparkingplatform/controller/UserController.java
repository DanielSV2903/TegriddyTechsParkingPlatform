package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.UserData;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.OperationResult;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.User;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.UserRole;

import java.util.ArrayList;

public class UserController {
    private UserData userData;

    public UserController() {
        userData = new UserData();
    }

    public OperationResult addUser(User user){
        if (userData.findById(user.getId()) != null){
            return OperationResult.failure("User already exists");
        }
        userData.addUser(user);
        return OperationResult.success("User added successfully");
    }
    public OperationResult deleteUser(User user){
        if (userData.findById(user.getId()) == null){
            return OperationResult.failure("User not found");
        }
        userData.deleteUser(user);
        return OperationResult.success("User deleted successfully");
    }
    public OperationResult updateUser(User user){
        if (userData.findById(user.getId()) == null){
            return OperationResult.failure("User not found");
        }
        userData.update(user);
        return OperationResult.success("User updated successfully");
    }
    public User findById(int userId){
       return userData.findById(userId);
    }
    public ArrayList<User> findByRole(UserRole role){
        return userData.findByRole(role);
    }
    public User findByUsername(String username){

        return userData.findByUsername(username);
    }

    public ArrayList<User> getAllUsers(){
        return userData.getAllUsers();
    }

    public UserData getUserData() {
        return userData;
    }
    public void setUserData(UserData userData) {
        this.userData = userData;
    }
}
