package com.massawe.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping(path = "/dashboard")
@CrossOrigin(origins = "http://localhost:4200")
public interface DashboardRest {
    @GetMapping(path = "/details")
    ResponseEntity<Map<String, Object>> getCount();
}
