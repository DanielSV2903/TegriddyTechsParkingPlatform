package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Administrator;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.Clerk;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.User;
import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.UserRole;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class UserXmlRepository {

    private final Path xmlPath;

    public UserXmlRepository() {
        this(Paths.get(System.getProperty("user.dir")).resolve("users.xml"));
    }

    public UserXmlRepository(Path xmlPath) {
        this.xmlPath = xmlPath;
    }

    public Path getXmlPath() {
        return xmlPath;
    }

    public List<User> loadAll() {
        ensureFileExists();

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(false);
            dbf.setIgnoringComments(true);
            dbf.setIgnoringElementContentWhitespace(true);

            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(xmlPath.toFile());
            doc.getDocumentElement().normalize();

            NodeList nodes = doc.getElementsByTagName("user");
            List<User> users = new ArrayList<>();

            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE) continue;

                Element e = (Element) node;

                int id = parseInt(getText(e, "id"), 0);
                String name = getText(e, "name");
                String username = getText(e, "username");
                String password = getText(e, "password");
                String roleText = getText(e, "role");

                UserRole role;
                try {
                    role = UserRole.valueOf(roleText);
                } catch (Exception ex) {
                    // si el XML viene raro, ignoramos ese registro
                    continue;
                }

                User user = (role == UserRole.ADMIN)
                        ? new Administrator(id, name, username, password)
                        : new Clerk(id, name, username, password);

                users.add(user);
            }

            return users;

        } catch (Exception ex) {
            // Si el XML está corrupto, devolvemos vacío para no tumbar la app
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void saveAll(List<User> users) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();

            Element root = doc.createElement("users");
            doc.appendChild(root);

            for (User u : users) {
                Element userEl = doc.createElement("user");
                root.appendChild(userEl);

                appendText(doc, userEl, "id", String.valueOf(u.getId()));
                appendText(doc, userEl, "name", safe(u.getName()));
                appendText(doc, userEl, "username", safe(u.getUserName()));
                appendText(doc, userEl, "password", safe(u.getPassword()));
                appendText(doc, userEl, "role", String.valueOf(u.getUserRole()));
            }

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            // Asegura carpeta (raíz ya existe, pero por seguridad)
            if (xmlPath.getParent() != null) {
                Files.createDirectories(xmlPath.getParent());
            }

            transformer.transform(new DOMSource(doc), new StreamResult(xmlPath.toFile()));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void ensureFileExists() {
        try {
            if (Files.exists(xmlPath)) return;

            // crea un XML válido vacío
            Files.createFile(xmlPath);
            saveAll(new ArrayList<>());

        } catch (FileAlreadyExistsException ignore) {
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void appendText(Document doc, Element parent, String tag, String value) {
        Element el = doc.createElement(tag);
        el.appendChild(doc.createTextNode(value));
        parent.appendChild(el);
    }

    private static String getText(Element parent, String tag) {
        NodeList nl = parent.getElementsByTagName(tag);
        if (nl.getLength() == 0) return "";
        Node n = nl.item(0);
        return n.getTextContent() != null ? n.getTextContent().trim() : "";
    }

    private static int parseInt(String s, int def) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return def;
        }
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }
}
