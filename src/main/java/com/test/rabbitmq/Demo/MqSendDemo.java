package com.test.rabbitmq.Demo;

import com.test.rabbitmq.base.BaseMqSender;
import com.test.rabbitmq.util.SerializationUtils;

import java.util.HashMap;
import java.util.Map;

public class MqSendDemo {
    public static String host = "127.0.0.1";
    public static int port = 5672;
    public static String userName = "zjl";
    public static String password = "123";
    public static String vhost = "mq_test_01";

    public static String routingKey = "test01_direct01";

    public static BaseMqSender sender;
    static {
        sender = new BaseMqSender(host, port, userName, password, vhost);
        sender.setmType("direct");
        sender.setmExchange("mq.direct.test.01");
    }

    public static void main(String[] args) {

        Map<String, Object> message = new HashMap<>();
        message.put("name", "张三");
        message.put("age", 21);
        byte[] body = SerializationUtils.serialize(message);
        sender.sendMsg(routingKey, body);
        System.out.println("发送成功！");

//        Map<String, Object> message2 = new HashMap<>();
//        message2.put("name", "李四");
//        message2.put("age", 20);
//        sender.sendMsg("test01_direct02",SerializationUtils.serialize(message2));
//        System.out.println("发送成功！");
//
//
//        Map<String, Object> message3 = new HashMap<>();
//        message3.put("name", "李四");
//        message3.put("age", 21);
//        sender.sendMsg("test01_direct02",SerializationUtils.serialize(message3));
//        System.out.println("发送成功！");

        sender.close();
    }
}
