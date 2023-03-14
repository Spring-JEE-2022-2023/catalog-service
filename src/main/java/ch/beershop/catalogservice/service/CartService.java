package ch.beershop.catalogservice.service;

import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;

import ch.beershop.catalogservice.service.model.Beer;
import ch.beershop.catalogservice.service.model.Cart;
import ch.beershop.catalogservice.service.model.CartItem;

public interface CartService {

	Cart createCart(Cart cart);

	Optional<Cart> getCartById(Integer id);

	CartItem addBeerToCart(CartItem cartItem);

	Boolean deleteCartItem(CartItem cartItem);

	Boolean isBeerInUse(Beer beer);

}
