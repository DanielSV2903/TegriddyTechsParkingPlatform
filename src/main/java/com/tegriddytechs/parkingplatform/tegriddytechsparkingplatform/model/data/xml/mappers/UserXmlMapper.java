package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.mappers;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.XmlEntityMapper;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.*;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

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
            Integer lotId = null;
            if (clerk.getParkingLot() != null) {
                lotId = clerk.getParkingLot().getParkingLotId();
            }
            e.addContent(new Element(PARKING_LOT_ID).setText(lotId != null ? String.valueOf(lotId) : ""));
            return e;
        }

        if (user instanceof Administrator admin) {
            String ids = joinParkingLotIds(admin.getParkingLots());
            e.addContent(new Element(PARKING_LOT_ID).setText(ids));
            return e;
        }
        e.addContent(new Element(PARKING_LOT_ID).setText(""));
        return e;
    }

    @Override
    public User fromElement(Element element) {
        User u = new User();
        User toReturn = u;

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

        String rawLotId = element.getChildText(PARKING_LOT_ID);
        rawLotId = rawLotId == null ? "" : rawLotId.trim();

        if (u.getUserRole() == UserRole.ADMIN) {
            Administrator admin = toAdmin(u);
            List<Integer> ids = parseIds(rawLotId);
            ArrayList<ParkingLot> lots = new ArrayList<>();
            for (Integer lotId : ids) {
                if (lotId == null) continue;
                lots.add(new ParkingLot(lotId, ""));
            }
            admin.setParkingLots(lots);

            toReturn = admin;

        } else if (u.getUserRole() == UserRole.CLERK) {
            Integer parkingLotId = null;
            if (!rawLotId.isBlank()) {
                // Para Clerk seguimos soportando solo un número
                parkingLotId = Integer.parseInt(rawLotId);
            }

            Clerk clerk = toClerk(u);
            clerk.setParkingLot(new ParkingLot(parkingLotId, ""));
            toReturn = clerk;
        }

        return toReturn;
    }

    private Administrator toAdmin(User u){
        return new Administrator(u.getId(), u.getName(), u.getUserName(), u.getPassword());
    }

    private Clerk toClerk(User u){
        return new Clerk(u.getId(), u.getName(), u.getUserName(), u.getPassword(), null);
    }

    private static String joinParkingLotIds(List<ParkingLot> lots) {
        if (lots == null || lots.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (ParkingLot lot : lots) {
            if (lot == null) continue;
            int id = lot.getParkingLotId();
            if (id <= 0) continue;
            if (!sb.isEmpty()) sb.append(';');
            sb.append(id);
        }
        return sb.toString();
    }

    private static List<Integer> parseIds(String raw) {
        List<Integer> ids = new ArrayList<>();
        if (raw == null || raw.isBlank()) return ids;

        String[] parts = raw.split(";");
        for (String p : parts) {
            if (p == null) continue;
            String t = p.trim();
            if (t.isEmpty()) continue;
                ids.add(Integer.parseInt(t));
        }
        return ids;
    }
}