package org.kmt.sensorwarehouseservice.application.usecases;


import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.kmt.sensorcommon.domain.models.SensorMessageModel;
import org.kmt.sensorwarehouseservice.domain.exceptions.InvalidMessageException;
import org.kmt.sensorwarehouseservice.domain.exceptions.UnSupportedPortException;
import org.kmt.sensorwarehouseservice.domain.ports.input.MessageParserService;
import org.kmt.sensorwarehouseservice.domain.ports.output.SensorPublisherPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
class UpdSensorListenerUseCaseTest {

    @InjectMock
    SensorPublisherPort sensorPublisherPort;

    @InjectMock
    MessageParserService messageParser;

    @Inject
    UpdSensorListenerUseCase updSensorListenerUseCase;

    @Test
    void handleSensorMessage_should_success() {
        var testSensor = mock(SensorMessageModel.class);
        var message = "some udp message";
        var port = 10;

        when(messageParser.parse(anyString(), anyInt())).thenReturn(Uni.createFrom().item(testSensor));
        when(sensorPublisherPort.publish(testSensor)).thenReturn(Uni.createFrom().voidItem());

        var response = updSensorListenerUseCase.handleSensorMessage(message, port)
              .subscribe()
              .withSubscriber(UniAssertSubscriber.create())
              .assertCompleted().getItem();
        assertNull(response);

        verify(messageParser).parse(anyString(), anyInt());
        verify(sensorPublisherPort).publish(testSensor);
    }

    @Test
    void handleSensorMessage_should_throw_exception_when_parser_throw_exception() {
        var testSensor = mock(SensorMessageModel.class);

        when(messageParser.parse(anyString(), anyInt())).thenThrow(new InvalidMessageException("Exception Message"));

        var exception = assertThrows(InvalidMessageException.class, () ->
              updSensorListenerUseCase.handleSensorMessage("message", 10));

        assertInstanceOf(InvalidMessageException.class, exception);
        assertEquals("Exception Message",  exception.getMessage());

        verify(messageParser).parse(anyString(), anyInt());
        verify(sensorPublisherPort, never()).publish(testSensor);
    }

    @Test
    void handleSensorMessage_should_fail_when_parser_finish_with_failure_port() {
        var testSensor = mock(SensorMessageModel.class);
        var port = 10;
        var expectedExceptionMessage = "The port " + port + " is not supported";

        when(messageParser.parse(anyString(), anyInt()))
              .thenReturn(Uni.createFrom().failure(new UnSupportedPortException(port)));

        var exception = updSensorListenerUseCase.handleSensorMessage("message", port)
              .subscribe()
              .withSubscriber(UniAssertSubscriber.create())
              .assertFailedWith(UnSupportedPortException.class)
              .getFailure();

        assertInstanceOf(UnSupportedPortException.class, exception);
        assertEquals(expectedExceptionMessage,  exception.getMessage());

        verify(messageParser).parse(anyString(), anyInt());
        verify(sensorPublisherPort, never()).publish(testSensor);
    }

    @Test
    void handleSensorMessage_should_fail_when_publish_get_failure() {
        var testSensor = mock(SensorMessageModel.class);
        var port = 10;
        var expectedExceptionMessage = "The port " + port + " is not supported";

        when(messageParser.parse(anyString(), anyInt()))
              .thenReturn(Uni.createFrom().failure(new UnSupportedPortException(port)));

        var exception = updSensorListenerUseCase.handleSensorMessage("message", port)
              .subscribe()
              .withSubscriber(UniAssertSubscriber.create())
              .assertFailedWith(UnSupportedPortException.class)
              .getFailure();

        assertInstanceOf(UnSupportedPortException.class, exception);
        assertEquals(expectedExceptionMessage,  exception.getMessage());

        verify(messageParser).parse(anyString(), anyInt());
        verify(sensorPublisherPort, never()).publish(testSensor);
    }

}