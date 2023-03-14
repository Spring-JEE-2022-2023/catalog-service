package ch.beershop.catalogservice.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;

import ch.beershop.catalogservice.controller.model.request.AddBeerToCartRequest;
import ch.beershop.catalogservice.controller.model.request.BeerCreationRequest;
import ch.beershop.catalogservice.controller.model.request.CartCreationRequest;
import ch.beershop.catalogservice.controller.model.response.BeerNotFoundResponse;
import ch.beershop.catalogservice.controller.model.response.CartAlreadySubmittedResponse;
import ch.beershop.catalogservice.controller.model.response.CartItemNotFoundResponse;
import ch.beershop.catalogservice.controller.model.response.CartNotFoundResponse;
import ch.beershop.catalogservice.controller.model.response.CartNotSubmittedResponse;
import ch.beershop.catalogservice.controller.model.response.ManufacturerNotFoundResponse;
import ch.beershop.catalogservice.service.CartService;
import ch.beershop.catalogservice.service.CatalogService;
import ch.beershop.catalogservice.service.CommandService;
import ch.beershop.catalogservice.service.model.Beer;
import ch.beershop.catalogservice.service.model.Cart;
import ch.beershop.catalogservice.service.model.CartItem;
import ch.beershop.catalogservice.service.model.Manufacturer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value="/cart")
public class CartController {

	@Autowired
	CartService cartService;
	@Autowired
	CommandService commandService;
	@Autowired
	CatalogService catalogService;
	
	Logger logger = Logger.getLogger(CartController.class.getName());
	
	@Operation(summary = "Get a cart",
            description = "Get a cart by it's id"          
	)
	@ApiResponse(responseCode = "200", description = "Cart found")
	@ApiResponse(responseCode = "404", description = "No Cart found")
	@Tag(name = "Cart")
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getCartById(@PathVariable("id") Integer id) {
		logger.info("Get cart by id: " + id);
		
		Optional<Cart> cart = cartService.getCartById(id);
		
		if(cart.isPresent()) {
			return ResponseEntity.ok(cart.get());
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CartNotFoundResponse(Long.valueOf(id)));
		}
	}
	
	
	@Operation(summary = "Create a cart",
            description = " Cart creation."   
    )
    @ApiResponse(responseCode = "200", description = "Cart created")
    @ApiResponse(responseCode = "400", description = "Malformed body")
    @Tag(name = "Cart")
	@PostMapping
	public ResponseEntity<?> createCart() {
		Cart newCart = cartService.createCart(Cart.newCart());
		return ResponseEntity.ok(newCart);
	}
	
	@Operation(summary = "Submit a command",
            description = "Submit a command."   
    )
    @ApiResponse(responseCode = "200", description = "Command submited")
    @ApiResponse(responseCode = "400", description = "Malformed body")
    @Tag(name = "Cart")
	@PostMapping(value = "{id}/submit")
	public ResponseEntity<?> submitCommand(@PathVariable("id") Integer cartId) throws JsonProcessingException {
		
		//Todo cart not exist
		Cart cart = cartService.getCartById(cartId).get();
		
		if(cart.getOrderSubmited()) {
			return ResponseEntity.ok(new CartAlreadySubmittedResponse(cart.getId()));
		}
		
		cart = commandService.submitCommand(cart);
		
		//submit error, si pas soumis
		if(!cart.getOrderSubmited()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CartNotSubmittedResponse(cart.getId()));
		}
		
		
		return ResponseEntity.ok(cart);
	}
	
	
	@Operation(summary = "Add beer to a cart",
            description = " Add beer to cart. Cart muss exist"   
    )
    @ApiResponse(responseCode = "201", description = "Beer added")
    @ApiResponse(responseCode = "400", description = "Malformed body")
	@ApiResponse(responseCode = "404", description = "Cart doesn't exist")
	@ApiResponse(responseCode = "404", description = "Beer doesn't exist")
    @Tag(name = "Cart")
	@PostMapping(value = "/{id}/item")
	public ResponseEntity<?> addBeerToCart(@PathVariable("id") Integer cartId,@Valid @RequestBody AddBeerToCartRequest addBeerDto) {
		
		Optional<Cart> cart = cartService.getCartById(cartId);
		
		if(cart.isPresent()) {
			
			Optional<Beer> beer = catalogService.getBeerById(addBeerDto.getBeerId());
			
			if (beer.isPresent()) {
				
				Cart toAddCart = cart.get();
				
				CartItem cartItem = new CartItem();
				cartItem.setBeer(beer.get());
				cartItem.setNbOfItems(addBeerDto.getNbreItems());
				cartItem.setCart(toAddCart);
				
				CartItem created = cartService.addBeerToCart(cartItem);
				
				URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				          .path("/{id}")
				          .buildAndExpand(created.getId())
				          .toUri();
				
				return ResponseEntity.created(uri).body(created);
				
			}else{
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BeerNotFoundResponse(Long.valueOf(addBeerDto.getBeerId())));
			}
			
			
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CartNotFoundResponse(Long.valueOf(cartId)));
		}
		
		
	}
	
	@Operation(summary = "Delete a cart item",
            description = "Cart item deletion. Delete a cart by it's id"   
    )
    @ApiResponse(responseCode = "204", description = "Cart deleted")
    @ApiResponse(responseCode = "404", description = "Beer id not found")
    @Tag(name = "Cart")
    @DeleteMapping(value = "/{cartId}/item/{itemId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable("cartId") Integer cartId,@PathVariable("itemId") Integer itemId ){
    	logger.info("Delete cartItem by id: " + itemId);
		
		Optional<Cart> cart = cartService.getCartById(cartId);
		
		
		if(cart.isPresent()) {
			
			Cart c = cart.get();
			
			Optional<CartItem> cartItem = c.getItems().stream().filter(ca -> ca.getId().equals(Long.valueOf(itemId))).findFirst();
			
			if(cartItem.isPresent()) {
				
				cartService.deleteCartItem(cartItem.get());
				
				return ResponseEntity.noContent().build();
				
			}else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CartItemNotFoundResponse(Long.valueOf(itemId)));
			}
			
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CartNotFoundResponse(Long.valueOf(cartId)));
		}
    }
}
