package org.kmt.sensorcommon.domain.models;

public enum SensorMessageType {

    TEMPERATURE("TEMPERATURE"),
    HUMIDITY("HUMIDITY");

    private final String value;

    SensorMessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
