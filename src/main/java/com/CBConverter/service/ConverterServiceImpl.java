package com.CBConverter.service;

import com.CBConverter.entities.Currency;
import com.CBConverter.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConverterServiceImpl implements ConverterService {

    private final CurrencyRepository currencyRepository;

    //fixMe: to one big Optional
    public BigDecimal convert(String originalCharCode, String targetCharCode, BigDecimal amountReceived) {
        Currency originalCurrency = null;
        log.info("Входные параметры: originalCharCode: '{}', targetCharCode: '{}', amountReceived: '{}'"
                , originalCharCode, targetCharCode, amountReceived);
        if (!originalCharCode.equals("RUB")) {
            originalCurrency = currencyRepository.findByCharCode(originalCharCode).orElseThrow();
        }
        Currency targetCurrency = null;
        if (!targetCharCode.equals("RUB")) {
            targetCurrency = currencyRepository.findByCharCode(targetCharCode).orElseThrow();
        }
        amountReceived = amountReceived.setScale(3, RoundingMode.HALF_UP);
        BigDecimal originalCurrencyInRubles;
        BigDecimal targetCurrencyConverted;
        if (originalCharCode.equals("RUB")) {
            originalCurrencyInRubles = amountReceived;
        } else {
            originalCurrencyInRubles = amountReceived
                    .multiply(originalCurrency.getValue())
                    .divide(BigDecimal.valueOf(originalCurrency.getNominal()), RoundingMode.HALF_UP);
        }
        if (!targetCharCode.equals("RUB")) {
            targetCurrencyConverted = originalCurrencyInRubles.multiply(
                    BigDecimal.valueOf(targetCurrency.getNominal()))
                    .divide(targetCurrency.getValue(), RoundingMode.HALF_UP);
        } else {
            targetCurrencyConverted = originalCurrencyInRubles.divide(new BigDecimal(1), 3, RoundingMode.HALF_UP);
        }
        log.info("Сконвертировано:  '{}' '{}'", targetCurrencyConverted, targetCharCode);
        return targetCurrencyConverted;
    }

    public String toDescription(String currency) {
        Currency inputCurrency = currencyRepository.findByCharCode(currency).orElse(null);
        return format("%s (%s)", currency, (inputCurrency == null) ? "Российский рубль"
                : inputCurrency.getDescription());
    }

}
