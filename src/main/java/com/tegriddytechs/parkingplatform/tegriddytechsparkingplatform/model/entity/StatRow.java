package com.tegriddytechs.parkingplatform.tegriddytechsparkingplatform.model.entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StatRow {
    private final StringProperty key = new SimpleStringProperty();
    private final StringProperty value = new SimpleStringProperty();
    private final StringProperty extra = new SimpleStringProperty();

    public StatRow(String key, String value, String extra) {
        this.key.set(key);
        this.value.set(value);
        this.extra.set(extra);
    }

    public StringProperty keyProperty() { return key; }
    public StringProperty valueProperty() { return value; }
    public StringProperty extraProperty() { return extra; }
}