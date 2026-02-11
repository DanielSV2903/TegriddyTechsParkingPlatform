package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Administrator;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Clerk;
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
                users.remove(u);
                u.setName(user.getName());
                u.setUserName(user.getUserName());
                u.setPassword(user.getPassword());
                u.setUserRole(user.getUserRole());
                users.add(u);
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

    public void replaceAll(List<User> newUsers) {
        this.users.clear();
        this.users.addAll(newUsers);
    }

    public int getNextClerkIDByCount() {
        int count = 0;
        if (users != null) {
            for (User u :users) {
                if (u instanceof Clerk) {
                    count++;
                }
            }
        }
        return count + 1;
    }
    public int getNextAdminIDByCount() {
        int count = 0;
        if (users != null) {
            for (User u :users) {
                if (u instanceof Administrator) {
                    count++;
                }
            }
        }
        return count + 1;
    }
    public List<Clerk> getClerks() {
        List<Clerk> clerks=new ArrayList<>();
        if (users != null) {
            for (User u : users) {
                if (u instanceof Clerk) {
                    clerks.add((Clerk) u);
                }
            }
        }
        return clerks;
    }
    public List<Administrator> getAdmins() {
        List<Administrator> administrators=new ArrayList<>();
        if (users!= null) {
            for (User u : users) {
                if (u instanceof Administrator) {
                    administrators.add((Administrator) u);
                }
            }
        }
        return administrators;
    }

}
