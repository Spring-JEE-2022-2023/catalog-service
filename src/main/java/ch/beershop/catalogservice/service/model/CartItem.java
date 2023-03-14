package ch.beershop.catalogservice.service.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class CartItem {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private int nbOfItems;
	
	private BigDecimal itemPrice;
	
	@JsonIgnore
	@ManyToOne
    @JoinColumn(name="cart_id", nullable=false)
	private Cart cart;
	
	@JsonIgnore
	@ManyToOne
	private Beer beer;

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getNbOfItems() {
		return nbOfItems;
	}

	public void setNbOfItems(int nbOfItems) {
		this.nbOfItems = nbOfItems;
	}

	public BigDecimal getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(BigDecimal itemPrice) {
		this.itemPrice = itemPrice;
	}

	public Beer getBeer() {
		return beer;
	}

	public void setBeer(Beer beer) {
		this.beer = beer;
	}
	
	
	
	
}
