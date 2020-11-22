package com.CBConverter.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int ID;
    private BigDecimal AMOUNT_RECEIVED;
    private String ORIGINAL_CURRENCY;
    private String TARGET_CURRENCY;
    private BigDecimal TOTAL_AMOUNT;
    private LocalDateTime DATE;

    public History(String originalCurrency, String targetCurrency, BigDecimal amountReceived, BigDecimal totalAmount, LocalDateTime date) {
        this.ORIGINAL_CURRENCY = originalCurrency;
        this.TARGET_CURRENCY = targetCurrency;
        this.AMOUNT_RECEIVED = amountReceived;
        this.TOTAL_AMOUNT = totalAmount;
        this.DATE = date;
    }

    @Override
    public String toString() {
        return "History{" +
                "ID=" + ID +
                ", AMOUNT_RECEIVED=" + AMOUNT_RECEIVED +
                ", ORIGINAL_currency='" + ORIGINAL_CURRENCY + '\'' +
                ", TARGET_CURRENCY='" + TARGET_CURRENCY + '\'' +
                ", TOTAL_AMOUNT=" + TOTAL_AMOUNT +
                ", DATE=" + DATE +
                '}';
    }
}
