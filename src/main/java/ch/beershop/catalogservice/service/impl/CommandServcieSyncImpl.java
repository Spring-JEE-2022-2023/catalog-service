package ch.beershop.catalogservice.service.impl;

import javax.jms.TextMessage;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.beershop.catalogservice.service.CommandService;
import ch.beershop.catalogservice.service.model.Cart;

@Service
@Profile("sync")
public class CommandServcieSyncImpl implements CommandService{

	@Override
	public Cart submitCommand(Cart cart) {
		
      throw new RuntimeException("not implemented yet");
    }
}
