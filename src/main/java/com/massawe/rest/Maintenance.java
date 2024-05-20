package com.massawe.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping(path = "/maintenance")
@CrossOrigin(origins = "http://localhost:4200")
public interface Maintenance {
    @PostMapping(path = "/add")
    public ResponseEntity<String> add(@RequestBody(required = true) Map<String, String> requestMap);

    @GetMapping("/getAllSchedule")
    public ResponseEntity<?> getAllMaintenance();

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSchedule(@PathVariable String id);
}
