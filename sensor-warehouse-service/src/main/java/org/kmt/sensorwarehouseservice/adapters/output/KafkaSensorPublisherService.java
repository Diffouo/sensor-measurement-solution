package org.kmt.sensorwarehouseservice.adapters.output;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.kmt.sensorcommon.domain.models.SensorMessageModel;
import org.kmt.sensorwarehouseservice.domain.exceptions.InvalidMessageException;
import org.kmt.sensorwarehouseservice.domain.ports.output.SensorPublisherPort;

@ApplicationScoped
public class KafkaSensorPublisherService implements SensorPublisherPort {

    private static final String PUBLISH_SENSOR_ERROR =
          "An error occurred while trying to publish the sensor {0}. \n Error message = {1}";

    private final Emitter<SensorMessageModel> sensorEmitter;

    public KafkaSensorPublisherService(@Channel("sensor-out") Emitter<SensorMessageModel> sensorEmitter) {
        this.sensorEmitter = sensorEmitter;
    }

    @Override
    public Uni<Void> publish(SensorMessageModel sensor) {
        return Uni.createFrom().completionStage(() -> sensorEmitter.send(sensor))
              .replaceWithVoid()
              .onFailure()
              .recoverWithUni(throwable -> {
                  Log.errorv(PUBLISH_SENSOR_ERROR, sensor, throwable.getMessage());
                  return Uni.createFrom().failure(new InvalidMessageException(throwable.getMessage()));
              });
    }
}
