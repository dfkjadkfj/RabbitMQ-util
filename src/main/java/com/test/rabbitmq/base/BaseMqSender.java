package com.test.rabbitmq.base;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AlreadyClosedException;
import com.rabbitmq.client.MessageProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class BaseMqSender extends BaseMQer {

    private static Logger log = LoggerFactory.getLogger(BaseMqSender.class);

    //交换机
    private String mExchange;
    //是否交换机持久化
    private boolean mExchangeDurable = true;
    //是否持久化
    private boolean mDurable = true;
    //交换机 类型
    private String mType;


    protected BaseMqSender(String host, int port, String userName, String password, String vhost) {
        super(host, port, userName, password, vhost);
    }

    public void connect() {
        try {
            //先获取到频道
            this.mChannel = this.createChannel();
            if (StringUtils.isNotEmpty(this.mExchange) && StringUtils.isNotEmpty(this.mType)) {
                //宣布一个非自动删除的交换机 如果设置第三个参数为true 则持久化这个交换机
                this.mChannel.exchangeDeclare(this.mExchange, this.mType, this.mExchangeDurable);
            }
            this.setConnStatus(CONNECTIONSTATUS.CONNECTED);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String exchange, String routingKey, AMQP.BasicProperties props, byte[] body) {
        int tryTimes = 2;
        while (tryTimes-- > 0) {
            if (this.getConnStatus() == CONNECTIONSTATUS.DISCONNECTED) {
                this.connect();
            }
            try {
                this.mChannel.basicPublish(exchange, routingKey, props, body);
                return;
            } catch (AlreadyClosedException | IOException e) {
                this.setConnStatus(CONNECTIONSTATUS.DISCONNECTED);
                if(tryTimes == 0){
                    throw new IllegalArgumentException("发送两次失败");
                }
            }
        }
    }

    public void sendMsg(String rotingKey, byte[] body) {
        this.sendMsg(this.mExchange, rotingKey, MessageProperties.TEXT_PLAIN, body);
    }

    public void setmExchange(String mExchange) {
        this.mExchange = mExchange;
    }

    public void setmExchangeDurable(boolean mExchangeDurable) {
        this.mExchangeDurable = mExchangeDurable;
    }

    public void setmDurable(boolean mDurable) {
        this.mDurable = mDurable;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }
}
