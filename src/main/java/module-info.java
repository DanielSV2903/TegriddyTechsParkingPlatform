module com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.desktop;
    requires org.jdom2;
    requires org.apache.pdfbox;

    opens com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view to javafx.fxml;
    opens com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity to com.google.gson;
    opens com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data to com.google.gson;
    exports com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;
    exports com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity;
    opens com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data.xml to com.google.gson;

}
