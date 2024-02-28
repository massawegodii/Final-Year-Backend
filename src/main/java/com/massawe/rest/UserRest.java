package com.massawe.rest;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping(path = "/user")
public interface UserRest {
    @PostMapping(path = "/registerNewUser")
    public ResponseEntity<String> registerNewUser(@RequestBody(required = true)Map<String, String> User);
    void initialRoleAndUser();

    @GetMapping("/forAdmin")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> forAdmin(Map<String, String> requestMap);

    @GetMapping("/forUser")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> forUser(Map<String, String> requestMap);
}
