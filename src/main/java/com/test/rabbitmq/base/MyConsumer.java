package com.test.rabbitmq.base;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.test.rabbitmq.exception.MqException;
import com.test.rabbitmq.util.SerializationUtils;

import java.io.IOException;
import java.util.Map;

public class MyConsumer extends DefaultConsumer {

    private ConsumerHandler handler;
    private boolean autoAck;

    /**
     * Constructs a new instance and records its association to the passed-in channel.
     *
     * @param channel the channel to which this consumer is attached
     */
    public MyConsumer(Channel channel, ConsumerHandler handler, boolean autoAck) {
        super(channel);
        this.handler = handler;
        this.autoAck = autoAck;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

//        try {
        int count = 0;

        Map<String, Object> map = (Map<String, Object>) SerializationUtils.deserialize(body);
        while (true) {
            try {
                handler.handleMessage(map);
                if (!autoAck) {
                    getChannel().basicAck(envelope.getDeliveryTag(), false);
                }
                break;
            } catch (Exception e) {
                System.out.println("----------------------处理次数" + (count++));
//                e.printStackTrace();
            }
        }



        /*} catch (Exception e) {
           *//*
            防止丢消息方式一
            专门定义一个接收处理失败的消息的队列，该队列只做简单的记录

            BaseMqSender sender = new BaseMqSender(MqSendDemo.host, MqSendDemo.port, MqSendDemo.userName, MqSendDemo.password, MqSendDemo.vhost);
            sender.setmType("direct");
            sender.setmExchange("mq.direct.test.01");
            sender.sendMsg("test01_fail", body);
            sender.close();*//*

//              防止丢消息方式二：
//            注释这里表示要将处理失败的消息放回队列重新消费
//            应该要指定一些规则来确定重试次数
//            getChannel().basicNack(envelope.getDeliveryTag(), false, true);


//            拒收消息
//            getChannel().basicReject(envelope.getDeliveryTag(), true);
            e.printStackTrace();
            System.out.println("消息处理失败!");
        }*/
    }
}
