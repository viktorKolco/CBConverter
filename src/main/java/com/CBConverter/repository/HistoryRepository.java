package com.CBConverter.repository;

import com.CBConverter.entities.History;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface HistoryRepository extends CrudRepository<History, Integer> {

    Optional<History> findTopByOrderByIDDesc();

    Iterable<History> findAllByDATE(LocalDateTime date);

    Iterable<History> findAllByUSERIDOrderByDATE(int userId);

    Iterable<History> findAllByOrderByDATE();
}
