package com.test.rabbitmq.base;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public abstract class BaseMQer {

    private static Logger log = LoggerFactory.getLogger(BaseMQer.class);

    private BaseMQConnection mBaseMQConnection;

    protected Channel mChannel;

    protected BaseMQer(String host, int port, String userName, String password, String vhost) {
        this.mBaseMQConnection = new BaseMQConnection(host, port, userName, password, vhost);
    }

    protected BaseMQer(BaseMQConnection mBaseMQConnection) {
        this.mBaseMQConnection = mBaseMQConnection;
    }

    protected Channel createChannel() throws IOException {
        return this.mBaseMQConnection.createChannel();
    }

    public void setConnStatus(CONNECTIONSTATUS mConnStatus) {
        this.mBaseMQConnection.setmConnStatus(mConnStatus);
    }

    public CONNECTIONSTATUS getConnStatus() {
        return this.mBaseMQConnection.getmConnStatus();
    }


    public void close() {
        try {
            if (this.mChannel != null) {
                this.mChannel.close();
                this.mChannel = null;
            }

            if (this.mBaseMQConnection != null) {
                this.mBaseMQConnection.close();
                this.mBaseMQConnection = null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static enum CONNECTIONSTATUS {
        CONNECTED, DISCONNECTED;

        private CONNECTIONSTATUS() {
        }
    }
}
