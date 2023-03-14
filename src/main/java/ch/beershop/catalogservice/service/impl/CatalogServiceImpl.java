package ch.beershop.catalogservice.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.beershop.catalogservice.repository.BeerRepository;
import ch.beershop.catalogservice.repository.EvaluationRepository;
import ch.beershop.catalogservice.repository.FabricantRepository;
import ch.beershop.catalogservice.service.CatalogService;
import ch.beershop.catalogservice.service.model.Beer;
import ch.beershop.catalogservice.service.model.Evaluation;
import ch.beershop.catalogservice.service.model.Manufacturer;

@Service
public class CatalogServiceImpl implements CatalogService{

	@Autowired
	BeerRepository beerRepository;
	@Autowired
	FabricantRepository fabricantRepository;
	@Autowired
	EvaluationRepository evaluationRepository;
	
	
	@Override
	public Beer createBeer(Beer beer) {
		
		Optional<Manufacturer> fabricant = fabricantRepository.findById((long)beer.getFabricant().getId());
		
		beer.setFabricant(fabricant.get());
		
		Beer savedBeer = beerRepository.save(beer);
		
		return savedBeer;
	}
	
	
	@Override
	public Boolean deleteFabricant(Manufacturer fabricant) {
		
		fabricantRepository.delete(fabricant);
		
		return Boolean.TRUE;
		
	}
	

	
	@Override
	public Boolean isFabricantInUse(Manufacturer fabricant) {
		
		return beerRepository.findByFabricant_Id(fabricant.getId()).size() > 0;
	}
	
	
	@Override
	public Optional<Beer> getBeerById(Integer id) {
		
		Optional<Beer> beer = beerRepository.findById((Long.valueOf(id)));
		
		return beer;
	}
	
	@Override
	public Boolean deleteBeer(Beer beer) {
		
		beerRepository.delete(beer);
		
		return Boolean.TRUE;
	}
	
	@Override
	public Manufacturer creatManufacturer(Manufacturer fabricant) {
		
		Manufacturer savedFabricant = fabricantRepository.save(fabricant);
		
		return savedFabricant;
		
	}

	@Override
	public List<Beer> getAllBeer() {
		List<Beer> result = new ArrayList<Beer>();
		beerRepository.findAll().forEach(result::add);
		return result;
	}
	
	@Override
	public List<Manufacturer> getAllFabricant() {
		List<Manufacturer> result = new ArrayList<Manufacturer>();
		fabricantRepository.findAll().forEach(result::add);
		return result;
	}
	
	@Override
	public Optional<Manufacturer> getFabricantById(Integer id){
		
		return fabricantRepository.findById(Long.valueOf(id));
	}
	
	
	@Override
	public List<Beer> getBeersByName(String name){
		
		return beerRepository.findByName(name);
	}
	
	@Override
	public Evaluation addEvaluationToBeer(Evaluation evaluation, Long beerId){
		
		return evaluationRepository.save(evaluation);
		
	}
	
	@Override
	public List<Evaluation> getAllEvaluationsByFabricant(Long fabricantId){
		
		return evaluationRepository.findByBeer_Fabricant_Id(fabricantId);
		
	}

}
