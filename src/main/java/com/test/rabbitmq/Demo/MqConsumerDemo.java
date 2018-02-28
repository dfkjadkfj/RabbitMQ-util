package com.test.rabbitmq.Demo;

import com.test.rabbitmq.base.BaseMqReceiver;
import com.test.rabbitmq.exception.MqException;

public class MqConsumerDemo {
    public static String host = "127.0.0.1";
    public static int port = 5672;
    public static String userName = "zjl";
    public static String password = "123";
    public static String vhost = "mq_test_01";

    public static String queue = "direct_queue_01";
    public static String routingKey = "test01_direct01";

    public static void main(String[] args) {
        BaseMqReceiver consumer = new BaseMqReceiver(host, port, userName, password, vhost);
        consumer.setmType("direct");
        consumer.setmExchange("mq.direct.test.01");
        consumer.setQueue(queue);
        consumer.addRoutingKeys(routingKey);
        consumer.setmAutoAct(false);
        consumer.setConsumerHandler(message -> {
            System.out.println(message);
        });

        consumer.connect();
    }
}
