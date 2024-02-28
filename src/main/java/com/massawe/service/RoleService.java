package com.massawe.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface RoleService {
    ResponseEntity<String> createNewRole(Map<String, String> requestMap);
}
