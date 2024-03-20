package com.massawe.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

public interface StatusService {

    ResponseEntity<String> addStatus(Map<String, String> requestMap);

    ResponseEntity<String> deleteStatus(@PathVariable String id);

    ResponseEntity<?> viewStatusById(@PathVariable Long id);

    ResponseEntity<?> getAllStatus();

    ResponseEntity<String> updateStatus(Map<String, String> requestMap);

}
