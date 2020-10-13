package com.CBConverter.repository;

import com.CBConverter.entities.Currency;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CurrencyRepository extends CrudRepository<Currency, Long> {

    Optional<Currency> findByCharCode(String charCode);
}
