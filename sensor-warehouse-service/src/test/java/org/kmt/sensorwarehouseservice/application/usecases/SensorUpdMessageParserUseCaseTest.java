package org.kmt.sensorwarehouseservice.application.usecases;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.kmt.sensorcommon.domain.models.SensorMessageModel;
import org.kmt.sensorcommon.domain.models.SensorMessageType;
import org.kmt.sensorwarehouseservice.domain.exceptions.InvalidMessageException;
import org.kmt.sensorwarehouseservice.domain.exceptions.UnSupportedPortException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
class SensorUpdMessageParserUseCaseTest {

    @Inject
    SensorUpdMessageParserUseCase sensorUpdMessageParserUseCase;

    @Test
    void parse_should_throw_InvalidMessageException_when_message_is_null_or_blank() {
        int anyPort = 3344;
        String emptyMessage = "";
        String emptyMessageWithWhiteSpace = " ";
        var expectedExceptionMessage = "Please provide a valid message";

        var exception = assertThrows(InvalidMessageException.class,
              () -> sensorUpdMessageParserUseCase.parse(null, 3344));
        assertEquals(expectedExceptionMessage, exception.getMessage());

        var exceptionWhenEmpty = assertThrows(InvalidMessageException.class,
              () -> sensorUpdMessageParserUseCase.parse(emptyMessage, anyPort));
        assertEquals(expectedExceptionMessage, exceptionWhenEmpty.getMessage());

        var exceptionWhenWhiteSpace = assertThrows(InvalidMessageException.class,
              () -> sensorUpdMessageParserUseCase.parse(emptyMessageWithWhiteSpace, anyPort));
        assertEquals(expectedExceptionMessage, exceptionWhenWhiteSpace.getMessage());
    }

    @Test
    void parse_should_return_temperature_sensor_when_is_port_3344() {
        int port = 3344;
        String message = "sensor_id=t1; value=30";
        var expectedSensor = new SensorMessageModel("t1", 30.0, SensorMessageType.TEMPERATURE);

        sensorUpdMessageParserUseCase.parse(message, port)
              .subscribe().withSubscriber(UniAssertSubscriber.create())
              .assertCompleted()
              .assertItem(expectedSensor);
    }

    @Test
    void parse_should_return_humidity_sensor_when_is_port_3355() {
        int port = 3355;
        String message = "sensor_id=h1; value=40";
        var expectedSensor = new SensorMessageModel("h1", 40.0, SensorMessageType.HUMIDITY);

        sensorUpdMessageParserUseCase.parse(message, port)
              .subscribe().withSubscriber(UniAssertSubscriber.create())
              .assertCompleted()
              .assertItem(expectedSensor);
    }

    @Test
    void parse_should_throw_unSupportedPortException_when_port_is_not_3344_nor_3355() {
        int port = 33;
        String message = "sensor_id=h1; value=40";
        var expectedExceptionMessage = "The port " + port + " is not supported";

        var exception = sensorUpdMessageParserUseCase.parse(message, port)
              .subscribe().withSubscriber(UniAssertSubscriber.create())
              .assertFailedWith(UnSupportedPortException.class)
              .getFailure();

        assertEquals(expectedExceptionMessage, exception.getMessage());
    }

    @Test
    void parse_should_fail_with_invalidMessageException_when_message_format_is_invalid() {
        int port = 3355;
        String message = "sensor_id=h1: value=40";
        var expectedException = (InvalidMessageException.class);

        var exception = sensorUpdMessageParserUseCase.parse(message, port)
              .subscribe().withSubscriber(UniAssertSubscriber.create())
              .assertFailedWith(Exception.class)
              .getFailure();

        assertInstanceOf(expectedException, exception);
    }

}