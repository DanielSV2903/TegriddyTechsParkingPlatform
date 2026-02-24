package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.mappers;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.XmlEntityMapper;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.*;
import org.jdom2.Element;

import java.time.LocalDateTime;

public class ParkingSpaceXmlMapper implements XmlEntityMapper<ParkingSpace> {
    private final String ID_ATTRIBUTE = "parkingLotId";
    private final String SPACE_NUMBER = "spaceNumber";
    private final String STATUS_ATTRIBUTE = "state";
    private final String PREFERENTIAL_ATTRIBUTE = "isPreferential";
    private final String TYPE = "type";
    private final String VEHICLE_PLATE = "vehiclePlate";//Para medir el tamaño del parqueo (ParkingSpace)


    @Override
    public String elementName() {
        return "parkingSpace";
    }

    @Override
    public String idAttributeName() {
        return ID_ATTRIBUTE;
    }

    @Override
    public String getId(ParkingSpace entity) {
        return entity.getParkingLot().getParkingLotId()+"|"+(entity.getSpaceNumber());
    }

    @Override
    public Element toElement(ParkingSpace parkingSpace) {
        Element e = new Element(elementName());
        e.setAttribute(idAttributeName(), parkingSpace.getParkingLot().getParkingLotId()+"|"+(parkingSpace.getSpaceNumber()));
        e.addContent(new Element(SPACE_NUMBER).setText(parkingSpace.getSpaceNumber()+""));
        e.addContent(new Element(STATUS_ATTRIBUTE).setText(parkingSpace.isState()?"true":"false"));
        e.addContent(new Element(PREFERENTIAL_ATTRIBUTE).setText(parkingSpace.isPreferential()?"true":"false"));
        e.addContent(new Element(TYPE).setText(parkingSpace.getSpaceType().name()+""));
        return e;
    }
    @Override
    public ParkingSpace fromElement(Element element) {
        ParkingSpace parkingSpace = new ParkingSpace();//usado para construir el objeto mas abstracto
        int id = Integer.parseInt(element.getAttributeValue(ID_ATTRIBUTE).split("|")[0]);
        int spaceNumber = Integer.parseInt(element.getChildText(SPACE_NUMBER));
        boolean status = castStatus(element.getChildText(STATUS_ATTRIBUTE));
        boolean preferential = castStatus(element.getChildText(PREFERENTIAL_ATTRIBUTE));
        SpaceType type = SpaceType.valueOf(element.getChildText(TYPE));
        String plate = element.getChildText(VEHICLE_PLATE);
        ParkingLot lot = new ParkingLot();//TODO CONECTAR la entidad completa
        lot.setParkingLotId(id);
        parkingSpace.setParkingLot(lot);
        parkingSpace.setSpaceNumber(spaceNumber);
        parkingSpace.setState(status);
        parkingSpace.setPreferential(preferential);
        Vehicle parkedVehicle = new Vehicle(plate);//TODO CONECTAR la entidad completa
        parkingSpace.setParkedVehicle(parkedVehicle);
        parkingSpace.setSpaceType(type);
        return parkingSpace;
    }

    private boolean castStatus(String childText) {
        return childText.equals("true");
    }

}
