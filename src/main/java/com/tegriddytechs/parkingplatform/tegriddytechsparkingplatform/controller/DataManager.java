package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.*;
import org.jdom2.JDOMException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Carga todos los controllers (que a su vez cargan datos desde persistencia)
 * y luego reconstruye/conecta referencias entre entidades.
 *
 * intenta reemplazar esas referencias por las instancias cargadas
 * por los controllers.
 */
public class DataManager {

    private final UserController userController;
    private final CustomerController customerController;
    private final VehicleTypeController vehicleTypeController;
    private final RateController rateController;
    private final ParkingLotController parkingLotController;
    private final ParkingSpaceController parkingSpaceController;
    private final ParkingTicketController parkingTicketController;
    private final VehicleController vehicleController;

    public DataManager() throws IOException, JDOMException {
        this.userController = new UserController();
        this.customerController = new CustomerController();
        this.vehicleTypeController = new VehicleTypeController();
        this.rateController = new RateController();
        this.parkingLotController = new ParkingLotController();
        this.parkingSpaceController = new ParkingSpaceController();
        this.parkingTicketController = new ParkingTicketController();
        this.vehicleController = new VehicleController();

        connectAll();
    }

    /**
     * Ejecuta todas las conexiones entre objetos.
     * Llamar esto si re-cargas datos desde XML o si sospechas que hay referencias desactualizadas.
     */
    public void connectAll() {
        List<Customer> customers = customerController.getAllCustomers();
        List<Vehicle> vehicles = vehicleController.getAllVehicles();
        List<VehicleType> vehicleTypes = vehicleTypeController.getAllVehicleTypes();
        List<Rate> rates = rateController.getAllRates();
        List<ParkingLot> lots = parkingLotController.getAllParkingLots();
        List<ParkingTicket> tickets = parkingTicketController.getAllTickets();

        Map<Integer, VehicleType> vehicleTypeById = indexVehicleTypes(vehicleTypes);
        Map<Integer, Rate> rateById = indexRates(rates);
        Map<String, ParkingTicket> ticketById = indexTickets(tickets);
        Map<Integer, ParkingLot> lotById = indexParkingLots(lots);
        Map<String, Vehicle> vehicleByPlate = indexVehiclesByPlate(vehicles);
        Map<Integer, Customer> customerById = indexCustomersById(customers);

        // 1) ParkingLot <-> ParkingSpace: asegurar parkingLot en cada space
        connectParkingLotsAndSpaces(lots, lotById);

        // 2) Rate -> VehicleType: reemplazar vehicleType por el "canónico"
        connectRatesToVehicleTypes(rates, vehicleTypeById);

        // 3) ParkingTicket -> Rate y ParkingSpace
        connectTickets(tickets, rateById, lots);

        // 4) Customer <-> Vehicle (bidireccional)
        connectCustomersAndVehicles(customers, vehicles, vehicleByPlate, customerById);

        // 5) Vehicle -> Ticket (reemplazar por ticket canónico) + Space.parkedVehicle
        connectVehiclesTicketsAndSpaces(vehicles, ticketById, lots);
        // 6) Clerk -> ParkingLot
        connectClerksToParkingLots(lotById);
    }

    private void connectClerksToParkingLots(Map<Integer, ParkingLot> lotById) {
        List<User> users = userController.getAllUsers();
        if (users == null) return;

        for (User u : users) {
            if (!(u instanceof Clerk clerk)) continue;

            Integer lotId = clerk.getParkingLot().getParkingLotId();
            if (lotId == null) continue;

            ParkingLot canonicalLot = lotById.get(lotId);
            if (canonicalLot != null) {
                clerk.setParkingLot(canonicalLot);
            }
        }
    }


    private void connectParkingLotsAndSpaces(List<ParkingLot> lots, Map<Integer, ParkingLot> lotById) {
        for (ParkingLot lot : lots) {
            if (lot == null) continue;

            ParkingLot canonicalLot = lotById.get(lot.getParkingLotId());
            if (canonicalLot == null) canonicalLot = lot;

            ParkingSpace[] spaces = canonicalLot.getSpaces();
            if (spaces == null) continue;

            for (ParkingSpace space : spaces) {
                if (space == null) continue;
                space.setParkingLot(canonicalLot);
            }
        }
    }

