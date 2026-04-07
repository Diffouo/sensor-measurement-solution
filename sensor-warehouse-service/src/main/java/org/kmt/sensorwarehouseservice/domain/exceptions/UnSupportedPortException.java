package org.kmt.sensorwarehouseservice.domain.exceptions;

public class UnSupportedPortException extends RuntimeException {

    public UnSupportedPortException(Integer port) {
        super("The port " + port + " is not supported");
    }
}
