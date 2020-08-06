package com.CBConverter.service;

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
public class ConverterService {
    private final CurrencyRepository currencyRepository;

    public BigDecimal convert(String originalCurrency, String targetCurrency, BigDecimal amountReceived) {
        log.info("Конвертация из '{}' в '{}' '{}' единиц", originalCurrency, targetCurrency, amountReceived);
        amountReceived = amountReceived.setScale(3, RoundingMode.HALF_UP);
        BigDecimal originalCurrencyInRubles;
        BigDecimal targetCurrencyConverted;
        if (originalCurrency.equals("RUB")) originalCurrencyInRubles = amountReceived;
        else {
            originalCurrencyInRubles = amountReceived
                    .multiply(currencyRepository.findValueByChar_code(originalCurrency))
                    .divide(BigDecimal.valueOf(currencyRepository.findNominalByChar_Code(originalCurrency)), RoundingMode.HALF_UP);
        }
        if (!targetCurrency.equals("RUB")) {
            targetCurrencyConverted = originalCurrencyInRubles.multiply(
                    BigDecimal.valueOf(currencyRepository.findNominalByChar_Code(targetCurrency)))
                    .divide(currencyRepository.findValueByChar_code(targetCurrency), RoundingMode.HALF_UP);
        } else {
            targetCurrencyConverted = originalCurrencyInRubles.divide(new BigDecimal(1), 3, RoundingMode.HALF_UP);
        }
        log.info("Сконвертировано:  '{}' '{}'", targetCurrencyConverted, targetCurrency);
        return targetCurrencyConverted;
    }

    public String toDescription(String currency) {
        return format("%s (%s)", currency, (currencyRepository.findDescriptionByChar_Code(currency) == null) ? "Российский рубль"
                : currencyRepository.findDescriptionByChar_Code(currency));
    }
}
