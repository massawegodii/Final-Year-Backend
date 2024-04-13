package com.massawe.service;

import com.massawe.entity.RequestAsset;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

public interface RequestService {
    ResponseEntity<String> requestAsset(Map<String, String> requestMap);

    ResponseEntity<?> getRequestById(@PathVariable Long id);

    ResponseEntity<List<RequestAsset>> getAllRequest();

    ResponseEntity<?> changeRequestStatus(Long id, String status);

    List<RequestAsset> getAllRequestByUsername();

    ResponseEntity<String> deleteRequest(String id);
}
