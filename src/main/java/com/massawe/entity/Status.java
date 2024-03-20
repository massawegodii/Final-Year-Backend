package com.massawe.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    public Status() {
    }

    public Status( String name) {
        this.name = name;
    }
}
