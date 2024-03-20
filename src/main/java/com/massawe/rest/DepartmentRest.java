package com.massawe.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping(path = "/department")
@CrossOrigin(origins = "http://localhost:4200")
public interface DepartmentRest {
    @PostMapping(path = "/add")
    public ResponseEntity<String> addDepartment(@RequestBody(required = true) Map<String, String> requestMap);

    @DeleteMapping("/deleteDepartment/{id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable String id);


    @GetMapping("/getAllDepartment")
    public ResponseEntity<?> getAllDepartment();

    @PostMapping(path = "/updateDepartment")
    public ResponseEntity<String> updateDepartment(@RequestBody(required = true) Map<String, String> requestMap);
}
