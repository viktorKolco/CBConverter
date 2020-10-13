package com.CBConverter.repository;

import com.CBConverter.entities.History;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.Optional;

public interface HistoryRepository extends CrudRepository<History, Integer> {

    Optional<History> findTopByOrderByIDDesc();

    Iterable<History> findAllByDATE(Date date);

    Iterable<History> findAllByOrderByDATE();
}
