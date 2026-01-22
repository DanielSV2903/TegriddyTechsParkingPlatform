package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TegriddyTechsParkingPlatformApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TegriddyTechsParkingPlatformApp.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Parking Platform");
        stage.setScene(scene);
        stage.show();
    }
}
