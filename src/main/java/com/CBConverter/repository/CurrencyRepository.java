package com.CBConverter.repository;

import com.CBConverter.entities.Currency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends CrudRepository<Currency, Long> {

    Optional<Currency> findByCharCode(String charCode);
}
