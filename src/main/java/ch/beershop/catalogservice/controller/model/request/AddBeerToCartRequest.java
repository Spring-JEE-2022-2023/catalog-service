package ch.beershop.catalogservice.controller.model.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import ch.beershop.catalogservice.service.model.Beer;

public class AddBeerToCartRequest {

	
	
	@NotNull(message = "L'identifiant de la bière est requis")
	private Integer beerId;
	
	@NotNull(message = "Le nombre d'éléments est requis")
	private Integer nbreItems;

	

	public Integer getBeerId() {
		return beerId;
	}

	public void setBeerId(Integer beerId) {
		this.beerId = beerId;
	}

	public Integer getNbreItems() {
		return nbreItems;
	}

	public void setNbreItems(Integer nbreItems) {
		this.nbreItems = nbreItems;
	}
}
