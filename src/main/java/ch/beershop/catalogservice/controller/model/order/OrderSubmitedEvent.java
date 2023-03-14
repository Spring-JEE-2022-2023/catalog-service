package ch.beershop.catalogservice.controller.model.order;

import java.math.BigDecimal;
import java.util.Date;

public class OrderSubmitedEvent {

	private BigDecimal totalCartAmount;
	
	public BigDecimal getTotalCartAmount() {
		return totalCartAmount;
	}
	public void setTotalCartAmount(BigDecimal totalCartAmount) {
		this.totalCartAmount = totalCartAmount;
	}
	public Long getCartId() {
		return cartId;
	}
	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}
	public Date getSubmitedDate() {
		return submitedDate;
	}
	public void setSubmitedDate(Date submitedDate) {
		this.submitedDate = submitedDate;
	}
	private Long cartId;
	private Date submitedDate;
	
}
