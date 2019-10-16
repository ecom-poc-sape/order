package com.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.order.Service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderRestController {

	@Autowired
	OrderService orderService;
	
	@PostMapping("/{customerId}")
	public ResponseEntity<String> initiateOrder(@PathVariable String customerId){
		String message = orderService.initiateOrder(customerId);
		
		System.out.println("message when placing the order:"+message);
		
		return (message != null && message.equals("orderinitiated"))?
				ResponseEntity.ok().body("Your order has been initiated. Redirecting to Payment gateway") 
				: ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	
	 
}
