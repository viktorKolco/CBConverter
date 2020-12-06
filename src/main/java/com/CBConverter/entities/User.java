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
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "activation_code")
    private String activationCode;

    @Column(name = "role", length = 5)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Override
    public String toString() {
        return format("id = '%s', name = '%s', active = '%s'", this.id, this.name, this.active);
    }
}
