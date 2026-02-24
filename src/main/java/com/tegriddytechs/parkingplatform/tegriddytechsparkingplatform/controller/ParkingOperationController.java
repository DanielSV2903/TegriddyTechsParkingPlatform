package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.*;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view.MainMenuView;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

public class ParkingOperationController {

    private ParkingLotController parkingLotController;
    private ParkingSpaceController parkingSpaceController;
    private VehicleController vehicleController;
    private ParkingTicketController ticketController;
    private MainMenuView mainMenuView;

    public ParkingOperationController(MainMenuView mainMenuView) throws IOException, JDOMException {
        this.parkingLotController = mainMenuView.getParkingLotController();
        this.vehicleController = mainMenuView.getVehicleController();
        this.ticketController = mainMenuView.getParkingTicketController();
        this.parkingSpaceController= mainMenuView.getParkingSpaceController();
        this.mainMenuView = mainMenuView;
    }

    public OperationResult parkVehicle(int parkingLotId, Vehicle vehicle) throws IOException {

        //Busca que el parqueo esté disponible
        ParkingLot parkingLot = parkingLotController.findParkingLotById(parkingLotId);
        if (parkingLot == null || !parkingLot.isActive()) {
            return OperationResult.failure("Parqueo no disponible");
        }

        //Valida el estado del vehículo
        if (vehicle.getVehicleStatus() == VehicleStatus.PARKED) {
            return OperationResult.failure("El vehículo ya está estacionado");
        }

        //busca un espacio disponible
        ParkingSpace availableSpace= assignSpace(vehicle,parkingLot);

        if (availableSpace == null) {
            return OperationResult.failure("No hay espacios disponibles para el tipo de vehículo: " + vehicle.getVehicleType());
        }

        Rate rate = mainMenuView.getRateController().findBySpaceType(availableSpace.getSpaceType());

        if (rate == null) {
            return OperationResult.failure("No existe tarifa para el tipo de vehículo: " + availableSpace.getSpaceType());
        }

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

        return OperationResult.success("El vehículo fue parqueado correctamente", ticket);
    }

    /**
     * Parquea un vehículo en un espacio específico
     * @param parkingLotId ID del parqueadero
     * @param vehicle Vehículo a parquear
     * @param spaceNumber Número del espacio específico donde parquear
     * @return Resultado de la operación
     */
    public OperationResult parkVehicleInSpecificSpace(int parkingLotId, Vehicle vehicle, int spaceNumber) throws IOException {
        // Buscar el parqueadero
        ParkingLot parkingLot = parkingLotController.findParkingLotById(parkingLotId);
        if (parkingLot == null || !parkingLot.isActive()) {
            return OperationResult.failure("Parqueo no disponible");
        }

        // Validar el estado del vehículo
        if (vehicle.getVehicleStatus() == VehicleStatus.PARKED) {
            return OperationResult.failure("El vehículo ya está estacionado");
        }

        // Buscar el espacio específico
        ParkingSpace specificSpace = null;
        for (ParkingSpace space : parkingLot.getSpaces()) {
            if (space != null && space.getSpaceNumber() == spaceNumber) {
                specificSpace = space;
                break;
            }
        }

        if (specificSpace == null) {
            return OperationResult.failure("El espacio número " + spaceNumber + " no fue encontrado");
        }

        // Validar que el espacio esté disponible
        if (specificSpace.isState()) {
            return OperationResult.failure("El espacio " + spaceNumber + " ya se encuentra ocupado");
        }

        // Validar que el espacio sea del tipo correcto
        if (specificSpace.getSpaceType() != vehicle.getVehicleType().getSpaceType()) {
            return OperationResult.failure("El espacio seleccionado no es compatible con el tipo de vehículo: " + vehicle.getVehicleType());
        }

        // Obtener la tarifa
        Rate rate = mainMenuView.getRateController().findBySpaceType(specificSpace.getSpaceType());
        if (rate == null) {
            return OperationResult.failure("No se encontró una tarifa para el tipo de vehículo : " + specificSpace.getSpaceType());
        }

        // Crear ticket
        ParkingTicket ticket = new ParkingTicket(
                UUID.randomUUID().toString(),
                specificSpace,
                LocalDateTime.now(),
                rate
        );

        // Actualizar estados
        specificSpace.setState(true);
        specificSpace.setParkedVehicle(vehicle);
        vehicle.setVehicleStatus(VehicleStatus.PARKED);
        vehicle.setTicket(ticket);

        // Guardar cambios
        ticketController.addParkingTicket(ticket);
        parkingSpaceController.editParkingSpace(specificSpace);
        vehicleController.editVehicle(vehicle);
        parkingLotController.editParkingLot(parkingLot);

        return OperationResult.success("El vehículo "+ vehicle.getPlate() + " se parqueó correctamente en el espacio: " + spaceNumber, ticket);
    }

    private ParkingSpace assignSpace(Vehicle vehicle, ParkingLot parkingLot) {
        ParkingSpace availableSpace = null;
        boolean preferentialNeeded = checkForPreferentialNeeded(vehicle);

        // Primera pasada: buscar con restricción de preferencial
        for (ParkingSpace space : parkingLot.getSpaces()) {
            // Validar que el espacio no sea null
            if (space == null) continue;

            // Verificar que esté disponible, sea del tipo correcto y cumpla con preferencial
            if (!space.isState() &&
                    space.getSpaceType() == vehicle.getVehicleType().getSpaceType() &&
                    preferentialNeeded == space.isPreferential()) {
                availableSpace = space;
                break;
            }
        }

        // Segunda pasada: si no se encontró y no requiere preferencial, buscar cualquier espacio disponible del tipo correcto
        if (availableSpace == null && !preferentialNeeded) {
            for (ParkingSpace space : parkingLot.getSpaces()) {
                if (space == null) continue;

                if (!space.isState() && space.getSpaceType() == vehicle.getVehicleType().getSpaceType()) {
                    availableSpace = space;
                    break;
                }
            }
        }

        return availableSpace;
    }

    private boolean checkForPreferentialNeeded(Vehicle vehicle) {
        // Validar que el vehículo tenga owner y que este tenga la propiedad de discapacidad
        if (vehicle == null || vehicle.getOwner() == null) {
            return false;
        }
        boolean disabilityNeeded=false;
        for (Customer owner : vehicle.getOwners()) {
            if (owner.isDisability()) {
                disabilityNeeded=true;
                break;
            }
        }
        return disabilityNeeded;
    }

    public OperationResult exitVehicle(String licensePlate) throws IOException {

        //Se busca el vehículo
        Vehicle vehicle = vehicleController.findVehicleByPlate(licensePlate);
        if (vehicle == null) {
            return OperationResult.failure("Vehículo no encontrado");
        }

        //Se valida el estado
        if (vehicle.getVehicleStatus() != VehicleStatus.PARKED) {
            return OperationResult.failure("Vehículo no está estacionado");
        }

        ParkingTicket ticket = vehicle.getTicket();
        if (ticket == null) {
            return OperationResult.failure("No se encontró un ticket asociado al vehículo");
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
                "El vehículo ha salido del parqueo. Total a pagar: " + totalAmount);
    }

}

