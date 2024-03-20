package com.massawe.rest;


import com.massawe.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RequestMapping(path = "/user")
@CrossOrigin(origins = "http://localhost:4200")
public interface UserRest {

    @PostMapping(path = "/registerNewUser")
    public ResponseEntity<String> registerNewUser(@RequestBody(required = true)Map<String, String> User);

    @GetMapping(path = "/getAllUsers")
    public ResponseEntity <List<User>> getAllUsers();

    @DeleteMapping("/deleteUser/{userName}")
    public ResponseEntity<String> deleteAllUser(@PathVariable String userName);

    @PostMapping(path = "/updateUser")
    public ResponseEntity<String> updateUsers(@RequestBody(required = true)Map<String, String> requestMap);

    void initialRoleAndUser();

    @GetMapping("/forAdmin")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<String> forAdmin(Map<String, String> requestMap);

    @GetMapping("/forUser")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<String> forUser(Map<String, String> requestMap);


    @PostMapping(path = "/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody(required = true)Map<String, String> requestMap);

    @PostMapping(path = "/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody(required = true)Map<String, String> requestMap);


}
