package com.order.events;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface CartEvent {
	public static final String TOPIC = "cart";
	@Output(TOPIC)
	MessageChannel cartMessageChannel();

}
