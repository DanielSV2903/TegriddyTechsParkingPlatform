package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity;

import java.util.Objects;

public class User extends Person {
    private String userName;
    private String password;
    private UserRole userRole;

    public User() {
    }

    public User(int id, String name) {
        super(id, name);
    }

    public User(int id, String name, UserRole userRole) {
        super(id, name);
        this.userRole = userRole;
    }

    public User(int id, String name, String userName, String password, UserRole userRole) {
        super(id, name);
        this.password = password;
        this.userName = userName;
        this.userRole = userRole;
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }


    //GETTER Y SETTERS

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return Objects.equals(userName, user.userName) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userName, password);
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
