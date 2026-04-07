package org.kmt.sensorwarehouseservice.domain.exceptions;

public class InvalidMessageException extends RuntimeException {

    public InvalidMessageException(String message) {
        super(message);
    }

}
