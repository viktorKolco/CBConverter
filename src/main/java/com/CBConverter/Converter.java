package com.CBConverter;

import com.CBConverter.repository.CurrencyRepository;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.lang.String.format;

@Getter
@Setter
public class Converter {
    private CurrencyRepository currencyRepository;
    private String original_current;
    private String target_current;
    private double amount_received;
    private double totalAmount;
    private String description;

    private Converter() {
    }

    public Converter(CurrencyRepository currencyRepository, String original_current, String target_current, double amount_received) {
        this.currencyRepository = currencyRepository;
        //todo:Поправить
        this.original_current = format("%s (%s)", original_current, (currencyRepository.findDescriptionByChar_Code(original_current) == null) ? "Российский рубль"
                : currencyRepository.findDescriptionByChar_Code(original_current));
        this.target_current = format("%s (%s)", target_current, (currencyRepository.findDescriptionByChar_Code(target_current) == null) ? "Российский рубль"
                : currencyRepository.findDescriptionByChar_Code(target_current));
        BigDecimal bd = new BigDecimal(Double.toString(amount_received));
        bd = bd.setScale(3, RoundingMode.HALF_UP);
        this.amount_received = bd.doubleValue();
        this.totalAmount = convert(currencyRepository, original_current, target_current, this.amount_received);
    }

    private double convert(CurrencyRepository currencyRepository, String original_current, String target_current, double amount_received) {
        double originalCurrentInRubles;
        if (original_current.equals("RUB")) originalCurrentInRubles = amount_received;
        else {
            originalCurrentInRubles = amount_received * currencyRepository.findValueByChar_code(original_current) / currencyRepository.findNominalByChar_Code(original_current);
        }
        double targetCurrentInRubles;
        if (target_current.equals("RUB")) targetCurrentInRubles = 1;
        else
            targetCurrentInRubles = currencyRepository.findValueByChar_code(target_current) * currencyRepository.findNominalByChar_Code(target_current);
        double totalAmount = originalCurrentInRubles / targetCurrentInRubles;
        BigDecimal bd = new BigDecimal(Double.toString(totalAmount));
        bd = bd.setScale(3, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}
