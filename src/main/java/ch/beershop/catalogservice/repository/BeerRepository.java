package ch.beershop.catalogservice.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import ch.beershop.catalogservice.service.model.Beer;
import ch.beershop.catalogservice.service.model.Manufacturer;

@Repository
public interface BeerRepository extends CrudRepository<Beer, Long>, PagingAndSortingRepository<Beer, Long> {

	List<Beer> findByName(String name);
	
	List<Beer> findByFabricant_Id(Long fabricantId);
}
