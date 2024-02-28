package com.massawe.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class MyUtils {

    private MyUtils() {

    }
    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus) {

        return new ResponseEntity<String>("{\"message\":\"" + responseMessage + "\"}", httpStatus);
    }


}
