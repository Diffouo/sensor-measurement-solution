package org.kmt.sensormonitoringservice.domain.ports.output;

import io.smallrye.mutiny.Multi;
import org.kmt.sensorcommon.domain.models.SensorMessageModel;

public interface MonitoringProcessorPort {

    Multi<SensorMessageModel> process(Multi<SensorMessageModel> inputSensors);
}
