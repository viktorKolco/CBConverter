package com.CBConverter.repository;

import com.CBConverter.entities.History;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HistoryRepository extends CrudRepository<History, Integer> {

    Optional<History> findTopByOrderByIDDesc();

    List<History> findAllByUSERIDOrderByDATE(int userId);

    List<History> findAllByOrderByDATE();

    List<History> findAllByDATELessThanEqualAndDATEGreaterThanEqual(LocalDateTime endDate, LocalDateTime startDate);

}
