package ch.beershop.catalogservice.service.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class Cart {

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private Date creationDate;
	
	private Date ordredDate;
	
	private Boolean orderSubmited = Boolean.FALSE;
	
	public Boolean getOrderSubmited() {
		return orderSubmited;
	}

	public void setOrderSubmited(Boolean orderSubmited) {
		this.orderSubmited = orderSubmited;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	private Long orderId;
	
	@OneToMany(mappedBy ="cart")
	private List<CartItem> items;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public List<CartItem> getItems() {
		return items;
	}

	public void setItems(List<CartItem> items) {
		this.items = items;
	}
	
	public static Cart newCart() {
		Cart cart = new Cart();
		cart.setCreationDate(new Date());
		return cart;
	}
	
	
	public BigDecimal computeTotalCartAmount() {
		
		BigDecimal totalAmount = BigDecimal.ZERO;
		
		for(CartItem item : items) {
			totalAmount = totalAmount.add(item.getItemPrice().multiply(new BigDecimal(item.getNbOfItems())));
		}
		
		
		return totalAmount;
	}
}
