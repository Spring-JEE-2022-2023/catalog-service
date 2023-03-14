package ch.beershop.catalogservice.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import ch.beershop.catalogservice.service.model.Beer;
import ch.beershop.catalogservice.service.model.Manufacturer;

@Repository
public interface FabricantRepository extends CrudRepository<Manufacturer, Long>, PagingAndSortingRepository<Manufacturer, Long> {

}
