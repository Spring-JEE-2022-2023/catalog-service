package ch.beershop.catalogservice.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import ch.beershop.catalogservice.service.model.Beer;
import ch.beershop.catalogservice.service.model.Cart;
import ch.beershop.catalogservice.service.model.Manufacturer;

@Repository
public interface CartRepository extends CrudRepository<Cart, Long>, PagingAndSortingRepository<Cart, Long> {

}
