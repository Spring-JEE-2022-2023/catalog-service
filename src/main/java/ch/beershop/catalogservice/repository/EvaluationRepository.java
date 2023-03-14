package ch.beershop.catalogservice.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import ch.beershop.catalogservice.service.model.Beer;
import ch.beershop.catalogservice.service.model.Evaluation;
import ch.beershop.catalogservice.service.model.Manufacturer;

@Repository
public interface EvaluationRepository extends CrudRepository<Evaluation, Long>, PagingAndSortingRepository<Evaluation, Long> {

	List<Evaluation> findByBeer_Fabricant_Id (Long id);
}
