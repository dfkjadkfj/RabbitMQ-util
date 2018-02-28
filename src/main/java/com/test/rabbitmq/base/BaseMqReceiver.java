package com.test.rabbitmq.base;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BaseMqReceiver extends BaseMQer {

    private static Logger log = LoggerFactory.getLogger(BaseMqReceiver.class);

    private MyConsumer mConsumer;

    private ConsumerHandler consumerHandler;

    //TODO
    //这个版本先支持 同一个交换机下的不同 RK
    //可以做成不同的 交换级 不同的类型 不同的rk这种
    private Set<String> mRoutingKeys = null;

    private String mExchange;

    private String mType;
    ////是否持久化交换机
    private boolean mExchangeDurable = true;
    //是否持久化队列
    private boolean mQueueDurable = true;
    //是否自动应答
    private boolean mAutoAct = true;
    //是否自动删除队列(服务器不适用时候将其删除)
    private boolean mAutoDelete = false;

    private boolean hasQueue = false;
    //队列名字
    private String queue;
    //是否是独占队列(如果生明成独占队列值在当前链接有效,链接关闭 队列删除)
    private boolean mQueueExclusive = false;

    private int mQos = 10;

    private int timeout = 0;

    public void connect() {
        try {
            this.mChannel = this.createChannel();

            if (StringUtils.isNotEmpty(this.mExchange) && StringUtils.isNotEmpty(this.mType)) {
                //宣布一个非自动删除的交换机 如果设置第三个参数为true 则持久化这个交换机
                this.mChannel.exchangeDeclare(this.mExchange, this.mType, mExchangeDurable);
            }

            if (StringUtils.isNotEmpty(this.queue) && this.hasQueue) {//持久队列
                //声明一个队列
                this.mChannel.queueDeclare(this.queue, this.mQueueDurable, this.mQueueExclusive, this.mAutoDelete, null);
            } else {//临时队列
                this.queue = this.mChannel.queueDeclare().getQueue();
            }

            Iterator<String> it = this.mRoutingKeys.iterator();
            while (it.hasNext()) {
                String routingKey = it.next();
                log.info("add routeKey mQueue<{}> mExchange<{}> routeKey<{}>", new Object[]{this.queue, this.mExchange, routingKey});
                mChannel.queueBind(queue, mExchange, routingKey);
            }
            this.mChannel.basicQos(mQos);
            if (consumerHandler != null) {
                MyConsumer myConsumer = new MyConsumer(this.mChannel, consumerHandler, mAutoAct);
                this.mChannel.basicConsume(this.queue, mAutoAct, myConsumer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BaseMqReceiver(String host, int port, String userName, String password, String vhost) {
        super(host, port, userName, password, vhost);
        this.mRoutingKeys = new HashSet<>();
    }

    public void setConsumerHandler(ConsumerHandler consumerHandler) {
        this.consumerHandler = consumerHandler;
    }

    public void addRoutingKeys(String mRoutingKey) {
        this.mRoutingKeys.add(mRoutingKey);
    }

    public void setmExchange(String mExchange) {
        this.mExchange = mExchange;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public void setmExchangeDurable(boolean mExchangeDurable) {
        this.mExchangeDurable = mExchangeDurable;
    }

    public void setmQueueDurable(boolean mQueueDurable) {
        this.mQueueDurable = mQueueDurable;
    }

    public void setmAutoAct(boolean mAutoAct) {
        this.mAutoAct = mAutoAct;
    }

    public void setmAutoDelete(boolean mAutoDelete) {
        this.mAutoDelete = mAutoDelete;
    }

    public void setQueue(String queue) {
        this.queue = queue;
        this.hasQueue = true;
    }

    public void setmQueueExclusive(boolean mQueueExclusive) {
        this.mQueueExclusive = mQueueExclusive;
    }

    public void setmQos(int mQos) {
        this.mQos = mQos;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
