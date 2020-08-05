package com.CBConverter.repository;

import com.CBConverter.entities.History;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface HistoryRepository extends CrudRepository<History, Integer> {

    @Query(value = "select h.total_amount from history h order by h.id desc limit 1",
            nativeQuery = true)
    Double findTotalAmountWithMaxId();

    @Query(value = "select h.amount_received from history h order by h.id desc limit 1",
            nativeQuery = true)
    Double findAmountReceivedWithMaxId();

    @Query(value = "select * from history h where h.date = ?1",
            nativeQuery = true)
    Iterable<History> findAllByDate(Date date);

    @Override
    @Query(value = "select * from history h order by h.date",
            nativeQuery = true)
    Iterable<History> findAll();
}
