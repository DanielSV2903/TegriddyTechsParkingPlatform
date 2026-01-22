package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Vehicle;

import java.util.ArrayList;

public class VehicleData {
    private ArrayList <Vehicle> vehicles;
    private transient PersistenceManager persistenceManager;

    public VehicleData() {
        this.vehicles = new ArrayList<>();
        this.persistenceManager = new PersistenceManager();
    }
    public void save() {
        try {
            persistenceManager.saveVehicleData(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public ArrayList <Vehicle> getAllVehicles() {
        return vehicles;
    }
    public Vehicle findVehicleByLicensePlate(String licensePlate) {
        Vehicle vehicle=null;
        for (Vehicle actualVehicle : vehicles) {
            if (actualVehicle.getPlate().equals(licensePlate)) {
                vehicle= actualVehicle;
            }
        }
        return vehicle;
    }
    public void removeVehicle(Vehicle vehicle) {
        this.vehicles.remove(vehicle);
        save();
    }
    public void registerVehicle(Vehicle vehicle) {
        this.vehicles.add(vehicle);//Agregar vehiculo a la lista
        save();
    }

    //TODO: Edit vehicle
    /*
        Este metodo recibe un vehiculo con las nuevas caracteristicas y busca el vehiculo existente por su placa
        Importante: La placa no puede ser editada, ya que es el identificador unico del vehiculo
        Si el vehiculo existe, se elimina de la lista el vehiculo existente y se agrega el vehiculo con las nuevas caracteristicas
     */
    public void editVehicle(Vehicle vehicle) {
        Vehicle existingVehicle = findVehicleByLicensePlate(vehicle.getPlate());
        if (existingVehicle != null) {
            vehicles.remove(existingVehicle);//Aqui se quita de la lista el vehiculo existente, con las caracteristicas viejas
            vehicles.add(vehicle);//Aqui se agrega el vehiculo con las nuevas caracteristicas
            save();
        }
    }
}
