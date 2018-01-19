package com.test.rabbitmq.base;

import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class BaseMqReceiver extends BaseMQer {

    private static Logger log = LoggerFactory.getLogger(BaseMqReceiver.class);

    private QueueingConsumer mConsumer;

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
    //是否是独占队列
    private boolean mQueueExclusive = false;

    private int mQos = 10;

    private int timeout = 0;

    private boolean isClose = false;

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
                this.mChannel.queueDeclare();
            }

            Iterator<String> it = this.mRoutingKeys.iterator();
            while (it.hasNext()) {
                String routingKey = it.next();
                log.info("add routeKey mQueue<{}> mExchange<{}> routeKey<{}>", new Object[]{this.queue, this.mExchange, routingKey});
                mChannel.queueBind(queue, mExchange, routingKey);
            }
            this.mChannel.basicQos(mQos);
            this.setmConsumer(new QueueingConsumer(this.mChannel));
            this.mChannel.basicConsume(queue, mAutoAct, this.mConsumer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public QueueingConsumer.Delivery recvDelivery() {
        return this.recvDelivery(0L);
    }

    public QueueingConsumer.Delivery recvDelivery(long to) {
        while (true) {
            try {
                if (this.getConnStatus() == CONNECTIONSTATUS.DISCONNECTED) {
                    this.connect();
                }

                QueueingConsumer.Delivery delivery = null;
                if (to == 0L) {
                    to = this.timeout;
                }

                if (to > 0L) {
                    delivery = this.mConsumer.nextDelivery(to);
                    if (delivery == null) {
                    }
                } else {
                    delivery = this.mConsumer.nextDelivery();
                }

                return delivery;
            } catch (ShutdownSignalException var4) {
                this.setConnStatus(CONNECTIONSTATUS.DISCONNECTED);
                var4.printStackTrace();

            } catch (InterruptedException | ConsumerCancelledException var5) {
                var5.printStackTrace();
            }
        }
    }

    public void handleCancel() {
        log.debug("cancel mq recving");
        try {
            this.mConsumer.handleCancel("");
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }

    public void close() {
        this.isClose = true;
        this.handleCancel();
        super.close();
    }

    protected BaseMqReceiver(String host, int port, String userName, String password, String vhost) {
        super(host, port, userName, password, vhost);
        this.mRoutingKeys = new HashSet<>();
    }

    public void setmConsumer(QueueingConsumer mConsumer) {
        this.mConsumer = mConsumer;
    }

    public void setmRoutingKeys(Set<String> mRoutingKeys) {
        this.mRoutingKeys = mRoutingKeys;
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
