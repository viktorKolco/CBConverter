package com.CBConverter.service;

import com.CBConverter.entities.Currency;

import java.util.List;

public interface ResponseService {

    /**
     * <p>Получить курсы валют.</p>
     * API http://www.cbr.ru/scripts/XML_daily.asp
     *
     * @return получение полного списка валют из ЦБ со всей необходимой информацией.
     **/
    List<Currency> getCurrenciesInfo();
}
