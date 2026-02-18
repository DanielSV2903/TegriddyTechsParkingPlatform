package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity;

public enum UserRole {
    CLERK("Operador"),ADMIN("Administrador");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }
    public String getRole() {
        return role;
    }
}
