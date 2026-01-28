package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.view;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public final class CrudFormUtils {

    private CrudFormUtils() {
    }

    public static String readRequired(TextField field, String title, String fieldName) {
        String value = field.getText() != null ? field.getText().trim() : "";
        if (value.isEmpty()) {
            CrudAlertHelper.showWarning(title, "Campo requerido: " + fieldName);
            return null;
        }
        return value;
    }

    public static Integer readInt(TextField field, String title, String fieldName) {
        String value = readRequired(field, title, fieldName);
        if (value == null) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            CrudAlertHelper.showWarning(title, "Valor invalido en " + fieldName + ": " + value);
            return null;
        }
    }

    public static Double readDouble(TextField field, String title, String fieldName) {
        String value = readRequired(field, title, fieldName);
        if (value == null) {
            return null;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            CrudAlertHelper.showWarning(title, "Valor invalido en " + fieldName + ": " + value);
            return null;
        }
    }

    public static <T> T readSelection(ComboBox<T> comboBox, String title, String fieldName) {
        T value = comboBox.getValue();
        if (value == null) {
            CrudAlertHelper.showWarning(title, "Seleccione: " + fieldName);
        }
        return value;
    }
}
