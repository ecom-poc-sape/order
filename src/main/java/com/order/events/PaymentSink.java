package com.order.events;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface PaymentSink {
	public static final String INPUT = "payment_status";
	@Input(INPUT)
	SubscribableChannel paymentStatus();

}
