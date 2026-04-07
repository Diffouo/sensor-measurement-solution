package org.kmt.sensorwarehouseservice.application.usecases;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.kmt.sensorcommon.domain.models.SensorMessageModel;
import org.kmt.sensorcommon.domain.models.SensorMessageType;
import org.kmt.sensorwarehouseservice.domain.exceptions.InvalidMessageException;
import org.kmt.sensorwarehouseservice.domain.exceptions.UnSupportedPortException;
import org.kmt.sensorwarehouseservice.domain.ports.input.MessageParserService;

@ApplicationScoped
public class SensorUpdMessageParserUseCase implements MessageParserService {

    private static final String PROPS_SEPARATOR = ";";
    private static final String VALUE_SEPARATOR = "=";
    private static final String LOG_PARSE_ERROR = "An error occurred while parsing the message {0}. \n" +
          "Exception message = {1}";

    @Override
    public Uni<SensorMessageModel> parse(String message, Integer port) {
        if (null == message || message.isBlank()) {
            throw new InvalidMessageException("Please provide a valid message");
        }

        return Uni.createFrom().item(() -> {
                  var sensorEntries = message.split(PROPS_SEPARATOR);
                  var sensorId = sensorEntries[0].split(VALUE_SEPARATOR)[1].trim();
                  var sensorValue = Double.parseDouble(sensorEntries[1].split(VALUE_SEPARATOR)[1].trim());

                  return switch (port) {
                      case 3344 -> new SensorMessageModel(sensorId, sensorValue, SensorMessageType.TEMPERATURE);
                      case 3355 -> new SensorMessageModel(sensorId, sensorValue, SensorMessageType.HUMIDITY);
                      default -> throw new UnSupportedPortException(port);
                  };
              })
              .onFailure()
              .recoverWithUni(throwable -> {
                  Log.errorv(LOG_PARSE_ERROR, message, throwable.getMessage());
                  if  (throwable instanceof UnSupportedPortException unSupportedPortException) {
                      return Uni.createFrom().failure(unSupportedPortException);
                  }
                  return Uni.createFrom().failure(new InvalidMessageException(throwable.getMessage()));
              });
    }
}
