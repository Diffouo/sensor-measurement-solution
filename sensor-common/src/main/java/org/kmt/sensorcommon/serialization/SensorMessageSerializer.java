package org.kmt.sensorcommon.serialization;

import io.quarkus.kafka.client.serialization.ObjectMapperSerializer;
import org.kmt.sensorcommon.domain.models.SensorMessageModel;

public class SensorMessageSerializer extends ObjectMapperSerializer<SensorMessageModel> {
}
