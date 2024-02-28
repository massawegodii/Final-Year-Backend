package com.massawe.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
public class Role {
    @Id
    private String roleName;
    private String roleDescription;
    public Role() {
    }

}
