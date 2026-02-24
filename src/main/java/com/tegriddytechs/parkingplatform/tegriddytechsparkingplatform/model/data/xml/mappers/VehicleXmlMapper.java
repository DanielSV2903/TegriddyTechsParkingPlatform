package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.mappers;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.XmlEntityMapper;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.*;
import org.jdom2.Element;

import java.lang.ref.PhantomReference;

public class VehicleXmlMapper implements XmlEntityMapper<Vehicle> {
    private final String ID_ATTRIBUTE = "plate";
    private final String VEHICLE_TYPE_ID = "vehicleTypeId";
    private final String VEHICLE_STATUS = "status";
    private final String BRAND = "brand";
    private final String MODEL = "model";
    private final String COLOR = "color";
    private final String OWNER = "ownerId";
    private final String TICKET_ID = "ticketId";

    @Override
    public String elementName() {
        return "vehicle";
    }

    @Override
    public String idAttributeName() {
        return ID_ATTRIBUTE;
    }

    @Override
    public String getId(Vehicle entity) {
        return entity.getPlate()+"";
    }

    @Override
    public Element toElement(Vehicle entity) {
        Element e = new Element(elementName());
        e.setAttribute(idAttributeName(),entity.getPlate());
        e.addContent(new Element(VEHICLE_TYPE_ID).setText(String.valueOf(entity.getVehicleType().getId())));
//        e.addContent(new Element(DISABILITY_ATTRIBUTE).setText(entity.isDisabledPermit() ? "true" : "false"));
        e.addContent(new Element(BRAND).setText(entity.getBrand()));
        e.addContent(new Element(MODEL).setText(entity.getModel()));
        e.addContent(new Element(COLOR).setText(entity.getColor()));
        e.addContent(new Element(OWNER).setText(String.valueOf(entity.getOwner().getId())));
        e.addContent(new Element(VEHICLE_STATUS).setText(entity.getVehicleStatus().getEstado()));
        e.addContent(new Element(TICKET_ID).setText(entity.getTicket()!=null?entity.getTicket().getTicketId():""));
        return e;
    }

    @Override
    public Vehicle fromElement(Element element) {
        Vehicle vehicle = new Vehicle();//usado para construir el objeto mas abstracto
        String vehiclePlate = element.getAttributeValue(ID_ATTRIBUTE);
        int typeId = Integer.parseInt(element.getChildText(VEHICLE_TYPE_ID));
        VehicleStatus status = castStatus(element.getChildText(VEHICLE_STATUS));
        String brand = element.getChildText(BRAND);
        String model = element.getChildText(MODEL);
        String color = element.getChildText(COLOR);
        String ticketId = element.getChildText(TICKET_ID);
        ParkingTicket ticket=new ParkingTicket();
        ticket.setTicketId(ticketId);
        int ownerId = Integer.parseInt(element.getChildText(OWNER));
        Customer owner = new Customer();
        owner.setId(ownerId);
        vehicle.setOwner(owner);
        VehicleType type = new VehicleType();
        type.setId(typeId);//TODO CONECTAR la entidad completa
        vehicle.setPlate(vehiclePlate);
        vehicle.setVehicleType(type);
        vehicle.setTicket(ticket);
        vehicle.setVehicleStatus(status);
        vehicle.setBrand(brand);
        vehicle.setModel(model);
        vehicle.setColor(color);

        return vehicle;
    }


    private VehicleStatus castStatus(String childText) {
        if (childText.equals(VehicleStatus.EXITED.getEstado())) return VehicleStatus.EXITED;
        return VehicleStatus.PARKED;
    }
}
