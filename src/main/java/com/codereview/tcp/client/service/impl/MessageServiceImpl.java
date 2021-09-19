package com.codereview.tcp.client.service.impl;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codereview.tcp.client.gateway.TcpClientGateway;
import com.codereview.tcp.client.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageServiceImpl.class);

    /** The tcp client gateway. */
    private TcpClientGateway tcpClientGateway;

    /**
     * Instantiates a new message service impl.
     *
     * @param tcpClientGateway the tcp client gateway
     */
    @Autowired
    public MessageServiceImpl(TcpClientGateway tcpClientGateway) {
        this.tcpClientGateway = tcpClientGateway;
    }

    /**
     * Send message.
     */
    @Override
    public void sendMessage() {
        String message = LocalDateTime.now().toString();
        LOGGER.info("Send message: {}", message);
        byte[] responseBytes = tcpClientGateway.send(message.getBytes());
        String response = new String(responseBytes);
        LOGGER.info("Receive response: {}", response);
    }

}
