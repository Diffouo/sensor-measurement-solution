package org.kmt.sensorwarehouseservice.domain.ports.input;

import io.smallrye.mutiny.Uni;
import org.kmt.sensorcommon.domain.models.SensorMessageModel;

public interface MessageParserService {

    Uni<SensorMessageModel> parse(String message, Integer port);
}
