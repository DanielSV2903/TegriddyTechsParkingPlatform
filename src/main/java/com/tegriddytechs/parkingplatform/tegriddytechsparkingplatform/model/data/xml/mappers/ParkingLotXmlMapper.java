package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.mappers;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.XmlEntityMapper;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.*;
import org.jdom2.Element;

public class ParkingLotXmlMapper implements XmlEntityMapper<ParkingLot> {
        private final String ID_ATTRIBUTE = "parkingLotId";
        private final String NAME_ATTRIBUTE = "name";
        private final String STATUS_ATTRIBUTE = "active";
        private final String ADMIN_ID = "adminId";
        private final String SIZE = "size";//Para medir el tamaño del parqueo (ParkingSpace)

        @Override
        public String elementName() {
            return "parkingLot";
        }

        @Override
        public String idAttributeName() {
            return ID_ATTRIBUTE;
        }

        @Override
        public String getId(ParkingLot entity) {
            return String.valueOf(entity.getParkingLotId());
        }

        @Override
        public Element toElement(ParkingLot parkingLot) {
            Element e = new Element(elementName());
            e.setAttribute(idAttributeName(), String.valueOf(parkingLot.getParkingLotId()));
            e.addContent(new Element(NAME_ATTRIBUTE).setText(parkingLot.getName()));
            e.addContent(new Element(STATUS_ATTRIBUTE).setText(parkingLot.isActive()?"true":"false"));
            e.addContent(new Element(ADMIN_ID).setText(String.valueOf(parkingLot.getAdministrator().getId())));
            e.addContent(new Element(SIZE).setText(parkingLot.getSpaces().length+""));
            return e;
        }
        @Override
        public ParkingLot fromElement(Element element) {
            ParkingLot parkingLot = new ParkingLot();//usado para construir el objeto mas abstracto
            int id = Integer.parseInt(element.getAttributeValue(ID_ATTRIBUTE));
            String name = element.getChildText(NAME_ATTRIBUTE);
            boolean status = castStatus(element.getChildText(STATUS_ATTRIBUTE));
            int adminId = Integer.parseInt(element.getChildText(ADMIN_ID));
            int size = Integer.parseInt(element.getChildText(SIZE));
            parkingLot.setParkingLotId(id);
            parkingLot.setName(name);
            parkingLot.setActive(status);
            Administrator admin = new Administrator();
            admin.setId(adminId);
            parkingLot.setAdministrator(admin);
            parkingLot.setSpaces(new ParkingSpace[size]);//TODO CONECTAR la entidad completa

            return parkingLot;
        }

    private boolean castStatus(String childText) {
        return childText.equals("true");
    }

}
