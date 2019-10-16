package com.order.domain;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.sapient.ecomm_commons.domain.PaymentDetails;

@Document
public class Order {

	@Id
	private String id;
	private List<String> products;
	private String customerId;
	private OrderStatus status;
	private Double totalPrice;
	private PaymentDetails details;

	public Order(List<String> products, String userId) {
		this.products = products;
		this.customerId = userId;
	}

	public Order(String customerId, List<String> products, OrderStatus status) {
		super();
		this.products = products;
		this.customerId = customerId;
		this.status = status;
	}

	public Order() {
	}

	public String getId() {
		return id;
	}

	public void setId(String orderId) {
		this.id = orderId;
	}

	public List<String> getProducts() {
		return products;
	}

	public void setProducts(List<String> products) {
		this.products = products;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String userId) {
		this.customerId = userId;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public PaymentDetails getDetails() {
		return details;
	}

	public void setDetails(PaymentDetails details) {
		this.details = details;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", products=" + products + ", customerId=" + customerId + ", status=" + status
				+ ", totalPrice=" + totalPrice + ", details=" + details + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (customerId == null) {
			if (other.customerId != null)
				return false;
		} else if (!customerId.equals(other.customerId))
			return false;
		return true;
	}

}
