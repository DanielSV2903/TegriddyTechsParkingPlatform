package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml;

import org.jdom2.Element;
import org.jdom2.JDOMException;

import java.io.IOException;

public interface XmlEntityMapper<T> {
    String elementName();              // e.g. "user", "vehicle"
    String idAttributeName();           // e.g. "id"
    String getId(T entity);             // e.g. user.getId()
    Element toElement(T entity);
    T fromElement(Element element);
}
