package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.mappers;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.XmlEntityMapper;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Rate;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.VehicleType;
import org.jdom2.Element;

import java.util.concurrent.TimeUnit;

public class RateXmlMapper implements XmlEntityMapper<Rate> {
private final String ID_ATTRIBUTE = "rateId";
private final String VEHICLE_TYPE_ATTRIBUTE = "vehicleTypeId";
private final String TIME_UNIT_ATTRIBUTE = "timeUnit";
private final String ACTIVE_ATTRIBUTE = "active";
private final String FEE = "fee";

    @Override
    public String elementName() {
        return "rate";
    }

    @Override
    public String idAttributeName() {
        return ID_ATTRIBUTE;
    }

    @Override
    public String getId(Rate entity) {
        return String.valueOf(entity.getRateId());
    }

    @Override
    public Element toElement(Rate entity) {
        Element element = new Element(elementName());
        element.setAttribute(idAttributeName(), getId(entity));
        element.addContent(new Element(VEHICLE_TYPE_ATTRIBUTE).setText(String.valueOf(entity.getVehicleType().getId())));
        element.addContent(new Element(FEE).setText(String.valueOf(entity.getFee())));
        element.addContent(new Element(TIME_UNIT_ATTRIBUTE).setText(entity.getTimeUnit().name()));
        element.addContent(new Element(ACTIVE_ATTRIBUTE).setText(entity.isActive()?"true":"false"));
        return element;
    }

    @Override
    public Rate fromElement(Element element) {
        int id = Integer.parseInt(element.getAttributeValue(idAttributeName()));
        double fee = Double.parseDouble(element.getChildText(FEE));
        TimeUnit timeUnit = TimeUnit.valueOf(element.getChildText(TIME_UNIT_ATTRIBUTE));
        VehicleType type = new VehicleType();
        type.setId(Integer.parseInt(element.getChildText(VEHICLE_TYPE_ATTRIBUTE)));//TODO CONECTAR la entidad completa
        boolean active = Boolean.parseBoolean(element.getChildText(ACTIVE_ATTRIBUTE));
        Rate rate = new Rate(id, type, timeUnit, fee);
        rate.setActive(active);
        return rate;
    }
}
