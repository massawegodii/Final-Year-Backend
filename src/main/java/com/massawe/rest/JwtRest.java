package com.massawe.rest;

import com.massawe.JwtService.JwtService;
import com.massawe.constants.MyConstant;
import com.massawe.dao.AuthLogDao;
import com.massawe.entity.UserTrack;
import com.massawe.entity.JwtRequest;
import com.massawe.entity.JwtResponse;
import com.massawe.exception.BlockedUserException;
import com.massawe.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin
public class JwtRest {

    @Autowired
    AuthLogDao authLogDao;

    @Autowired
    JwtService jwtService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createJwtToken(@RequestBody JwtRequest jwtRequest, HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        try {
            JwtResponse response = jwtService.createJwtToken(jwtRequest, ipAddress);
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

    @GetMapping("/authLogs")
    public List<UserTrack> getAllAuthLogs() {
        return authLogDao.findAll();
    }

}
