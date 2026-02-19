package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.mappers;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.controller.UserController;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.ParkingSpaceData;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.XmlEntityMapper;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.*;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import java.io.IOException;

public class ParkingLotXmlMapper implements XmlEntityMapper<ParkingLot> {
        private final String ID_ATTRIBUTE = "parkingLotId";
        private final String NAME_ATTRIBUTE = "name";
        private final String STATUS_ATTRIBUTE = "active";
        private final String ADMIN_ID = "adminId";
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
            return e;
        }
        @Override
        public ParkingLot fromElement(Element element) {
            ParkingLot parkingLot = new ParkingLot();//usado para construir el objeto mas abstracto
            int id = Integer.parseInt(element.getAttributeValue(ID_ATTRIBUTE));
            String name = element.getChildText(NAME_ATTRIBUTE);
            boolean status = castStatus(element.getChildText(STATUS_ATTRIBUTE));
            int adminId = Integer.parseInt(element.getChildText(ADMIN_ID));
            parkingLot.setParkingLotId(id);
            parkingLot.setName(name);
            parkingLot.setActive(status);
            Administrator admin = new Administrator();
            admin.setId(adminId);
            parkingLot.setAdministrator(admin);
//            parkingLot.setSpaces(getSpaces(parkingLot.getParkingLotId()));//TODO CONECTAR la entidad completa

            return parkingLot;
        }

    private boolean castStatus(String childText) {
        return childText.equals("true");
    }

    private ParkingSpace[] getSpaces(int lotId) throws IOException, JDOMException {
            ParkingSpaceData data = new ParkingSpaceData();
            return data.getAllParkingSpacesByLot(lotId).toArray(new ParkingSpace[0]);
    }
    private Administrator getAdmin(int id) throws IOException, JDOMException {
        UserController userController=new UserController();
        User user = userController.findById(id);
        Administrator admin=new Administrator();
        admin.setId(id);
        admin.setPassword(user.getPassword());
        admin.setUserName(user.getUserName());
        admin.setName(user.getName());

        return admin;
    }

}
