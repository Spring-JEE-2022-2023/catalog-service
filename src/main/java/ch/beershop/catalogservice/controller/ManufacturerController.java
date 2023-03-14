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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ch.beershop.catalogservice.controller.model.request.FabricantCreationRequest;
import ch.beershop.catalogservice.controller.model.response.EntityDeletedResponse;
import ch.beershop.catalogservice.controller.model.response.ManufacturerInUseResponse;
import ch.beershop.catalogservice.controller.model.response.ManufacturerNotFoundResponse;
import ch.beershop.catalogservice.service.CatalogService;
import ch.beershop.catalogservice.service.model.Beer;
import ch.beershop.catalogservice.service.model.Evaluation;
import ch.beershop.catalogservice.service.model.Manufacturer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value="/manufacturer")
public class ManufacturerController {

Logger logger = Logger.getLogger(BeerController.class.getName());
	
	@Autowired
	CatalogService catalogService;
	
	@Operation(summary = "Create a manufacturer",
            description = "Manufacturer creation."   
    )
    @ApiResponse(responseCode = "201", description = "Manufacturer created")
    @ApiResponse(responseCode = "400", description = "Malformed body")
    @Tag(name = "Fabricant")
	@PostMapping
	public ResponseEntity<Manufacturer> createFabricant(@Valid @RequestBody FabricantCreationRequest fabricant) {
		logger.info("Created fabricant: " + fabricant);
		
		
		
		Manufacturer createdFabricant = catalogService.creatManufacturer(new Manufacturer(fabricant.getName()));
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
		          .path("/{id}")
		          .buildAndExpand(createdFabricant.getId())
		          .toUri();

		return ResponseEntity.created(uri).body(createdFabricant);
	}
	
	@Operation(summary = "Create a manufacturer",
            description = "Manufacturer creation."   
    )
    @ApiResponse(responseCode = "200", description = "Manufacturer created")
    @ApiResponse(responseCode = "404", description = "Manufacturer doesn't exist")
	@ApiResponse(responseCode = "400", description = "Manufacturer is still in use, or Bad Request malformed body")
    @Tag(name = "Fabricant")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteFabricantById(@PathVariable("id") Integer id) {
		logger.info("Delete fabricant by id: " + id);
		
		Optional<Manufacturer> fabricant = catalogService.getFabricantById(id);
		
		if(fabricant.isPresent()) {
			
			Boolean isFabricantInUse = catalogService.isFabricantInUse(fabricant.get());
			
			if(isFabricantInUse) {
				return ResponseEntity.badRequest().body(new ManufacturerInUseResponse(id));
			}else {
				catalogService.deleteFabricant(fabricant.get());
				return ResponseEntity.ok(new EntityDeletedResponse(Long.valueOf(id),Manufacturer.class));
			}
			
			
		}else {
			return ResponseEntity.notFound().build();
		}
		
		
	}
	
	@Operation(summary = "Get a manufacturer",
            description = "Get a manufacturer"   
    )
	@ApiResponse(responseCode = "200", description = "Ok")
    @ApiResponse(responseCode = "404", description = "Manufacturer doesn't exist")
    @Tag(name = "Fabricant")
	@GetMapping(value = "/{id}")
	public ResponseEntity<Manufacturer> getFabricantById(@PathVariable("id") Integer id) {
		logger.info("Get fabricant by id: " + id);
		
		Optional<Manufacturer> fabricant = catalogService.getFabricantById(id);
		
		if(fabricant.isPresent()) {
			return ResponseEntity.ok(fabricant.get());
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@Operation(summary = "Get all manufacturers",
            description = "Get all manufacturers"   
    )
	@ApiResponse(responseCode = "200", description = "Ok")
    @Tag(name = "Fabricant")
	@GetMapping
	public ResponseEntity<List<Manufacturer>> getAllFabricant() {
		
		
		List<Manufacturer> fabricants = catalogService.getAllFabricant();
		
		return ResponseEntity.ok(fabricants);
	}
	
	@Operation(summary = "Get all eval for a manufacturer",
            description = "Get all eval  manufacturer"   
    )
	@ApiResponse(responseCode = "200", description = "Ok")
	@ApiResponse(responseCode = "404", description = "Manufacturer doesn't exist")
    @Tag(name = "Fabricant")
	@GetMapping("/{id}/eval")
	public ResponseEntity<?> getAllEvalByManufactuer(@PathVariable("id") Integer id){
		
		Optional<Manufacturer> fabricant = catalogService.getFabricantById(id);
		
		if(fabricant.isPresent()) {
			return ResponseEntity.ok(catalogService.getAllEvaluationsByFabricant(Long.valueOf(id)));
		}else{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ManufacturerNotFoundResponse(id));
		}
		
		
	}

	
}
