package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.mappers;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml.XmlEntityMapper;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.SpaceType;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.VehicleType;
import org.jdom2.Element;

import java.lang.ref.PhantomReference;

public class VehicleTypeXmlMapper implements XmlEntityMapper<VehicleType> {
    private final String ID_ATTRIBUTE = "id";
    private final String DESCRIPTION_ATTRIBUTE = "description";
    private final String AMOUNT_OF_TYRES_ATTRIBUTE = "amountOfTyres";
    private final String FEE_ATTRIBUTE = "fee";
    private final String SPACE_TYPE_ATTRIBUTE = "spaceType";

    @Override
    public String elementName() {
        return "vehicleType";
    }

    @Override
    public String idAttributeName() {
        return ID_ATTRIBUTE;
    }

    @Override
    public String getId(VehicleType entity) {
        return entity.getId()+"";
    }

    @Override
    public Element toElement(VehicleType entity) {
        Element e = new Element(elementName());
        e.setAttribute(idAttributeName(), entity.getId()+"");
        e.addContent(new Element(DESCRIPTION_ATTRIBUTE).setText(entity.getDescription()));
        e.addContent(new Element(AMOUNT_OF_TYRES_ATTRIBUTE).setText(entity.getAmountOfTyres()+""));
        e.addContent(new Element(FEE_ATTRIBUTE).setText(entity.getFee()+""));
        e.addContent(new Element(SPACE_TYPE_ATTRIBUTE).setText(entity.getSpaceType().name()));
        return e;
    }

    @Override
    public VehicleType fromElement(Element element) {
        VehicleType vehicleType = new VehicleType();
        vehicleType.setId(Integer.parseInt(element.getAttributeValue(ID_ATTRIBUTE)));
        vehicleType.setDescription(element.getChildText(DESCRIPTION_ATTRIBUTE));
        vehicleType.setAmountOfTyres(Byte.parseByte(element.getChildText(AMOUNT_OF_TYRES_ATTRIBUTE)));
        vehicleType.setFee(Double.parseDouble(element.getChildText(FEE_ATTRIBUTE)));
        vehicleType.setSpaceType(castType(element.getChildText(SPACE_TYPE_ATTRIBUTE)));
        return vehicleType;
    }

    private SpaceType castType(String childText) {
        if (childText == null || childText.trim().isEmpty()) {
            return null;
        }
            return SpaceType.valueOf(childText.trim());
    }
}
