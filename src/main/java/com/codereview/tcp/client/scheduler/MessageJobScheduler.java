package com.codereview.tcp.client.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.codereview.tcp.client.service.MessageService;

@Component
public class MessageJobScheduler {

	/** The message service. */
	private MessageService messageService;

	/**
	 * Instantiates a new message job scheduler.
	 *
	 * @param messageService the message service
	 */
	@Autowired
	public MessageJobScheduler(MessageService messageService) {
		this.messageService = messageService;
	}

	/**
	 * Send message job.
	 */
	@Scheduled(fixedDelay = 1000L)
	public void sendMessageJob() {
		messageService.sendMessage();
	}

}
