package ch.beershop.catalogservice.controller;

import java.net.URI;
import java.util.Date;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ch.beershop.catalogservice.controller.model.request.BeerCreationRequest;
import ch.beershop.catalogservice.controller.model.request.EvaluationCreationRequest;
import ch.beershop.catalogservice.controller.model.response.BeerInUseResponse;
import ch.beershop.catalogservice.controller.model.response.BeerNotFoundResponse;
import ch.beershop.catalogservice.controller.model.response.EntityDeletedResponse;
import ch.beershop.catalogservice.controller.model.response.ManufacturerNotFoundResponse;
import ch.beershop.catalogservice.service.CartService;
import ch.beershop.catalogservice.service.CatalogService;
import ch.beershop.catalogservice.service.model.Beer;
import ch.beershop.catalogservice.service.model.Evaluation;
import ch.beershop.catalogservice.service.model.Manufacturer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value="/beer")
public class BeerController {

	Logger logger = Logger.getLogger(BeerController.class.getName());
	
	@Autowired
	CatalogService catalogService;
	@Autowired
	CartService cartService;
	
	
    @Operation(summary = "Create a beer",
            description = "Beer creation. The fabricant id muss be specified and musst exist"   
    )
    @ApiResponse(responseCode = "201", description = "Beer created")
    @ApiResponse(responseCode = "404", description = "Fabricant id not found")
    @ApiResponse(responseCode = "400", description = "Malformed body")
    @Tag(name = "Beer")
	@PostMapping
	public ResponseEntity<?> createBeer(@Valid @RequestBody BeerCreationRequest beerDto) {
		logger.info("Beer creation: " + beerDto);
		
		Optional<Manufacturer> fabricant = catalogService.getFabricantById(beerDto.getFabricantId());
		
		if(fabricant.isPresent()) {
			
			Beer beer = convertToBeer(beerDto, fabricant);
			
			Beer createdBeer = catalogService.createBeer(beer);
			
			URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
			          .path("/{id}")
			          .buildAndExpand(createdBeer.getId())
			          .toUri();
			
			return ResponseEntity.created(uri).body(createdBeer);
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ManufacturerNotFoundResponse(beerDto.getFabricantId()));
		}
				
	}
    
    @Operation(summary = "Update a beer",
            description = "Beer updating. The fabricant id muss be specified and muss exist"   
    )
    @ApiResponse(responseCode = "201", description = "Beer updated")
    @ApiResponse(responseCode = "404", description = "Fabricant id not found, or beerId not found")
    @ApiResponse(responseCode = "400", description = "Malformed body")
    @Tag(name = "Beer")
	@PutMapping(value = "/{id}")
	public ResponseEntity<?> updateBeer(@PathVariable("id") Integer id,@Valid @RequestBody BeerCreationRequest beerDto) {
		logger.info("Beer updating: " + beerDto);
		
		if(catalogService.getBeerById(id).isPresent()) {
			
			Optional<Manufacturer> fabricant = catalogService.getFabricantById(beerDto.getFabricantId());
			
			if(fabricant.isPresent()) {
				
				Beer beer = convertToBeer(beerDto, fabricant);
				beer.setId(Long.valueOf(id));
				
				Beer createdBeer = catalogService.createBeer(beer);
				
				URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				          .path("/{id}")
				          .buildAndExpand(createdBeer.getId())
				          .toUri();
				
				return ResponseEntity.created(uri).body(createdBeer);
			}else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ManufacturerNotFoundResponse(beerDto.getFabricantId()));
			}
			
		}else {
			return ResponseEntity.notFound().build();
		}
		
	}

    @Operation(summary = "Delete a beer",
            description = "Beer deletion. Delete a beer by it's id"   
    )
    @ApiResponse(responseCode = "200", description = "Beer deleted")
    @ApiResponse(responseCode = "404", description = "Beer id not found")
    @Tag(name = "Beer")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteBeer(@PathVariable("id") Integer id){
    	logger.info("Delete beer by id: " + id);
		
		Optional<Beer> beer = catalogService.getBeerById(id);
		
		if(beer.isPresent()) {
			
			Boolean isBeerInUse = cartService.isBeerInUse(beer.get());
			
			if(isBeerInUse) {
				return ResponseEntity.badRequest().body(new BeerInUseResponse(id));
			}else {
				catalogService.deleteBeer(beer.get());
				return ResponseEntity.ok(new EntityDeletedResponse(Long.valueOf(id),Beer.class));
			}
			
			
		}else {
			return ResponseEntity.notFound().build();
		}
    }

	@Operation(summary = "Get a beer",
	            description = "Get a beer by it's id"          
    )
    @ApiResponse(responseCode = "200", description = "Beer found")
    @ApiResponse(responseCode = "404", description = "No Beer found")
    @Tag(name = "Beer")
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getBeerById(@PathVariable("id") Integer id) {
		logger.info("Get beer by id: " + id);
		
		Optional<Beer> beer = catalogService.getBeerById(id);
		
		if(beer.isPresent()) {
			return ResponseEntity.ok(beer.get());
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BeerNotFoundResponse(Long.valueOf(id)));
		}
	}
	
	@Operation(summary = "Get all beer",
            description = "Get all beer"          
	)
	@ApiResponse(responseCode = "200", description = "Beer's list found")
	@Tag(name = "Beer")
	@GetMapping
	public ResponseEntity<List<Beer>> getAllBeer(@RequestParam(name="name") Optional<String> name) {
		logger.info("Get all beer with param: " + ((name.isPresent()) ? name.get() : "no param"));
		
		List<Beer> beers;
		
		if(name.isPresent()) {
			logger.info("Get all beers with name request param: " + name);
			beers = catalogService.getBeersByName(name.get());
			
		}else {
			logger.info("Get all beers");
			beers = catalogService.getAllBeer();
		}
		
		
		return ResponseEntity.ok(beers);
	}

	@Operation(summary = "Post evaluation for a beer",
            description = "Post evaluation for a beer"          
	)
	@ApiResponse(responseCode = "201", description = "Evaéuation created")
	@ApiResponse(responseCode = "404", description = "No Beer found for id")
	@Tag(name = "Beer")
	@PostMapping(value = "/{id}/eval")
	public ResponseEntity<?> addEvaluationToBeer(@PathVariable("id") Integer id, @RequestBody EvaluationCreationRequest evaluationRequest){
		logger.info("Create evaluation for beer id: " + id);
		
		Optional<Beer> optionnalBeer = catalogService.getBeerById(id);
		
		if(optionnalBeer.isPresent()) {
			
			Beer evaluatedBeer = catalogService.getBeerById(id).get();
			
			Evaluation evaluation = new Evaluation();
			evaluation.setNote(evaluationRequest.getNote());
			evaluation.setBeer(evaluatedBeer);
			evaluation.setDate(new Date());
			
			Evaluation created = catalogService.addEvaluationToBeer(evaluation, evaluatedBeer.getId());
			
			
			URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
			          .path("/{beerid}/eval/{id}")
			          .buildAndExpand(evaluatedBeer.getId(),created.getId())
			          .toUri();
			
			return ResponseEntity.created(uri).body(created);
			
		}else {
			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BeerNotFoundResponse(Long.valueOf(id)));
		}
		
	}
	
	
	private Beer convertToBeer(BeerCreationRequest beerDto, Optional<Manufacturer> fabricant) {
		Beer beer = new Beer();
		beer.setName(beerDto.getName());
		beer.setPrice(beerDto.getPrice());
		beer.setStock(beerDto.getStock());
		beer.setFabricant(fabricant.get());
		return beer;
	}
	
	
}
