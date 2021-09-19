package com.codereview.tcp.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.ip.tcp.TcpOutboundGateway;
import org.springframework.integration.ip.tcp.connection.AbstractClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.CachingClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNioClientConnectionFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class TcpClientConfig implements ApplicationEventPublisherAware {

	/** The host. */
	@Value("${tcp.server.host}")
	private String host;

	/** The port. */
	@Value("${tcp.server.port}")
	private int port;

	/** The connection pool size. */
	@Value("${tcp.client.connection.poolSize}")
	private int connectionPoolSize;

	/** The application event publisher. */
	private ApplicationEventPublisher applicationEventPublisher;

	/**
	 * Sets the application event publisher.
	 *
	 * @param applicationEventPublisher the new application event publisher
	 */
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	/**
	 * Client connection factory.
	 *
	 * @return the abstract client connection factory
	 */
	@Bean
	public AbstractClientConnectionFactory clientConnectionFactory() {
		TcpNioClientConnectionFactory tcpNioClientConnectionFactory = new TcpNioClientConnectionFactory(host, port);
		tcpNioClientConnectionFactory.setUsingDirectBuffers(true);
		tcpNioClientConnectionFactory.setApplicationEventPublisher(applicationEventPublisher);
		return new CachingClientConnectionFactory(tcpNioClientConnectionFactory, connectionPoolSize);
	}

	/**
	 * Outbound channel.
	 *
	 * @return the message channel
	 */
	@Bean
	public MessageChannel outboundChannel() {
		return new DirectChannel();
	}

	/**
	 * Outbound gateway.
	 *
	 * @param clientConnectionFactory the client connection factory
	 * @return the message handler
	 */
	@Bean
	@ServiceActivator(inputChannel = "outboundChannel")
	public MessageHandler outboundGateway(AbstractClientConnectionFactory clientConnectionFactory) {
		TcpOutboundGateway tcpOutboundGateway = new TcpOutboundGateway();
		tcpOutboundGateway.setConnectionFactory(clientConnectionFactory);
		return tcpOutboundGateway;
	}

}
