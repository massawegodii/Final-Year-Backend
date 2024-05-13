package com.massawe.rest;

import com.massawe.JwtService.JwtService;
import com.massawe.constants.MyConstant;
import com.massawe.entity.JwtRequest;
import com.massawe.entity.JwtResponse;
import com.massawe.entity.User;
import com.massawe.exception.BlockedUserException;
import com.massawe.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class JwtRest {

    @Autowired
    JwtService jwtService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createJwtToken(@RequestBody JwtRequest jwtRequest) {
        try {
            JwtResponse response = jwtService.createJwtToken(jwtRequest);
            // Modify the response object to include a message
            response.setMessage("Authentication successful");
            return ResponseEntity.ok(response);
        } catch (BlockedUserException ex) {
            return MyUtils.getResponseEntity("User is blocked. Please contact with administrator!.", HttpStatus.OK);
        } catch (Exception e) {
            // Handle other exceptions
            return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
