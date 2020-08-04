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

    public double convert(String originalCurrent, String targetCurrent, double amountReceived) {
        BigDecimal bigDecimal = new BigDecimal(Double.toString(amountReceived));
        bigDecimal = bigDecimal.setScale(3, RoundingMode.HALF_UP);
        amountReceived = bigDecimal.doubleValue();
        double originalCurrentInRubles;
        if (originalCurrent.equals("RUB")) originalCurrentInRubles = amountReceived;
        else {
            originalCurrentInRubles = amountReceived * currencyRepository.findValueByChar_code(originalCurrent) / currencyRepository.findNominalByChar_Code(originalCurrent);
        }
        double targetCurrentInRubles;
        if (targetCurrent.equals("RUB")) targetCurrentInRubles = 1;
        else
            targetCurrentInRubles = currencyRepository.findValueByChar_code(targetCurrent) * currencyRepository.findNominalByChar_Code(targetCurrent);
        double totalAmount = originalCurrentInRubles / targetCurrentInRubles;
        bigDecimal = new BigDecimal(Double.toString(totalAmount));
        bigDecimal = bigDecimal.setScale(3, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    public String toDescription(String currency){
        return format("%s (%s)", currency, (currencyRepository.findDescriptionByChar_Code(currency) == null) ? "Российский рубль"
                : currencyRepository.findDescriptionByChar_Code(currency));
    }
}
