package com.massawe.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface MaintenanceService {
    ResponseEntity<String> add(Map<String, String> requestMap);

    ResponseEntity<?> getAllMaintenance();
}
