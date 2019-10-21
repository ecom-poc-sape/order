package com.order.events;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.support.MessageBuilder;

@EnableBinding(CartEvent.class)
public class CartEventPublisher {

	@Bean
	public MessageSource<String> cartMessage() {
		return () -> MessageBuilder.withPayload("").build();
	}

}
