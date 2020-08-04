package com.CBConverter.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "currency")
public class Currency {
    @Id
    private String id;
    private int num_code;
    private String char_code;
    private String description;
    private int nominal;
    private double value;

    public Currency() {
    }

    public Currency(String id, int numCode, String charCode, int nominal, double value, String description) {
        this.id = id;
        this.num_code = numCode;
        this.char_code = charCode;
        this.description = description;
        this.nominal = nominal;
        this.value = value;
    }
}
