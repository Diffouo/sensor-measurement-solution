package org.kmt.sensorwarehouseservice.domain.ports.output;


import io.smallrye.mutiny.Uni;
import org.kmt.sensorcommon.domain.models.SensorMessageModel;

public interface SensorPublisherPort {
    Uni<Void> publish(SensorMessageModel sensor);
}
