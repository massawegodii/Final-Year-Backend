package com.massawe.entity;

import lombok.Data;

@Data
public class JwtResponse {
    private User user;
    private String jwtToken;
    private String message;

    public JwtResponse(User user, String jwtToken) {
        this.user = user;
        this.jwtToken = jwtToken;
    }

    public JwtResponse() {

    }
}
