package org.kmt.sensorcommon.domain.models;

public record SensorMessageModel(
      String sensorId,
      Double value,
      SensorMessageType sensorType
) { }
