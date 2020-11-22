package com.CBConverter.service;

import com.CBConverter.entities.Currency;
import com.CBConverter.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConverterServiceImpl implements ConverterService {

    private final CurrencyRepository currencyRepository;

    public BigDecimal convert(String originalCharCode, String targetCharCode, BigDecimal amountReceived) {
        BigDecimal originalCurrencyInRubles = currencyRepository.findByCharCode(originalCharCode)
                .map(originalCurrency -> amountReceived
                        .multiply(originalCurrency.getValue())
                        .divide(BigDecimal.valueOf(originalCurrency.getNominal()), RoundingMode.HALF_UP))
                .or(() -> {
                    if ("RUB".equals(originalCharCode)) {
                        return Optional.of(amountReceived);
                    } else return Optional.empty();
                })
                .orElseThrow();
        return currencyRepository.findByCharCode(targetCharCode)
                .map(targetCurrency ->
                        originalCurrencyInRubles.multiply(
                                BigDecimal.valueOf(targetCurrency.getNominal()))
                                .divide(targetCurrency.getValue(), 3, RoundingMode.HALF_UP))
                .or(() -> {
                    if ("RUB".equals(targetCharCode)) {
                        return Optional.of(originalCurrencyInRubles.divide(new BigDecimal(1), 3, RoundingMode.HALF_UP));
                    } else return Optional.empty();
                })
                .orElseThrow();
    }


    public String toDescription(String currency) {
        Currency inputCurrency = currencyRepository.findByCharCode(currency).orElse(null);
        return format("%s (%s)", currency, (inputCurrency == null) ? "Российский рубль"
                : inputCurrency.getDescription());
    }
}
