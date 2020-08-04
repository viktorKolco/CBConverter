package com.CBConverter.repository;

import com.CBConverter.domain.Currency;
import com.CBConverter.domain.History;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CurrencyRepository extends CrudRepository<Currency, Long> {

    @Query(value = "select c.value from currency c where c.char_code = ?1",
            nativeQuery = true)
    float findValueByChar_code(String charCode);

    @Query(value = "select c.nominal from currency c where c.char_code = ?1",
            nativeQuery = true)
    int findNominalByChar_Code(String charCode);

    @Query(value = "select c.description from currency c where c.char_code = ?1",
            nativeQuery = true)
    String findDescriptionByChar_Code(String charCode);

}
