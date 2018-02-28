package com.test.rabbitmq.exception;

public class MqException extends RuntimeException {
    public MqException(String message) {
        super(message);
    }
}
