package com.test.rabbitmq.base;

import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface ConsumerHandler {

    void handleMessage(Map<String, Object> message) throws Exception;
}
