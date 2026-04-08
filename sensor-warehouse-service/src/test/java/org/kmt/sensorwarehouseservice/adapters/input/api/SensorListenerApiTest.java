package org.kmt.sensorwarehouseservice.adapters.input.api;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.vertx.mutiny.core.Vertx;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.kmt.sensorwarehouseservice.domain.ports.input.SensorListenerService;

import java.time.Duration;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.after;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
class SensorListenerApiTest {

    @InjectMock
    SensorListenerService sensorListenerService;

    @Inject
    SensorListenerApi sensorListenerApi;

    @Inject
    Vertx vertx;


    @Test
    void processing_udp_message_should_success_on_port_3344() {
        String message = "sensor_id=t1; value=30";

        when(sensorListenerService.handleSensorMessage(anyString(), anyInt()))
              .thenReturn(Uni.createFrom().voidItem());

        vertx.createDatagramSocket().send(message, 3344, "0.0.0.0")
              .subscribe()
              .withSubscriber(UniAssertSubscriber.create())
              .awaitItem(Duration.ofSeconds(2))
              .assertCompleted();

        verify(sensorListenerService, timeout(2000)).handleSensorMessage(anyString(), anyInt());
    }

    @Test
    void processing_udp_message_should_success_on_port_3355() {
        String message = "sensor_id=h1; value=40";

        when(sensorListenerService.handleSensorMessage(anyString(), anyInt()))
              .thenReturn(Uni.createFrom().voidItem());

        vertx.createDatagramSocket().send(message, 3355, "0.0.0.0")
              .subscribe()
              .withSubscriber(UniAssertSubscriber.create())
              .awaitItem(Duration.ofSeconds(2))
              .assertCompleted();

        verify(sensorListenerService, after(2000)).handleSensorMessage(anyString(), anyInt());
    }

    @Test
    void processing_udp_message_should_fail_without_stop_when_handler_fail() {
        String message = "sensor_id=h1; value=40";
        int testPort = 3355;

        when(sensorListenerService.handleSensorMessage(anyString(), anyInt()))
              .thenReturn(Uni.createFrom().failure(new RuntimeException("Kafka Down")));

        vertx.createDatagramSocket().send(message, 3355, "0.0.0.0")
              .subscribe()
              .withSubscriber(UniAssertSubscriber.create())
              .awaitItem(Duration.ofSeconds(2))
              .assertCompleted();

        verify(sensorListenerService, after(2000)).handleSensorMessage(anyString(), eq(testPort));
    }

    @Test
    void processing_udp_message_should_fail_without_stop_when_port_is_used() {
        int testPort = 3355;

        when(sensorListenerService.handleSensorMessage(anyString(), anyInt()))
              .thenReturn(Uni.createFrom().voidItem());

        sensorListenerApi.onStart(new  StartupEvent());

        verify(sensorListenerService, after(2000).never()).handleSensorMessage(anyString(), eq(testPort));
    }

}