package ch.beershop.catalogservice.controller.model.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import ch.beershop.catalogservice.service.model.Beer;


public class BeerCreationRequest {

	@NotNull(message = "Le stock est requis")
	private Integer stock;
	@NotEmpty(message = "Le nom est requis")
	private String name;
	@NotNull(message = "Le prix est requis")
	private BigDecimal price;
	@NotNull(message = "L'identifiant du fabricant est requis")
	private Integer fabricantId;
	public Integer getStock() {
		return stock;
	}
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Integer getFabricantId() {
		return fabricantId;
	}
	public void setFabricantId(Integer fabricantId) {
		this.fabricantId = fabricantId;
	}
	
	
}
