package com.massawe.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping(path = "/category")
@CrossOrigin(origins = "http://localhost:4200")
public interface CategoryRest {

    @PostMapping(path = "/add")
    public ResponseEntity<String> addCategory(@RequestBody(required = true) Map<String, String> requestMap);

    @DeleteMapping("/deleteCategory/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable String id);


    @GetMapping("/getAllCategory")
    public ResponseEntity<?> getAllCategory();

    @PostMapping(path = "/updateCategory")
    public ResponseEntity<String> updateCategory(@RequestBody(required = true) Map<String, String> requestMap);
}
