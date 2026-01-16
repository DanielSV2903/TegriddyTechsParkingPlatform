module com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform to javafx.fxml;
    exports com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform;
}