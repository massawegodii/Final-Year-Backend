package com.massawe.service;


import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface UserService {
    ResponseEntity<String> registerNewUser(Map<String, String> requestMap);
    void initialRoleAndUser();
    ResponseEntity<String> forAdmin(Map<String, String> requestMap);
    ResponseEntity<String> forAUser(Map<String, String> requestMap);
}
