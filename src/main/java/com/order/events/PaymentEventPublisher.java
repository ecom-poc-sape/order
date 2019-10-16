package com.order.events;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.support.MessageBuilder;

import com.order.domain.Order;

@EnableBinding(PaymentEvent.class)
public class PaymentEventPublisher {

	@Bean
	public MessageSource<Order> orderMessage() {
		return () -> MessageBuilder.withPayload(new Order()).build();
	}

}
