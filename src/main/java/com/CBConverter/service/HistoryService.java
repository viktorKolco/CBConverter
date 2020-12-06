package com.CBConverter.service;

import com.CBConverter.entities.History;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public interface HistoryService {

    /**
     * <p>Получить последнюю конвертацию из БД.</p>
     * @return последняя конвертация.
     **/
    History getLastConvert();

    /**
     * <p>Добавить в историю новую конвертацию.</p>
     * @param originalCurrency оригинальный курс, с которого производится конвертиирование
     * @param targetCurrency   курс, в который необходимо конвертировать
     * @param amountReceived   количество единиц для конвертации в валюте originalCurrency
     **/
    void addHistory(String originalCurrency, String targetCurrency, BigDecimal amountReceived);

    /**
     * <p>Получить несколько конвертаций по определенной дате.</p>
     * @param date дата, на которую необзодимо получить конвертации
     * @return конвертации
     **/
    Iterable<History> getByDate(LocalDateTime date);

    /**
     * <p>Удаляет все конвертации из БД.</p>
     * Доступно только для 'ADMIN'
     **/
    void deleteAll();

    /**
     * <p>Удаляет конвертацию по конкретному идентитфикатору.</p>
     * Доступно только для 'ADMIN'
     * @param id идентификатор для удаления
     **/
    void deleteById(int id);

    /**
     * <p>Получить все конвертации по конкретному пользователю.</p>
     * @return все конвертации пользователя
     **/
    Iterable<History> findAllByCurrentUser();
}
