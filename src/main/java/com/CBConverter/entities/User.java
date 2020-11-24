package com.CBConverter.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import static java.lang.String.format;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String password;
    private boolean active;

    @Column(name = "role", length = 4)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Override
    public String toString() {
        return format("id = '%s', name = '%s', active = '%s'", this.id, this.name, this.active);
    }
}
