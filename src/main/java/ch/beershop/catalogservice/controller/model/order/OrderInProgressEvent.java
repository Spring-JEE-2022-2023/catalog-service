package ch.beershop.catalogservice.controller.model.order;

import java.math.BigDecimal;
import java.util.Date;

public class OrderInProgressEvent {

	private Long cartSubmitedId;
	
	private Date creationDate;
	
	private Date ordredDate;
	
	private Long orderId;
	
	private BigDecimal totalAmount;

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Long getCartSubmitedId() {
		return cartSubmitedId;
	}

	public void setCartSubmitedId(Long cartSubmitedId) {
		this.cartSubmitedId = cartSubmitedId;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getOrdredDate() {
		return ordredDate;
	}

	public void setOrdredDate(Date ordredDate) {
		this.ordredDate = ordredDate;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	
	
}
