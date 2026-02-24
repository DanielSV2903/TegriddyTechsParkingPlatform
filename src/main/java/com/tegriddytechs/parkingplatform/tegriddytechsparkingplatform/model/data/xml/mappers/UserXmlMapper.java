package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.mappers;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.XmlEntityMapper;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.*;
import org.jdom2.Element;

public class UserXmlMapper implements XmlEntityMapper<User> {
    private final String ID_ATTRIBUTE = "id";
    private final String NAME_ATTRIBUTE = "name";
    private final String USERNAME_ATTRIBUTE = "username";
    private final String PASSWORD_ATTRIBUTE = "password";
    private final String ROLE_ATTRIBUTE = "role";
    private final String PARKING_LOT_ID = "parkingLotId";

    @Override
    public String elementName() {
        return "user";
    }

    @Override
    public String idAttributeName() {
        return ID_ATTRIBUTE;
    }

    @Override
    public String getId(User entity) {
        return String.valueOf(entity.getId()); // ajusta si tu ID se llama distinto
    }

    @Override
    public Element toElement(User user) {
        Element e = new Element(elementName());
        e.setAttribute(idAttributeName(), String.valueOf(user.getId()));
        e.addContent(new Element(NAME_ATTRIBUTE).setText(user.getName()));
        e.addContent(new Element(USERNAME_ATTRIBUTE).setText(user.getUserName()));
        e.addContent(new Element(PASSWORD_ATTRIBUTE).setText(user.getPassword()));
        e.addContent(new Element(ROLE_ATTRIBUTE).setText(user.getUserRole().name()));
        if (user instanceof Clerk clerk) {
            Integer lotId = clerk.getParkingLot().getParkingLotId();
            if (lotId == null && clerk.getParkingLot() != null) {
                lotId = clerk.getParkingLot().getParkingLotId();
            }
            e.addContent(new Element(PARKING_LOT_ID).setText(lotId != null ? String.valueOf(lotId) : ""));
        } else {
            e.addContent(new Element(PARKING_LOT_ID).setText(""));
        }
        return e;
    }

    @Override
    public User fromElement(Element element) {
        User u = new User();//usado para construir el objeto mas abstracto
        User toReturn = u;//Cambia su tipo dependiendo del rol
        int id = Integer.parseInt(element.getAttributeValue(ID_ATTRIBUTE));
        String name = element.getChildText(NAME_ATTRIBUTE);
        String username = element.getChildText(USERNAME_ATTRIBUTE);
        String password = element.getChildText(PASSWORD_ATTRIBUTE);
        UserRole role = UserRole.valueOf(element.getChildText(ROLE_ATTRIBUTE));

        u.setId(id);
        u.setName(name);
        u.setUserName(username);
        u.setPassword(password);
        u.setUserRole(role);
        if (u.getUserRole().name().equals(UserRole.ADMIN.getRole())) {
            toReturn = toAdmin(u);
        } else if (u.getUserRole().name().equals(UserRole.CLERK.getRole())) {
            Integer parkingLotId = null;
            String rawLotId = element.getChildText(PARKING_LOT_ID);
            if (rawLotId != null && !rawLotId.isBlank()) {
                parkingLotId = Integer.parseInt(rawLotId.trim());
            }

            Clerk clerk = toClerk(u);
            clerk.setParkingLot(new ParkingLot(parkingLotId, ""));
            toReturn = clerk;
        }
        return toReturn;
    }

    private Administrator toAdmin(User u){
        Administrator admin = new Administrator(u.getId(),u.getName(),u.getUserName(),u.getPassword());
        return admin;
    }

    private Clerk toClerk(User u){
        Clerk clerk = new Clerk(u.getId(),u.getName(),u.getUserName(),u.getPassword(),null);
        return clerk;
    }
}