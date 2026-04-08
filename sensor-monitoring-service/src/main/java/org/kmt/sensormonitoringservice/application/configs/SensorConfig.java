package org.kmt.sensormonitoringservice.application.configs;

import io.smallrye.config.ConfigMapping;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigMapping(prefix = "sensor")
public interface SensorConfig {

    Threshold threshold();

    interface Threshold {
        String temperature();
        String humidity();
    }
}
