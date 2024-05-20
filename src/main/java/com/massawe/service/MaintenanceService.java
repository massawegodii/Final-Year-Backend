package com.massawe.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

public interface MaintenanceService {
    ResponseEntity<String> add(Map<String, String> requestMap);
    ResponseEntity<?> getAllMaintenance();

    public ResponseEntity<String> deleteSchedule(@PathVariable String id);
}
