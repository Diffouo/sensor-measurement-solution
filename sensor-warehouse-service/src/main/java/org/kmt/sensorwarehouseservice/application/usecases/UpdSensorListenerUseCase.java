package org.kmt.sensorwarehouseservice.application.usecases;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.kmt.sensorwarehouseservice.domain.ports.input.MessageParserService;
import org.kmt.sensorwarehouseservice.domain.ports.input.SensorListenerService;
import org.kmt.sensorwarehouseservice.domain.ports.output.SensorPublisherPort;

@ApplicationScoped
public class UpdSensorListenerUseCase implements SensorListenerService {

    private final SensorPublisherPort sensorPublisherService;
    private final MessageParserService messageParser;

    public UpdSensorListenerUseCase(SensorPublisherPort sensorPublisherService, MessageParserService messageParser) {
        this.sensorPublisherService = sensorPublisherService;
        this.messageParser = messageParser;
    }


    @Override
    public Uni<Void> handleSensorMessage(String message, Integer port) {
        return messageParser.parse(message, port)
              .chain(sensorPublisherService::publish);
    }
}
