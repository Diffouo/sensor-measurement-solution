package org.kmt.sensorwarehouseservice.domain.ports.input;

import io.smallrye.mutiny.Uni;

public interface SensorListenerService {
    Uni<Void> handleSensorMessage(String message, Integer port);
}
