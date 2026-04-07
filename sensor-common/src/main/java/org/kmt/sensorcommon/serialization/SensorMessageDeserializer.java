package org.kmt.sensorcommon.serialization;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import org.kmt.sensorcommon.domain.models.SensorMessageModel;

public class SensorMessageDeserializer extends ObjectMapperDeserializer<SensorMessageModel> {

    public SensorMessageDeserializer() {
        super(SensorMessageModel.class);
    }
}
