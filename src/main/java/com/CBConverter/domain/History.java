package com.CBConverter.domain;

import com.CBConverter.Converter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int ID;
    private double AMOUNT_RECEIVED;
    private String ORIGINAL_CURRENT;
    private String TARGET_CURRENCY;
    private double TOTAL_AMOUNT;

    public History(){}

    public History(Converter converter){
        this.ORIGINAL_CURRENT = converter.getOriginal_current();
        this.TARGET_CURRENCY = converter.getTarget_current();
        this.AMOUNT_RECEIVED = converter.getAmount_received();
        this.TOTAL_AMOUNT = converter.getTotalAmount();
    }
}
