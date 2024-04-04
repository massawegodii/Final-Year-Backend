package com.massawe.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
public class User {
    @Id
    private String userName;
    @Column(name = "email")
    private String email;
    private String userFirstName;
    private String userLastName;
    @Column(name = "userPassword")
    private String userPassword;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(name = "USER_ROLE",
            joinColumns = {
                    @JoinColumn(name = "USER_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "ROLE_ID")
            }
    )
    private Set<Role> role;


    public User() {

    }

}
