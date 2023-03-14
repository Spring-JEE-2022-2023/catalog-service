package ch.beershop.catalogservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import ch.beershop.catalogservice.service.model.Cart;

public interface CommandService {

	Cart submitCommand(Cart cart) throws JsonProcessingException;
	
}
