package com.massawe.service;


import com.massawe.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

public interface UserService {
    ResponseEntity<String> registerNewUser(Map<String, String> requestMap);

    ResponseEntity<List<User>> getAllUsers();

    void initialRoleAndUser();

    ResponseEntity<String> deleteAllUser(@PathVariable String userName);

    ResponseEntity<String> updateUsers(Map<String, String> requestMap);

    ResponseEntity<String> forAdmin(Map<String, String> requestMap);

    ResponseEntity<String> forAUser(Map<String, String> requestMap);

    ResponseEntity<String> changePassword(Map<String, String> requestMap);

    ResponseEntity<String> forgotPassword (Map<String, String> requestMap);

    ResponseEntity<?> lockUserAccount(String username);
}
