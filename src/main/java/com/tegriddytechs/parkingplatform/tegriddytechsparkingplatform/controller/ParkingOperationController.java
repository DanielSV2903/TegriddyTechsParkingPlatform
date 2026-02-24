package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.*;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view.MainMenuController;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

public class ParkingOperationController {

    private ParkingLotController parkingLotController;
    private ParkingSpaceController parkingSpaceController;
    private VehicleController vehicleController;
    private ParkingTicketController ticketController;
    private MainMenuController mainMenuController;

    public ParkingOperationController(MainMenuController mainMenuController) throws IOException, JDOMException {
        this.parkingLotController =mainMenuController.getParkingLotController();
        this.vehicleController =mainMenuController.getVehicleController();
        this.ticketController =mainMenuController.getParkingTicketController();
        this.parkingSpaceController=mainMenuController.getParkingSpaceController();
        this.mainMenuController=mainMenuController;
    }

    public OperationResult parkVehicle(int parkingLotId, Vehicle vehicle) throws IOException {

        //Busca que el parqueo esté disponible
        ParkingLot parkingLot = parkingLotController.findParkingLotById(parkingLotId);
        if (parkingLot == null || !parkingLot.isActive()) {
            return OperationResult.failure("Parking lot not available");
        }

        //Valida el estado del vehículo
        if (vehicle.getVehicleStatus() == VehicleStatus.PARKED) {
            return OperationResult.failure("Vehicle is already parked");
        }

        //busca un espacio disponible
        ParkingSpace availableSpace= assignSpace(vehicle,parkingLot);

        if (availableSpace == null) {
            return OperationResult.failure("No available space for this vehicle type");
        }

        double fee = vehicle.getVehicleType().getFee();

        Rate rate = mainMenuController.getRateController().findBySpaceType(availableSpace.getSpaceType());
        //Generar ticket
        ParkingTicket ticket = new ParkingTicket(
                UUID.randomUUID().toString(),
                availableSpace,
                LocalDateTime.now(),rate
        );

        //Se actualizan los estados
        availableSpace.setState(true);
        availableSpace.setParkedVehicle(vehicle);
        vehicle.setVehicleStatus(VehicleStatus.PARKED);
        vehicle.setTicket(ticket);

        //Se escribe en el archivo
        ticketController.addParkingTicket(ticket);
        parkingSpaceController.editParkingSpace(availableSpace);
        vehicleController.editVehicle(vehicle);
        parkingLotController.editParkingLot(parkingLot);

        return OperationResult.success("Vehicle parked successfully", ticket);
    }

    private ParkingSpace assignSpace(Vehicle vehicle, ParkingLot parkingLot) {
        ParkingSpace availableSpace = null;
        boolean preferentialNeeded = checkForPreferentialNeeded(vehicle);
        for (ParkingSpace space : parkingLot.getSpaces()) {
            if (!space.isState() &&
                    space.getSpaceType() == vehicle.getVehicleType().getSpaceType()&& preferentialNeeded == space.isPreferential()) {
                availableSpace = space;
                break;
            }
        }
        return availableSpace;
    }

    private boolean checkForPreferentialNeeded(Vehicle vehicle) {
        return vehicle.getOwner().isDisability();
    }

    public OperationResult exitVehicle(String licensePlate) throws IOException {

        //Se busca el vehículo
        Vehicle vehicle = vehicleController.findVehicleByPlate(licensePlate);
        if (vehicle == null) {
            return OperationResult.failure("Vehicle not found");
        }

        //Se valida el estado
        if (vehicle.getVehicleStatus() != VehicleStatus.PARKED) {
            return OperationResult.failure("Vehicle is not currently parked");
        }

        ParkingTicket ticket = vehicle.getTicket();
        if (ticket == null) {
            return OperationResult.failure("Parking ticket not found");
        }

        //Asignar hora de salida
        ticket.setExitTime(LocalDateTime.now());

        //Calcular tiempo estacionado
        long minutesParked = java.time.Duration
                .between(ticket.getEntryTime(), ticket.getExitTime())
                .toMinutes();

        long hoursToCharge = (long) Math.ceil(minutesParked / 60.0);

        double hourlyFee = ticket.getRate().getFee();
        double totalAmount = hoursToCharge * hourlyFee;
        //Se libera el espacio
        ParkingSpace space = ticket.getParkingSpace();
        space.setState(false);
        space.setParkedVehicle(null);

        //Se actualiza el estado del vehículo
        vehicle.setVehicleStatus(VehicleStatus.EXITED);
        vehicle.setTicket(null);
        ticket.setAmountPaid(totalAmount);

        //Escribir cambios
        ticketController.updateParkingTicket(ticket);
        vehicleController.editVehicle(vehicle);
        parkingLotController.editParkingLot(space.getParkingLot());
        parkingSpaceController.editParkingSpace(space);


        return OperationResult.success(
                "Vehicle exited successfully. Total amount: " + totalAmount);
    }

}

