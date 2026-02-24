package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.mappers;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.XmlEntityMapper;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingLot;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingSpace;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.ParkingTicket;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Rate;
import org.jdom2.Element;

import java.time.LocalDateTime;

public class ParkingTicketXmlMapper implements XmlEntityMapper<ParkingTicket> {
    private final String ID_ATTRIBUTE = "parkingTicketId";
    private final String LOT_ID = "parkingLotId";
    private final String SPACE_NUMBER = "spaceNumber";
    private final String ENTRY_TIME = "entryTime";
    private final String EXIT_TIME = "exitTime";
    private final String RATE_ID = "rateID";
    private final String AMOUNT = "amount";


    @Override
    public String elementName() {
        return "parkingTicket";
    }

    @Override
    public String idAttributeName() {
        return ID_ATTRIBUTE;
    }

    @Override
    public String getId(ParkingTicket entity) {
        return String.valueOf(entity.getTicketId());
    }

    @Override
    public Element toElement(ParkingTicket entity) {
        Element e = new Element(elementName());
        e.setAttribute(idAttributeName(), entity.getTicketId());
        e.addContent(new Element(LOT_ID).setText(entity.getParkingSpace().getParkingLot().getParkingLotId()+""));
        e.addContent(new Element(SPACE_NUMBER).setText(entity.getParkingSpace().getSpaceNumber()+""));
        e.addContent(new Element(ENTRY_TIME).setText(entity.getEntryTime()!= null ?entity.getEntryTime().toString():""));
        e.addContent(new Element(EXIT_TIME).setText(entity.getExitTime() != null ? entity.getExitTime().toString() : ""));
        e.addContent(new Element(RATE_ID).setText(entity.getRate().getRateId()+""));
        e.addContent(new Element(AMOUNT).setText(entity.getAmountPaid()+""));
        return e;
    }

    @Override
    public ParkingTicket fromElement(Element element) {
       String  id =element.getAttributeValue(ID_ATTRIBUTE);
        int lotId = Integer.parseInt(element.getChildText(LOT_ID));
        int spaceNumber= Integer.parseInt(element.getChildText(SPACE_NUMBER));
        double amountPaid=Double.parseDouble(element.getChildText(AMOUNT));
        LocalDateTime entryTime= LocalDateTime.parse(element.getChildText(ENTRY_TIME));
        LocalDateTime exitTime=null;
        if(element.getChildText(EXIT_TIME).length()>2){
            exitTime= LocalDateTime.parse(element.getChildText(EXIT_TIME));
        }
        int rateId = Integer.parseInt(element.getChildText(RATE_ID));
        ParkingLot lot = new ParkingLot();
        lot.setParkingLotId(lotId);
        Rate rate = new Rate();
        rate.setRateId(rateId);
        ParkingSpace space = new ParkingSpace();
        space.setSpaceNumber(spaceNumber);
        space.setParkingLot(lot);
        ParkingTicket ticket = new ParkingTicket(id,space,entryTime,rate,exitTime);
        ticket.setAmountPaid(amountPaid);

        return ticket;
    }
}
