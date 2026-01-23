package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.VehicleData;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Clerk;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.OperationResult;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingLot;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Vehicle;

public class ClerkController {
    private Clerk user;
    private ParkingLot parkingLot;

    public ClerkController(Clerk user) {
        this.user = user;
        this.parkingLot = user.getParkingLot();
    }

    public OperationResult registerVehicleEntry(Vehicle vehicle) {
        //TODO Lógica para registrar la entrada del vehículo
        return OperationResult.success("Vehiculo estacionado exitosamente en el espacio");
    }
}
