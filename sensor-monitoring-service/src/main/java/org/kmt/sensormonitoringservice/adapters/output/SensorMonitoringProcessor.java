package org.kmt.sensormonitoringservice.adapters.output;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.kmt.sensorcommon.domain.models.SensorMessageModel;
import org.kmt.sensormonitoringservice.domain.ports.input.ThresholdFilterService;
import org.kmt.sensormonitoringservice.domain.ports.output.MonitoringProcessorPort;

@ApplicationScoped
public class SensorMonitoringProcessor implements MonitoringProcessorPort {

    private final ThresholdFilterService thresholdFilterService;

    public SensorMonitoringProcessor(ThresholdFilterService thresholdFilterService) {
        this.thresholdFilterService = thresholdFilterService;
    }

    @Override
    @Incoming("sensors-in")
    @Outgoing("alerts-out")
    public Multi<SensorMessageModel> process(Multi<SensorMessageModel> multi) {
        return multi
              .filter(thresholdFilterService::isCriticMeasure)
              .onItem()
              .invoke(sensorMessageModel ->
                    Log.infov("\u001B[31mAlert triggering for sensor {0}", sensorMessageModel));
    }
}
