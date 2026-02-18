//package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller;
//
//import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.ParkingLotData;
//import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.ParkingTicketData;
//import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.PersistenceManager;
//import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.VehicleData;
//import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.*;
//import org.jdom2.JDOMException;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//public class ParkingOperationController {
//
//    private ParkingLotData parkingLotData;
//    private VehicleData vehicleData;
//    private ParkingTicketData ticketData;
//    private PersistenceManager persistenceManager;
//
//    public ParkingOperationController() {
//        this.persistenceManager = new PersistenceManager();
//        this.parkingLotData = persistenceManager.loadParkingLotData();
//        try {
//            this.vehicleData = new VehicleData();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (JDOMException e) {
//            throw new RuntimeException(e);
//        }
//        this.ticketData = persistenceManager.loadParkingTicketData();
//    }
//
//    public OperationResult parkVehicle(String parkingLotId, Vehicle vehicle) {
//
//        //Busca que el parqueo esté disponible
//        ParkingLot parkingLot = parkingLotData.findParkingLotById(parkingLotId);
//        if (parkingLot == null || !parkingLot.isActive()) {
//            return OperationResult.failure("Parking lot not available");
//        }
//
//        //Valida el estado del vehículo
//        if (vehicle.getVehicleStatus() == VehicleStatus.PARKED) {
//            return OperationResult.failure("Vehicle is already parked");
//        }
//
//        //busca un espacio disponible
//        ParkingSpace availableSpace = null;
//
//        for (ParkingSpace space : parkingLot.getSpaces()) {
//            if (!space.isState() &&
//                    space.getSpaceType() == vehicle.getVehicleType().getSpaceType()) {
//
//                availableSpace = space;
//                break;
//            }
//        }
//
//        if (availableSpace == null) {
//            return OperationResult.failure("No available space for this vehicle type");
//        }
//
//        double fee = vehicle.getVehicleType().getFee();
//
//        //Generar ticket
//        ParkingTicket ticket = new ParkingTicket(
//                UUID.randomUUID().toString(),
//                availableSpace,
//                LocalDateTime.now(),
//                new Rate(0, vehicle.getVehicleType(), null, fee)
//        );
//
//        //Se actualizan los estados
//        availableSpace.setState(true);
//        vehicle.setVehicleStatus(VehicleStatus.PARKED);
//        vehicle.setTicket(ticket);
//
//        //Se escribe en el json
//        ticketData.registerTicket(ticket);
//        vehicleData.editVehicle(vehicle);
//        parkingLotData.editParkingLot(parkingLot);
//
//        try {
//            persistenceManager.saveParkingTicketData(ticketData);
//            persistenceManager.saveVehicleData(vehicleData);
//            persistenceManager.saveParkingLotData(parkingLotData);
//        } catch (Exception e) {
//            return OperationResult.failure("Error saving parking data");
//        }
//
//        return OperationResult.success("Vehicle parked successfully", ticket);
//    }
//
//    public OperationResult exitVehicle(String licensePlate) {
//
//        //Se busca el vehículo
//        Vehicle vehicle = vehicleData.findVehicleByLicensePlate(licensePlate);
//        if (vehicle == null) {
//            return OperationResult.failure("Vehicle not found");
//        }
//
//        //Se valida el estado
//        if (vehicle.getVehicleStatus() != VehicleStatus.PARKED) {
//            return OperationResult.failure("Vehicle is not currently parked");
//        }
//
//        ParkingTicket ticket = vehicle.getTicket();
//        if (ticket == null) {
//            return OperationResult.failure("Parking ticket not found");
//        }
//
//        //Asignar hora de salida
//        ticket.setExitTime(LocalDateTime.now());
//
//        //Calcular tiempo estacionado
//        long minutesParked = java.time.Duration
//                .between(ticket.getEntryTime(), ticket.getExitTime())
//                .toMinutes();
//
//        long hoursToCharge = (long) Math.ceil(minutesParked / 60.0);
//
//        double hourlyFee = ticket.getRate().getFee();
//        double totalAmount = hoursToCharge * hourlyFee;
//
//        //Se libera el espacio
//        ParkingSpace space = ticket.getParkingSpace();
//        space.setState(false);
//
//        //Se actualiza el estado del vehículo
//        vehicle.setVehicleStatus(VehicleStatus.EXITED);
//        vehicle.setTicket(null);
//
//        //Escribir cambios
//        ticketData.updateTicket(ticket);
//        vehicleData.editVehicle(vehicle);
//
//        parkingLotData.editParkingLot(space.getParkingLot());
//
//        try {
//            persistenceManager.saveParkingTicketData(ticketData);
//            persistenceManager.saveVehicleData(vehicleData);
//            persistenceManager.saveParkingLotData(parkingLotData);
//        } catch (Exception e) {
//            return OperationResult.failure("Error saving exit operation");
//        }
//
//        return OperationResult.success(
//                "Vehicle exited successfully. Total amount: " + totalAmount);
//    }
//
//}
//
