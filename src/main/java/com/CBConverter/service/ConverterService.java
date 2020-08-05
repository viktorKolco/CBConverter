package com.CBConverter.service;

import com.CBConverter.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class ConverterService {
    private final CurrencyRepository currencyRepository;

    public BigDecimal convert(String originalCurrency, String targetCurrency, BigDecimal amountReceived) {
        amountReceived = amountReceived.setScale(3, RoundingMode.HALF_UP);
        BigDecimal originalCurrencyInRubles;
        if (originalCurrency.equals("RUB")) originalCurrencyInRubles = amountReceived;
        else {
            originalCurrencyInRubles = amountReceived
                    .multiply(currencyRepository.findValueByChar_code(originalCurrency))
                    .divide(BigDecimal.valueOf(currencyRepository.findNominalByChar_Code(originalCurrency)), RoundingMode.HALF_UP);
        }
        BigDecimal targetCurrencyInRubles;
        if (targetCurrency.equals("RUB")) targetCurrencyInRubles = new BigDecimal(1);
        else
            targetCurrencyInRubles = currencyRepository.findValueByChar_code(targetCurrency)
                    .multiply(BigDecimal.valueOf(currencyRepository.findNominalByChar_Code(targetCurrency)));
        return originalCurrencyInRubles.divide(targetCurrencyInRubles, 3, RoundingMode.HALF_UP);
    }

    public String toDescription(String currency) {
        return format("%s (%s)", currency, (currencyRepository.findDescriptionByChar_Code(currency) == null) ? "Российский рубль"
                : currencyRepository.findDescriptionByChar_Code(currency));
    }
}
