package org.kmt.sensorwarehouseservice.application.configs;

import io.smallrye.config.ConfigMapping;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigMapping(prefix = "sensor.upd")
public interface SensorConfig {

    SupportedPort ports();

    interface SupportedPort {
        String temperature();
        String humidity();
    }
}
