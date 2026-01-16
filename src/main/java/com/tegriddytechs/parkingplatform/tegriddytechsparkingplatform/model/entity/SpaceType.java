package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity;

public enum SpaceType {
    CAR("Carro"),MOTORCYCLE("Motocicleta"),BICYCLE("Bicicleta"),HEAVY("Pesado");

    private final String type;

    SpaceType(String type) {
        this.type = type;
    }
}