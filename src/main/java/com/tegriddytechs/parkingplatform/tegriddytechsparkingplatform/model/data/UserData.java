package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.User;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.UserRole;

import java.util.ArrayList;
import java.util.List;

public class UserData {
    private ArrayList<User> users ;

    public UserData(){
        this.users = new ArrayList<>();
    }

    public void addUser(User user){
        this.users.add(user);
    }
    public void deleteUser(User user){
        this.users.remove(user);
    }
    public void update(User user){
        for (User u:getAllUsers()){
            if (u.getId() == user.getId()){
                u.setName(user.getName());
                u.setUserName(user.getUserName());
                u.setPassword(user.getPassword());
                u.setUserRole(user.getUserRole());
            }
        }
    }
    public User findById(int userId){
        User userToReturn = null;
        for (User u:getAllUsers()){
            if (u.getId() == userId){
                userToReturn=u;
            }
        }
        return userToReturn;
    }
    public ArrayList<User> findByRole(UserRole role){
        ArrayList<User> usersToReturn = new ArrayList<>();
        for (User u:getAllUsers()){
            if (u.getUserRole().equals(role)){
                usersToReturn.add(u);
            }
        }
        return usersToReturn;
    }
    public User findByUsername(String username){
        User userToReturn = null;
        for (User u:getAllUsers()){
            if (u.getUserName().equalsIgnoreCase(username)){
                userToReturn=u;
            }
        }
        return userToReturn;
    }

    public ArrayList<User> getAllUsers(){
        return users;
    }

}
