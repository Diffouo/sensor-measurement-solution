package org.kmt.sensormonitoringservice.adapters.output;


import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.kmt.sensorcommon.domain.models.SensorMessageModel;
import org.kmt.sensorcommon.domain.models.SensorMessageType;
import org.kmt.sensormonitoringservice.domain.ports.input.ThresholdFilterService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@QuarkusTest
class SensorMonitoringProcessorTest {

    @Inject
    @Any
    InMemoryConnector connector;

    @InjectMock
    ThresholdFilterService thresholdFilterService;

    @Test
    void should_send_to_alerts_out_only_when_threshold_is_exceeded() {
        var source = connector.source("sensors-in");
        var sink = connector.sink("alerts-out");

        var criticMsg = new SensorMessageModel("t1", 40.0, SensorMessageType.TEMPERATURE);
        var normalMsg = new SensorMessageModel("t2", 20.0, SensorMessageType.TEMPERATURE);

        when(thresholdFilterService.isCriticMeasure(criticMsg)).thenReturn(true);
        when(thresholdFilterService.isCriticMeasure(normalMsg)).thenReturn(false);

        source.send(criticMsg);
        source.send(normalMsg);

        var received = sink.received();
        assertEquals(1, received.size());
        assertEquals(criticMsg, received.getFirst().getPayload());
    }

}