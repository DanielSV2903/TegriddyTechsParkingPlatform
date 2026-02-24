package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity;

public enum VehicleStatus {
    PARKED("Estacionado"),EXITED("Retirado"), WAITING("En espera"),;;

    private final String estado;

    VehicleStatus(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }
}
