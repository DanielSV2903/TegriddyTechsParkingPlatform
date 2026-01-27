package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform;

import com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity.OperationResult;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public final class CrudAlertHelper {

    private CrudAlertHelper() {
    }

    public static void showResult(String title, OperationResult result) {
        Alert.AlertType type = result != null && result.isSuccessfull()
                ? Alert.AlertType.INFORMATION
                : Alert.AlertType.ERROR;
        String message = result != null ? result.getMessage() : "Operacion sin resultado";
        showAlert(title, "Resultado", message, type);
    }

    public static void showEntity(String title, Object entity) {
        String message = entity != null ? entity.toString() : "No encontrado";
        showAlert(title, "Detalle", message, Alert.AlertType.INFORMATION);
    }

    public static void showWarning(String title, String message) {
        showAlert(title, "Atencion", message, Alert.AlertType.WARNING);
    }

    private static void showAlert(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type, content, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }
}
