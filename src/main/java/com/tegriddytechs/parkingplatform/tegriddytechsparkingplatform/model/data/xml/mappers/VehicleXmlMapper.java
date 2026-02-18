package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.mappers;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.XmlEntityMapper;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.*;
import org.jdom2.Element;

public class VehicleXmlMapper implements XmlEntityMapper<Vehicle> {
    private final String ID_ATTRIBUTE = "plate";
    private final String VEHICLE_TYPE_ID = "vehicleTypeId";
    private final String VEHICLE_STATUS = "status";
    private final String OWNER = "ownerId";

    @Override
    public String elementName() {
        return "vehicles";
    }

    @Override
    public String idAttributeName() {
        return ID_ATTRIBUTE;
    }

    @Override
    public String getId(Vehicle entity) {
        return "";
    }

    @Override
    public Element toElement(Vehicle entity) {
        Element e = new Element(elementName());
        e.setAttribute(idAttributeName(), String.valueOf(entity.getPlate()));
        e.addContent(new Element(VEHICLE_TYPE_ID).setText(String.valueOf(entity.getVehicleType().getId())));
//        e.addContent(new Element(DISABILITY_ATTRIBUTE).setText(entity.isDisabledPermit() ? "true" : "false"));
        e.addContent(new Element(OWNER).setText(String.valueOf(entity.getOwner().getId())));
        e.addContent(new Element(VEHICLE_STATUS).setText(entity.getVehicleStatus().getEstado()));
        return e;
    }

    @Override
    public Vehicle fromElement(Element element) {
        Vehicle vehicle = new Vehicle();//usado para construir el objeto mas abstracto
        String vehiclePlate = element.getChildText(ID_ATTRIBUTE);
        int typeId = Integer.parseInt(element.getChildText(VEHICLE_TYPE_ID));
        VehicleStatus status = castStatus(element.getChildText(VEHICLE_STATUS));

        vehicle.setPlate(vehiclePlate);
        vehicle.setVehicleType(new VehicleType());//TODO CONECTAR la entidad completa
        vehicle.setVehicleStatus(status);

        return vehicle;
    }


    private VehicleStatus castStatus(String childText) {
        return VehicleStatus.valueOf(childText);
    }
}
