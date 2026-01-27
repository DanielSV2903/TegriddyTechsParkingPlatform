module com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.desktop;

    opens com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform to javafx.fxml;
    opens com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity to com.google.gson;
    opens com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.data to com.google.gson;

    exports com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform;
}