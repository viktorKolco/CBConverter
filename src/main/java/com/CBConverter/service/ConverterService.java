package com.CBConverter.service;

import java.math.BigDecimal;

public interface ConverterService {

    /**
     * <p>Произвести конвертирование валют.</p>
     *
     * @param originalCurrency оригинальный курс, с которого производится конвертиирование
     * @param targetCurrency   курс, в который необходимо конвертировать
     * @param amountReceived   количество единиц для конвертации в валюте originalCurrency
     * @return количество полученных денежных единиц в валюте targetCurrency.
     **/
    BigDecimal convert(String originalCurrency, String targetCurrency, BigDecimal amountReceived);

    /**
     * <p>Перевод в необходимый для вывода формат в соответствии с ТЗ.</p>
     *
     * @param currency курс валют для преобразования
     * @return готовая строка под запись в историю конвертаций.
     **/
    String toDescription(String currency);
}
