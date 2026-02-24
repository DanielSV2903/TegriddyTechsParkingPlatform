package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.mappers;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller.VehicleController;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.VehicleData;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.XmlEntityMapper;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Customer;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.User;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.UserRole;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Vehicle;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import java.io.IOException;

public class CustomerXmlMapper implements XmlEntityMapper< Customer> {
    private final String ID_ATTRIBUTE = "id";
    private final String NAME_ATTRIBUTE = "name";
    private final String DISABILITY_ATTRIBUTE = "disability";
    private final String AGE_ATTRIBUTE = "age";
    private final String VEHICLE_ID_ATTRIBUTE = "vehiceId";

    @Override
    public String elementName() {
        return "customer";
    }

    @Override
    public String idAttributeName() {
        return ID_ATTRIBUTE;
    }

    @Override
    public String getId(Customer entity) {
        return String.valueOf(entity.getId());
    }

    @Override
    public Element toElement(Customer entity) {
        Element e = new Element(elementName());
        e.setAttribute(idAttributeName(), String.valueOf(entity.getId()));
        e.addContent(new Element(NAME_ATTRIBUTE).setText(entity.getName()));
        e.addContent(new Element(DISABILITY_ATTRIBUTE).setText(entity.isDisability() ? "true" : "false"));
//        e.addContent(new Element(AGE_ATTRIBUTE).setText(String.valueOf(entity.getAge())));
        e.addContent(new Element(VEHICLE_ID_ATTRIBUTE).setText(entity.getVehicle()!=null?entity.getVehicle().getPlate():""));
        return e;
    }

    @Override
    public Customer fromElement(Element element){
        Customer customer = new Customer();//usado para construir el objeto mas abstracto
        int id = Integer.parseInt(element.getAttributeValue(ID_ATTRIBUTE));
        String name = element.getChildText(NAME_ATTRIBUTE);
        boolean disability = castDisability(element.getChildText(DISABILITY_ATTRIBUTE));
        String vehiclePlate = element.getChildText(VEHICLE_ID_ATTRIBUTE);

        customer.setId(id);
        customer.setName(name);
        Vehicle vehicle = new Vehicle();
        vehicle.setPlate(vehiclePlate);
        customer.setVehicle(vehicle);
        customer.setDisability(disability);

        return customer;
    }

    private boolean castDisability(String childText) {
        return childText.equals("true");
    }

}
