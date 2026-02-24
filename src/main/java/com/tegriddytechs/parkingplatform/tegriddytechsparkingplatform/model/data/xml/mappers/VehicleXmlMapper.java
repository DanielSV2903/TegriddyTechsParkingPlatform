package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.mappers;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.XmlEntityMapper;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.*;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class VehicleXmlMapper implements XmlEntityMapper<Vehicle> {
    private final String ID_ATTRIBUTE = "plate";
    private final String VEHICLE_TYPE_ID = "vehicleTypeId";
    private final String VEHICLE_STATUS = "status";
    private final String BRAND = "brand";
    private final String MODEL = "model";
    private final String COLOR = "color";
    private final String OWNERS = "ownerId";
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
        e.addContent(new Element(OWNERS).setText(ownersIds(entity)));
        e.addContent(new Element(VEHICLE_STATUS).setText(entity.getVehicleStatus().getEstado()));
        e.addContent(new Element(TICKET_ID).setText(entity.getTicket()!=null?entity.getTicket().getTicketId():""));
        return e;
    }

    private String ownersIds(Vehicle entity) {
        StringBuilder ids=new StringBuilder();
        for (Customer owner: entity.getOwners())
            ids.append(owner.getId()+";");
        return ids.toString();
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
        String [] ownersIds =  element.getChildText(OWNERS).split(";");
        List<Customer> owners = getOwners(ownersIds);
        vehicle.setOwners(owners);
        VehicleType type = new VehicleType();
        type.setId(typeId);
        vehicle.setPlate(vehiclePlate);
        vehicle.setVehicleType(type);
        vehicle.setTicket(ticket);
        vehicle.setVehicleStatus(status);
        vehicle.setBrand(brand);
        vehicle.setModel(model);
        vehicle.setColor(color);

        return vehicle;
    }

    private List<Customer> getOwners(String[] ownersIds) {
        List<Customer> owners=new ArrayList<>();
        for (String id: ownersIds) {
            Customer c = new Customer();
            c.setId(Integer.parseInt(id));
            owners.add(c);
        }
        return owners;
    }


    private VehicleStatus castStatus(String childText) {
        if (childText.equals(VehicleStatus.EXITED.getEstado())) return VehicleStatus.EXITED;
        return VehicleStatus.PARKED;
    }
}
