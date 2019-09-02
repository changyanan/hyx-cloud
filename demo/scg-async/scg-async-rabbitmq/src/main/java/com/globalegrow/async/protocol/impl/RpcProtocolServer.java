package com.globalegrow.async.protocol.impl;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Delivery;
import com.rabbitmq.client.RpcServer;

public class RpcProtocolServer extends RpcServer{

    public RpcProtocolServer(Channel channel, String queueName) throws IOException {
    	super(channel, queueName);
    }

    @Override
    public void handleCast(Delivery request) {
    }

}
