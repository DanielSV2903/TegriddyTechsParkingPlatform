package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Customer;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
TODO
Handles the persistence of data to and from XML files (generic).
 */
public class PersistenceXMLManager<T> implements PersistenceOperations<T> {


    private final String filePath;
    private final Element root;
    private final Document document;

    private final XmlEntityMapper<T> mapper;

    public PersistenceXMLManager(String filePath, String rootName, XmlEntityMapper<T> mapper) throws IOException, JDOMException {

        if (mapper == null) throw new IllegalArgumentException("mapper cannot be null");
        this.mapper = mapper;
        this.filePath = filePath;

        File file = new File(filePath);

        if (!file.exists()) {
            this.root = new Element(rootName);
            this.document = new Document(root);
            save();
        } else {
            SAXBuilder saBuilder = new SAXBuilder();
            saBuilder.setIgnoringElementContentWhitespace(true);
            this.document = saBuilder.build(filePath);
            this.root = document.getRootElement();
        }
    }

    public static <T> PersistenceXMLManager<T> openXMLFile(String filePath, String rootName, XmlEntityMapper<T> mapper)
            throws JDOMException, IOException {
        return new PersistenceXMLManager<>(filePath, rootName, mapper);
    }

    @Override
    public void save() throws IOException {
        XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
        try (FileWriter writer = new FileWriter(this.filePath)) {
            xmlOutputter.output(this.document, writer);
        }
    }
    @Override
    public List<T> load(){
        List<T> result = new ArrayList<>();
        for (Element child : root.getChildren(mapper.elementName())) {
            result.add(mapper.fromElement(child));
        }
        return result;
    }

    public Optional<T> findById(String id) {
        if (id == null) return Optional.empty();
        for (Element child : root.getChildren(mapper.elementName())) {
            String storedId = child.getAttributeValue(mapper.idAttributeName());
            if (id.equals(storedId)) {
                return Optional.of(mapper.fromElement(child));
            }
        }
        return Optional.empty();
    }
    public Optional<T> findById(int id) {
        Optional<T> result = Optional.empty();
        for (Element child : root.getChildren(mapper.elementName())) {
            int storedId = Integer.parseInt(child.getAttributeValue(mapper.idAttributeName()));
            if (id==storedId) {
                result = Optional.of(mapper.fromElement(child));
            }
        }
        return result;
    }

    public void upsert(T entity) throws IOException {
        String id = mapper.getId(entity);
        if (id == null) throw new IllegalArgumentException("Entity id cannot be null");

        // buscar existente
        List<Element> children = root.getChildren(mapper.elementName());
        for (int i = 0; i < children.size(); i++) {
            Element existing = children.get(i);
            String storedId = existing.getAttributeValue(mapper.idAttributeName());
            if (id.equals(storedId)) {
                // reemplazar
                children.set(i, mapper.toElement(entity));
                save();
                return;
            }
        }

        // insertar nuevo
        root.addContent(mapper.toElement(entity));
        save();
    }

    public boolean deleteById(String id) throws IOException {
        if (id == null) return false;

        List<Element> children = root.getChildren(mapper.elementName());
        for (int i = 0; i < children.size(); i++) {
            Element existing = children.get(i);
            String storedId = existing.getAttributeValue(mapper.idAttributeName());
            if (id.equals(storedId)) {
                children.remove(i);
                save();
                return true;
            }
        }
        return false;
    }

    public boolean deleteById(int id) throws IOException {
        boolean flag = false;
        List<Element> children = root.getChildren(mapper.elementName());
        for (int i = 0; i < children.size(); i++) {
            Element existing = children.get(i);
            int storedId = Integer.parseInt(existing.getAttributeValue(mapper.idAttributeName()));
            if (id==storedId) {
                children.remove(i);
                save();
                flag= true;
            }
        }
        return flag;
    }

    public boolean delete(T entity) throws IOException {
        String id = mapper.getId(entity);
        if (id == null) return false;
        return deleteById(id);
    }
}
