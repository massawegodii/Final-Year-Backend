package com.massawe.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping(path = "/status")
@CrossOrigin(origins = "http://localhost:4200")
public interface StatusRest {
    @PostMapping(path = "/add")
    public ResponseEntity<String> addStatus(@RequestBody(required = true)Map<String, String> requestMap);

    @DeleteMapping("/deleteStatus/{id}")
    public ResponseEntity<String> deleteStatus(@PathVariable String id);

    @GetMapping("/viewStatusById/{id}")
    ResponseEntity<?> viewStatusById(@PathVariable Long id);

    @GetMapping("/getAllStatus")
    public ResponseEntity<?> getAllStatus();

    @PostMapping(path = "/updateStatus")
    public ResponseEntity<String> updateStatus(@RequestBody(required = true) Map<String, String> requestMap);
}
