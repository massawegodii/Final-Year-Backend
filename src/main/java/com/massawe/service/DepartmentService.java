package com.massawe.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

public interface DepartmentService {
    ResponseEntity<String> addDepartment(Map<String, String> requestMap);

    ResponseEntity<String> deleteDepartment(@PathVariable String id);


    ResponseEntity<?> getAllDepartment();

    ResponseEntity<String> updateDepartment(Map<String, String> requestMap);
}
