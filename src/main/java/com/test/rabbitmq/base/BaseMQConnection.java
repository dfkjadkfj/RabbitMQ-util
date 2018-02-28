package com.test.rabbitmq.base;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class BaseMQConnection {
    private static Logger log = LoggerFactory.getLogger(BaseMQConnection.class);

    private ConnectionFactory mFactory;
    private Connection mConnection;
    private BaseMQer.CONNECTIONSTATUS mConnStatus;

    public BaseMQConnection(String host, int port, String userName, String password, String vhost) {
        this.mConnStatus = BaseMQer.CONNECTIONSTATUS.DISCONNECTED;
        this.mFactory = new ConnectionFactory();
        this.mFactory.setHost(host);
        this.mFactory.setPort(port);
        this.mFactory.setUsername(userName);
        this.mFactory.setPassword(password);
        this.mFactory.setVirtualHost(vhost);
        //设置连接超时
        this.mFactory.setConnectionTimeout(10000);
        //设置连接恢复间隔。默认值是5000
//        this.mFactory.setNetworkRecoveryInterval(5000);
        //设置请求的心跳 param： 最初请求的心跳间隔，以秒为单位
//        this.mFactory.setRequestedHeartbeat(6000);
        /**
         * 连接恢复机制
         *      1.设置ConnectionFactory.automaticRecovery = true
         *      2.在创建链接是的时候实例化AutorecoveringConnection
         *      3.调用init方法会set 链接恢复的listener
         */
        //设置崩溃自动恢复  默认fasle
//        this.mFactory.setAutomaticRecoveryEnabled(true);
        //设置自动连接恢复间隔  参数如果是int就是秒  如果是long就是毫秒
//        this.mFactory.setNetworkRecoveryInterval(3);
        //（队列，交换，绑定，消费者）恢复
//        this.mFactory.setTopologyRecoveryEnabled(true);

        this.log.info("set mq connection: username<{}> host<{}> port<{}>", new Object[]{userName, host, port, vhost});
    }

    /**
     * 创建频道
     */
    public Channel createChannel() throws IOException {
        int count = 2;
        while (count-- > 0) {
            if (this.mConnection == null) {
                this.mConnection = this.getFactory().newConnection();
            }

            if (mConnection.isOpen())
                return this.mConnection.createChannel();
            else
                this.mConnection = null;
        }
        return null;
    }

    public ConnectionFactory getFactory() {
        return this.mFactory;
    }

    public void setmConnStatus(BaseMQer.CONNECTIONSTATUS mConnStatus) {
        this.mConnection = mConnection;
    }

    public BaseMQer.CONNECTIONSTATUS getmConnStatus() {
        return this.mConnStatus;
    }

    public void close() {
        try {
            if (this.mConnection != null) {
                this.mConnection.close();
                this.mConnection = null;
            }
        } catch (IOException var2) {
            this.log.warn("{}", var2.getMessage());
        }

    }
}