    private void connectRatesToVehicleTypes(List<Rate> rates, Map<Integer, VehicleType> vehicleTypeById) {
        for (Rate rate : rates) {
            if (rate == null) continue;

            VehicleType vt = rate.getVehicleType();
            if (vt == null) continue;

            VehicleType canonical = vehicleTypeById.get(vt.getId());
            if (canonical != null) {
                rate.setVehicleType(canonical);
            }
        }
    }

    private void connectTickets(List<ParkingTicket> tickets,
                                Map<Integer, Rate> rateById,
                                List<ParkingLot> lots) {
        for (ParkingTicket ticket : tickets) {
            if (ticket == null) continue;

            // Ticket -> Rate (por id si existe)
            Rate r = ticket.getRate();
            if (r != null) {
                Rate canonicalRate = rateById.get(r.getRateId());
                if (canonicalRate != null) {
                    ticket.setRate(canonicalRate);
                }
            }

            // Ticket -> ParkingSpace (resolver la instancia canónica del espacio)
            ParkingSpace resolvedSpace = resolveSpace(ticket.getParkingSpace(), lots);
            if (resolvedSpace != null) {
                ticket.setParkingSpace(resolvedSpace);
            }
        }
    }

    private void connectCustomersAndVehicles(List<Customer> customers,
                                             List<Vehicle> vehicles,
                                             Map<String, Vehicle> vehicleByPlate,
                                             Map<Integer, Customer> customerById) {

        // A) Primero, si Customer trae Vehicle (aunque sea parcial), reemplazarlo por el canónico por placa
        for (Customer customer : customers) {
            if (customer == null) continue;

            Vehicle v = customer.getVehicle();
            if (v == null) continue;

            String plate = safePlate(v);
            if (plate == null) continue;

            Vehicle canonicalVehicle = vehicleByPlate.get(plate);
            if (canonicalVehicle != null) {
                customer.setVehicle(canonicalVehicle);
            }
        }

        // B) Luego, asegurar Vehicle.owner (Customer) y consistencia bidireccional
        for (Vehicle vehicle : vehicles) {
            if (vehicle == null) continue;

            Customer owner = vehicle.getOwner();

            // Si el owner viene (parcial) con id, intentamos canónicamente por id
            if (owner != null) {
                Customer canonicalOwner = customerById.get(owner.getId());
                if (canonicalOwner != null) {
                    vehicle.setOwner(canonicalOwner);
                    if (canonicalOwner.getVehicle() == null) {
                        canonicalOwner.setVehicle(vehicle);
                    }
                    continue;
                }
            }

            // Si no hay owner (o no pudimos resolverlo), intentamos deducirlo desde Customer.vehicle.plate
            String plate = safePlate(vehicle);
            if (plate == null) continue;

            for (Customer customer : customers) {
                if (customer == null || customer.getVehicle() == null) continue;
                String cPlate = safePlate(customer.getVehicle());
                if (plate.equalsIgnoreCase(cPlate)) {
                    vehicle.setOwner(customer);
                    if (customer.getVehicle() != vehicle) {
                        customer.setVehicle(vehicle);
                    }
                    break;
                }
            }
        }
    }

    private void connectVehiclesTicketsAndSpaces(List<Vehicle> vehicles,
                                                Map<String, ParkingTicket> ticketById,
                                                List<ParkingLot> lots) {

        for (Vehicle vehicle : vehicles) {
            if (vehicle == null) continue;

            ParkingTicket t = vehicle.getTicket();
            if (t == null) continue;

            ParkingTicket canonicalTicket = ticketById.get(t.getTicketId());
            if (canonicalTicket != null) {
                vehicle.setTicket(canonicalTicket);
                t = canonicalTicket;
            }

            // Si el ticket tiene espacio, conectarlo al canónico y marcar parkedVehicle
            ParkingSpace resolvedSpace = resolveSpace(t.getParkingSpace(), lots);
            if (resolvedSpace != null) {
                t.setParkingSpace(resolvedSpace);
                resolvedSpace.setParkedVehicle(vehicle);
                resolvedSpace.setState(true);
            }
        }
    }

    // -----------------------
    // Resoluciones e índices
    // -----------------------

