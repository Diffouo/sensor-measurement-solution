package org.kmt.sensorwarehouseservice.adapters.output;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.kmt.sensorcommon.domain.models.SensorMessageModel;
import org.kmt.sensorcommon.domain.models.SensorMessageType;
import org.kmt.sensorwarehouseservice.domain.exceptions.InvalidMessageException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;

@QuarkusTest
class KafkaSensorPublisherServiceTest {

    @Inject
    @Any
    InMemoryConnector connector;

    @Inject
    KafkaSensorPublisherService kafkaSensorPublisherService;


    @Test
    void publish_message_on_out_channel_should_success() {
        var sensorChannelOut = connector.sink("sensor-out");
        var sensorMessage = new SensorMessageModel("t1", 30.0, SensorMessageType.TEMPERATURE);

        var response = kafkaSensorPublisherService.publish(sensorMessage)
              .subscribe()
              .withSubscriber(UniAssertSubscriber.create())
              .assertCompleted()
              .getItem();

        assertNull(response);
        assertEquals(1, sensorChannelOut.received().size());

        var actualMessageReceived = sensorChannelOut.received().getFirst().getPayload();
        assertEquals(sensorMessage, actualMessageReceived);
    }

    @Test
    void publish_message_should_fail_with_invalidMessageException_when_something_is_wrong() {
        var sensorChannelOut = connector.sink("sensor-out");

        var exception = kafkaSensorPublisherService.publish(null)
              .subscribe()
              .withSubscriber(UniAssertSubscriber.create())
              .assertFailedWith(InvalidMessageException.class)
              .getFailure();

        assertInstanceOf(InvalidMessageException.class, exception);
        assertEquals(0, sensorChannelOut.received().size());
    }

}