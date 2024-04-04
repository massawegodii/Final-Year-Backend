package com.massawe.service;

import com.massawe.entity.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface AssignService {
    ResponseEntity<String> assignToUser(Map<String, String> requestMap);

    public List<Product> getAssignDetails();

    ResponseEntity<String> removeUserFromProduct(Integer productId,  String userName);
}
