package org.kmt.sensormonitoringservice.domain.ports.input;

import org.kmt.sensorcommon.domain.models.SensorMessageModel;

public interface ThresholdFilterService {

    boolean isCriticMeasure(SensorMessageModel sensorMessageModel);
}
