package org.kmt.sensorwarehouseservice.adapters.input.api;

import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import io.vertx.mutiny.core.Vertx;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.kmt.sensorwarehouseservice.application.configs.SensorConfig;
import org.kmt.sensorwarehouseservice.domain.ports.input.SensorListenerService;

import java.nio.charset.StandardCharsets;
import java.util.List;

@ApplicationScoped
public class SensorListenerApi {

    private static final String LISTENING_HOST = "0.0.0.0";
    private static final String LOG_START_LISTENING_SUCCESS = "The UDP server is ready and listening on port {0}";
    private static final String LOG_START_LISTENING_FAILURE = "Failed to listen on UDP port {0}. Error Message = {1}";
    private static final String LOG_MESSAGE_RECEIVED_INFO = "Message {0} received from UDP port >> {1}";
    private static final String LOG_PROCESSING_SUCCESS = "Sensor message processed and sent to kafka broker";
    private static final String LOG_PROCESSING_FAILURE = "An error occurred while processing the UDP Message. " +
          "Error message = {0}";

    private final SensorConfig sensorConfig;
    private final SensorListenerService sensorListenerService;
    private final Vertx vertx;

    public SensorListenerApi(Vertx vertx, SensorConfig sensorConfig, SensorListenerService sensorListenerService) {
        this.vertx = vertx;
        this.sensorConfig = sensorConfig;
        this.sensorListenerService = sensorListenerService;
    }

    void onStart(@Observes StartupEvent startupEvent) {
        List.of(
              Integer.parseInt(sensorConfig.ports().temperature()),
              Integer.parseInt(sensorConfig.ports().humidity())
        ).forEach(this::processListening);
    }

    private void processListening(Integer port) {
        var udpSocket = vertx.createDatagramSocket();
        udpSocket.listen(port, LISTENING_HOST).subscribe().with(openedSocket -> {
            Log.infov(LOG_START_LISTENING_SUCCESS, port);
            openedSocket.handler(packet -> {
                var message = packet.data().toString(StandardCharsets.UTF_8);
                Log.infov(LOG_MESSAGE_RECEIVED_INFO, message, port);
                sensorListenerService.handleSensorMessage(message, port)
                      .subscribe().with(
                            ignored -> Log.info(LOG_PROCESSING_SUCCESS),
                            failure -> Log.errorv(LOG_PROCESSING_FAILURE, failure.getMessage())
                      );
            });
        }, failure -> Log.warnv(LOG_START_LISTENING_FAILURE, port, failure.getMessage()));

    }
}
