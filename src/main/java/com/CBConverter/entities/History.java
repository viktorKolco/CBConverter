package com.CBConverter.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int ID;
    private double AMOUNT_RECEIVED;
    private String ORIGINAL_currency;
    private String TARGET_CURRENCY;
    private double TOTAL_AMOUNT;
    private Date DATE;

    public History(String originalCurrency, String targetCurrency, Double amountReceived, Double totalAmount, Date date) {
        this.ORIGINAL_currency = originalCurrency;
        this.TARGET_CURRENCY = targetCurrency;
        this.AMOUNT_RECEIVED = amountReceived;
        this.TOTAL_AMOUNT = totalAmount;
        this.DATE = date;
    }
}
