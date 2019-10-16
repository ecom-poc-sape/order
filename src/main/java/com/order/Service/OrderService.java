package com.order.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.order.domain.Order;
import com.order.domain.OrderStatus;
import com.order.events.PaymentEvent;
import com.order.repository.OrderRepository;
import com.sapient.ecomm_commons.domain.Product;

@Service
public class OrderService {

	@Value("${cartapp.uri}")
	private String cartURI;

	@Value("${inventory.uri}")
	private String inventoryURI;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	private OrderProcessor orderProcessor;

	@Autowired
	private PaymentEvent paymentEvent;

	private static Logger logger = LoggerFactory.getLogger(OrderService.class);

	/*
	 * This method is called by Order service by user in order to initiate the
	 * order.
	 */
	public String initiateOrder(String customerId) {
		String orderStatus;

		Order latestOrder = orderProcessor.getLatestOrder(customerId);
		String orderId = null;
		List<String> products = latestOrder.getProducts();
		latestOrder.setStatus(OrderStatus.IN_PROGRESS);
		
		List<LinkedHashMap> list =  restTemplate.getForObject(inventoryURI + "products", List.class);
		System.out.println(list);
		
		 double totalPrice = 0;
		 
		 for(LinkedHashMap map : list) {
			 if(products.contains((String)map.get("id"))) {
				 totalPrice += (Double)map.get("price");
			 }
		 }
		 
		 latestOrder.setTotalPrice(totalPrice);
		// To update the product count before initiating the order
		StringBuilder inventoryreduceURI = new StringBuilder(inventoryURI);
		// To update the product count if payment fails
		StringBuilder inventoryIncreaseURI = new StringBuilder(inventoryURI);
		// Get the list of products from cart.

		if (products != null && products.size() > 0) {
			inventoryreduceURI.append("products/ids/");
			for (String str : products) {
				inventoryreduceURI.append(str).append(",");
			}
			inventoryreduceURI.append("/task/remove");
			logger.info(inventoryreduceURI.toString());
			// Request Inventory to update the product count in DB.
			ResponseEntity<String> response = restTemplate.exchange(inventoryreduceURI.toString(), HttpMethod.PUT, null,
					String.class);
			// Persist the order in DB.
			Order savedOrder = orderRepository.save(latestOrder);
			orderId = savedOrder.getId();
			orderStatus = "orderinitiated";
			// Kafka event generation below
			initiatePaymentEvent(savedOrder);
		} else {
			orderStatus = "emptycart";
		}

		return orderId;
	}

	public void initiatePaymentEvent(Order order) {
		// code to trigger Kafka event to initiate payment.
		paymentEvent.paymentMessageChannel().send(MessageBuilder.withPayload(order).build());

	}
}