    private Map<Integer, VehicleType> indexVehicleTypes(List<VehicleType> vehicleTypes) {
        Map<Integer, VehicleType> map = new HashMap<>();
        for (VehicleType vt : vehicleTypes) {
            if (vt == null) continue;
            map.put(vt.getId(), vt);
        }
        return map;
    }

    private Map<Integer, Rate> indexRates(List<Rate> rates) {
        Map<Integer, Rate> map = new HashMap<>();
        for (Rate r : rates) {
            if (r == null) continue;
            map.put(r.getRateId(), r);
        }
        return map;
    }

    private Map<String, ParkingTicket> indexTickets(List<ParkingTicket> tickets) {
        Map<String, ParkingTicket> map = new HashMap<>();
        for (ParkingTicket t : tickets) {
            if (t == null || t.getTicketId() == null) continue;
            map.put(t.getTicketId(), t);
        }
        return map;
    }

    private Map<Integer, ParkingLot> indexParkingLots(List<ParkingLot> lots) {
        Map<Integer, ParkingLot> map = new HashMap<>();
        for (ParkingLot lot : lots) {
            if (lot == null) continue;
            map.put(lot.getParkingLotId(), lot);
        }
        return map;
    }

    private Map<String, Vehicle> indexVehiclesByPlate(List<Vehicle> vehicles) {
        Map<String, Vehicle> map = new HashMap<>();
        for (Vehicle v : vehicles) {
            String plate = safePlate(v);
            if (plate == null) continue;
            map.put(plate.toUpperCase(), v);
        }
        return map;
    }

    private Map<Integer, Customer> indexCustomersById(List<Customer> customers) {
        Map<Integer, Customer> map = new HashMap<>();
        for (Customer c : customers) {
            if (c == null) continue;
            map.put(c.getId(), c);
        }
        return map;
    }

    /**
     * Intenta encontrar el ParkingSpace "real" dentro de los ParkingLots cargados.
     * Como no vemos aquí los IDs exactos que guardas en XML, esta función hace una resolución
     * por (parkingLotId + spaceNumber) si es posible, y si no, por spaceNumber + atributos.
     */
    private ParkingSpace resolveSpace(ParkingSpace candidate, List<ParkingLot> lots) {
        if (candidate == null) return null;

        Integer lotId = null;
        if (candidate.getParkingLot() != null) {
            lotId = candidate.getParkingLot().getParkingLotId();
        }

        // A) Si tenemos parkingLotId, buscar directo dentro de ese lote
        if (lotId != null) {
            for (ParkingLot lot : lots) {
                if (lot == null || lot.getSpaces() == null) continue;
                if (lot.getParkingLotId() != lotId) continue;

                for (ParkingSpace s : lot.getSpaces()) {
                    if (s == null) continue;
                    if (s.getSpaceNumber() == candidate.getSpaceNumber()) {
                        return s;
                    }
                }
            }
        }

        // B) Fallback: buscar por coincidencia fuerte (spaceNumber + spaceType + preferential)
        for (ParkingLot lot : lots) {
            if (lot == null || lot.getSpaces() == null) continue;

            for (ParkingSpace s : lot.getSpaces()) {
                if (s == null) continue;

                boolean sameNumber = s.getSpaceNumber() == candidate.getSpaceNumber();
                boolean sameType = s.getSpaceType() == candidate.getSpaceType();
                boolean samePref = s.isPreferential() == candidate.isPreferential();

                if (sameNumber && sameType && samePref) {
                    return s;
                }
            }
        }

        return null;
    }

    private String safePlate(Vehicle v) {
        if (v == null) return null;
        String plate = v.getPlate();
        if (plate == null || plate.isBlank()) plate = v.getLicensePlate();
        if (plate == null || plate.isBlank()) return null;
        return plate.trim();
    }



    public UserController getUserController() {
        return userController;
    }

    public CustomerController getCustomerController() {
        return customerController;
    }

    public VehicleTypeController getVehicleTypeController() {
        return vehicleTypeController;
    }

    public RateController getRateController() {
        return rateController;
    }

    public ParkingLotController getParkingLotController() {
        return parkingLotController;
    }

    public ParkingSpaceController getParkingSpaceController() {
        return parkingSpaceController;
    }

    public ParkingTicketController getParkingTicketController() {
        return parkingTicketController;
    }

    public VehicleController getVehicleController() {
        return vehicleController;
    }
}
