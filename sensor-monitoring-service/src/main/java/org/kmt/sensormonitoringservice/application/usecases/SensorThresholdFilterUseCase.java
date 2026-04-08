package org.kmt.sensormonitoringservice.application.usecases;

import jakarta.enterprise.context.ApplicationScoped;
import org.kmt.sensorcommon.domain.models.SensorMessageModel;
import org.kmt.sensormonitoringservice.application.configs.SensorConfig;
import org.kmt.sensormonitoringservice.domain.ports.input.ThresholdFilterService;

@ApplicationScoped
public class SensorThresholdFilterUseCase implements ThresholdFilterService {

    private final SensorConfig sensorConfig;

    public SensorThresholdFilterUseCase(SensorConfig sensorConfig) {
        this.sensorConfig = sensorConfig;
    }

    @Override
    public boolean isCriticMeasure(SensorMessageModel sensorMessageModel) {
        return switch (sensorMessageModel.sensorType()) {
            case TEMPERATURE -> sensorMessageModel.value() > Double.parseDouble(sensorConfig.threshold().temperature());
            case HUMIDITY -> sensorMessageModel.value() > Double.parseDouble(sensorConfig.threshold().humidity());
        };
    }
}
