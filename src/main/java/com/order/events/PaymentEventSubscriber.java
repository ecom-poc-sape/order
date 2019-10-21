package com.order.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;

import com.order.domain.Order;
import com.order.domain.OrderStatus;
import com.order.repository.OrderRepository;
import com.sapient.ecomm_commons.domain.Payment;
import com.sapient.ecomm_commons.domain.PaymentStatus;

@EnableBinding(PaymentSink.class)
public class PaymentEventSubscriber {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CartEvent cartEvent;

	@StreamListener(PaymentSink.INPUT)
	//@SendTo(CartEvent.TOPIC)
	public void receive(Payment payment) {

		if (PaymentStatus.COMPLETED == payment.getStatus()) {
			logger.info("Payment status received..." + payment);
			Order order = orderRepository.findById(payment.getOrderId()).get();
			order.setStatus(OrderStatus.COMPLETED);
			orderRepository.save(order);
			logger.info("Order with id: {} completed", order.getId());
			//return order.getCustomerId();
			cartEvent.cartMessageChannel().send(MessageBuilder.withPayload(order.getCustomerId()).build());
			//paymentEvent.paymentMessageChannel().send(MessageBuilder.withPayload(order).build());
		}

		//return "";
	}

}
