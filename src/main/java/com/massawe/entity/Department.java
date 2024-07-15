package com.massawe.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public Department(){

    }
    @ElementCollection
    private List<String> offices;

    public Department(String name) {
        this.name = name;
    }
}
