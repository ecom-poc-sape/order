package com.order.Service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.order.domain.Order;
import com.order.repository.OrderRepository;
import com.sapient.ecomm_commons.domain.Product;

@Service
public class OrderService {

	@Value("${cartapp.uri}")
	private String cartURI;
	
	@Value("${inventory.uri}")
	private String inventoryURI;
	
	private String userId="chandan";
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	OrderRepository orderRepository;
	
	private static Logger logger = LoggerFactory.getLogger(OrderService.class);
	/*
	 * This method is called by Order service by user in order to initiate the order.
	 */
	public String initiateOrder() {
		String orderStatus;
		//To update the product count before initiating the order
		StringBuilder inventoryreduceURI=new StringBuilder(inventoryURI);
		//To update the product count if payment fails
		StringBuilder inventoryIncreaseURI=new StringBuilder(inventoryURI);
		//Get the list of products from cart.
		List<String> products =new ArrayList<String>();
		products = (List<String>) restTemplate.getForObject(cartURI+userId, List.class);
		if (products!=null && products.size()>0) {
			Order order=new Order(products,userId);
			inventoryreduceURI.append("products/ids/");
			for(String str:products) {
				inventoryreduceURI.append(str).append(",");
			}
			inventoryreduceURI.append("/task/remove");
			logger.info(inventoryreduceURI.toString());
			//Request Inventory to update the product count in DB.
			ResponseEntity <String> response = 
						restTemplate.exchange(inventoryreduceURI.toString(),HttpMethod.PUT, null,String.class);
			//Persist the order in DB.
			orderRepository.save(order);
			orderStatus="orderinitiated";
			//Kafka event generation below
			initiatePaymentEvent();
		}else {
			orderStatus = "emptycart";
		}

		return orderStatus;
	}
	private void initiatePaymentEvent(){
		//code to trigger Kafka event to initiate payment.
	}
}
