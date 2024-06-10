package com.massawe.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

public interface CategoryService {
    ResponseEntity<String> addCategory(Map<String, Object> requestMap);

    ResponseEntity<String> deleteCategory(@PathVariable String id);

    ResponseEntity<?> getAllCategory();

    ResponseEntity<String> updateCategory(Map<String, Object> requestMap);

    ResponseEntity<?> getCategoriesByName(String name);
}
