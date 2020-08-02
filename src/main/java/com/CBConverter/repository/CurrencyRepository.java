package com.CBConverter.repository;

import com.CBConverter.domain.Currency;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CurrencyRepository extends CrudRepository<Currency, Long> {

    @Query(value = "select c.value from currency c where c.char_code = ?1",
            nativeQuery = true)
    float findValueByChar_code(String charCode);
}
