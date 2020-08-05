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

    public double convert(String originalCurrency, String targetCurrency, double amountReceived) {
        BigDecimal bigDecimal = new BigDecimal(Double.toString(amountReceived));
        bigDecimal = bigDecimal.setScale(3, RoundingMode.HALF_UP);
        amountReceived = bigDecimal.doubleValue();
        double originalCurrencyInRubles;
        if (originalCurrency.equals("RUB")) originalCurrencyInRubles = amountReceived;
        else {
            originalCurrencyInRubles = amountReceived * currencyRepository.findValueByChar_code(originalCurrency) / currencyRepository.findNominalByChar_Code(originalCurrency);
        }
        double targetCurrencyInRubles;
        if (targetCurrency.equals("RUB")) targetCurrencyInRubles = 1;
        else
            targetCurrencyInRubles = currencyRepository.findValueByChar_code(targetCurrency) * currencyRepository.findNominalByChar_Code(targetCurrency);
        double totalAmount = originalCurrencyInRubles / targetCurrencyInRubles;
        bigDecimal = new BigDecimal(Double.toString(totalAmount));
        bigDecimal = bigDecimal.setScale(3, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    public String toDescription(String currency){
        return format("%s (%s)", currency, (currencyRepository.findDescriptionByChar_Code(currency) == null) ? "Российский рубль"
                : currencyRepository.findDescriptionByChar_Code(currency));
    }
}
