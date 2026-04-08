package org.kmt.sensormonitoringservice.application.usecases;


import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.kmt.sensorcommon.domain.models.SensorMessageModel;
import org.kmt.sensorcommon.domain.models.SensorMessageType;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class SensorThresholdFilterUseCaseTest {

    @Inject
    SensorThresholdFilterUseCase sensorThresholdFilterUseCase;


    @Test
    void isCritic_should_return_true_when_temperature_threshold_exceeded() {
        var tempSensor = new SensorMessageModel("t1", 36.0, SensorMessageType.TEMPERATURE);

        var isCritic = sensorThresholdFilterUseCase.isCriticMeasure(tempSensor);

        assertTrue(isCritic);
    }

    @Test
    void isCritic_should_return_false_when_temperature_threshold_not_exceeded() {
        var tempSensor = new SensorMessageModel("t1", 34.0, SensorMessageType.TEMPERATURE);

        var isCritic = sensorThresholdFilterUseCase.isCriticMeasure(tempSensor);

        assertFalse(isCritic);
    }

    @Test
    void isCritic_should_return_true_when_humidity_threshold_exceeded() {
        var tempSensor = new SensorMessageModel("h1", 51.0, SensorMessageType.HUMIDITY);

        var isCritic = sensorThresholdFilterUseCase.isCriticMeasure(tempSensor);

        assertTrue(isCritic);
    }

    @Test
    void isCritic_should_return_false_when_humidity_threshold_not_exceeded() {
        var tempSensor = new SensorMessageModel("h1", 49.0, SensorMessageType.HUMIDITY);

        var isCritic = sensorThresholdFilterUseCase.isCriticMeasure(tempSensor);

        assertFalse(isCritic);
    }
  
}