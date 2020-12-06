package com.CBConverter.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
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
    private Integer ID;

    @Column(name = "amount_received", nullable = false)
    private BigDecimal AMOUNT_RECEIVED;

    @Column(name = "original_currency", nullable = false)
    private String ORIGINAL_CURRENCY;

    @Column(name = "target_currency", nullable = false)
    private String TARGET_CURRENCY;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal TOTAL_AMOUNT;

    @Column(name = "date", nullable = false)
    private LocalDateTime DATE;

    @Column(name = "user_id", nullable = false)
    private Integer USERID;

    public History(String originalCurrency, String targetCurrency, BigDecimal amountReceived, BigDecimal totalAmount, LocalDateTime date, Integer userId) {
        this.ORIGINAL_CURRENCY = originalCurrency;
        this.TARGET_CURRENCY = targetCurrency;
        this.AMOUNT_RECEIVED = amountReceived;
        this.TOTAL_AMOUNT = totalAmount;
        this.DATE = date;
        this.USERID = userId;
    }

    @Override
    public String toString() {
        return "History{" +
                "ID=" + ID +
                ", AMOUNT_RECEIVED=" + AMOUNT_RECEIVED +
                ", ORIGINAL_CURRENCY='" + ORIGINAL_CURRENCY + '\'' +
                ", TARGET_CURRENCY='" + TARGET_CURRENCY + '\'' +
                ", TOTAL_AMOUNT=" + TOTAL_AMOUNT +
                ", DATE=" + DATE +
                ", USER_ID=" + USERID +
                '}';
    }
}
