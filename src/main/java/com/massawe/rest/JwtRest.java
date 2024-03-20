package com.massawe.rest;

import com.massawe.JwtService.JwtService;
import com.massawe.constants.MyConstant;
import com.massawe.entity.JwtRequest;
import com.massawe.entity.JwtResponse;
import com.massawe.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class JwtRest {

    @Autowired
    JwtService jwtService;

    @PostMapping({"/authenticate"})
    public JwtResponse createJwtToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        JwtResponse response = jwtService.createJwtToken(jwtRequest);

        // Modify the response object to include a message
        response.setMessage("Authentication successful");

        return response;
    }
}
