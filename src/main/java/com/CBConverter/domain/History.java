package com.CBConverter.domain;

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

    private int AMOUNT_RECEIVED;
    private String ORIGINAL_CURRENT;
    private String TARGET_CURRENCY;
    private double TOTAL_AMOUNT;
//    private Date DATE;



    public History(){}

    public History(String original_current, String target_current, int amount_received, double totalAmount){
        this.ORIGINAL_CURRENT = original_current;
        this.TARGET_CURRENCY = target_current;
        this.AMOUNT_RECEIVED = amount_received;
        this.TOTAL_AMOUNT = totalAmount;
    }
}
