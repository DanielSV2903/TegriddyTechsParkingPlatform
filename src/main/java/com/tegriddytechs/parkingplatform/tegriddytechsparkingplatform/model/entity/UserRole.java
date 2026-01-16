package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity;

public enum UserRole {
    OPERATOR("Operador"),ADMIN("Administrador");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }
}
