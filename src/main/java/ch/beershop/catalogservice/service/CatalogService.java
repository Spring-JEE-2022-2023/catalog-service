package ch.beershop.catalogservice.service;

import java.util.List;
import java.util.Optional;

import ch.beershop.catalogservice.service.model.Beer;
import ch.beershop.catalogservice.service.model.Evaluation;
import ch.beershop.catalogservice.service.model.Manufacturer;

public interface CatalogService {

	Beer createBeer(Beer beer);

	Optional<Beer> getBeerById(Integer id);
	
	Manufacturer creatManufacturer(Manufacturer fabricant);

	List<Beer> getAllBeer();

	List<Beer> getBeersByName(String name);

	Optional<Manufacturer> getFabricantById(Integer id);

	List<Manufacturer> getAllFabricant();

	Boolean deleteBeer(Beer beer);

	Boolean isFabricantInUse(Manufacturer fabricant);

	Boolean deleteFabricant(Manufacturer fabricant);

	Evaluation addEvaluationToBeer(Evaluation evaluation, Long beerId);

	List<Evaluation> getAllEvaluationsByFabricant(Long fabricantId);
	
}
