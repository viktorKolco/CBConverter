package com.CBConverter.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "currency")
public class Currency {

    @Id
    @Column(name = "cb_id", nullable = false)
    private String cbId;

    @Column(name = "num_code", nullable = false)
    private int numCode;

    @Column(name = "char_code", nullable = false)
    private String charCode;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "nominal", nullable = false)
    private int nominal;

    @Column(name = "value", nullable = false)
    private BigDecimal value;

    @Override
    public String toString() {
        return "Currency{" +
                "cbId='" + cbId + '\'' +
                ", numCode=" + numCode +
                ", charCode='" + charCode + '\'' +
                ", description='" + description + '\'' +
                ", nominal=" + nominal +
                ", value=" + value +
                '}';
    }
}
