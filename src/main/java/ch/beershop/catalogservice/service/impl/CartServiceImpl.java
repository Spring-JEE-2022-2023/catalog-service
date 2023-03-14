package ch.beershop.catalogservice.service.impl;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.beershop.catalogservice.controller.BeerController;
import ch.beershop.catalogservice.repository.BeerRepository;
import ch.beershop.catalogservice.repository.CartItemRepository;
import ch.beershop.catalogservice.repository.CartRepository;
import ch.beershop.catalogservice.service.CartService;
import ch.beershop.catalogservice.service.model.Beer;
import ch.beershop.catalogservice.service.model.Cart;
import ch.beershop.catalogservice.service.model.CartItem;

@Service
public class CartServiceImpl implements CartService {

	Logger logger = Logger.getLogger(CartServiceImpl.class.getName());
	
	@Autowired
	CartRepository cartReposisory;
	@Autowired
	CartItemRepository cartItemReposisory;
	@Autowired
	BeerRepository beerRepository;
	
	
	
	
	@Override
	public Cart createCart(Cart cart) {
		
		Cart c = null;
		
		try {		
			c =  cartReposisory.save(cart);
			
			
		}catch(Exception e) {
			System.out.println(e);
		}
		return c;

	}
	
	@Override
	public CartItem addBeerToCart(CartItem cartItem) {
		
		cartItem.setItemPrice(cartItem.getBeer().getPrice().multiply(new BigDecimal(cartItem.getNbOfItems())));
		
		Beer beer = cartItem.getBeer();
		beer.setStock(beer.getStock() - cartItem.getNbOfItems());
		beerRepository.save(beer);
		
		return cartItemReposisory.save(cartItem);
	}
	
	@Override
	public Optional<Cart> getCartById(Integer id) {
		
		Optional<Cart> cart = cartReposisory.findById((Long.valueOf(id)));
		
		return cart;
	}
	
	@Override
	public Boolean deleteCartItem(CartItem cartItem) {
		
		
		cartItemReposisory.delete(cartItem);
		
		Beer beer = cartItem.getBeer();
		beer.setStock(beer.getStock() + cartItem.getNbOfItems());
		beerRepository.save(beer);
		
		return Boolean.TRUE;
		
	}

	@Override
	public Boolean isBeerInUse(Beer beer) {
		
		return cartItemReposisory.findByBeer_Id(beer.getId()).size() > 0;
	}

	
	
		
	
	
}
