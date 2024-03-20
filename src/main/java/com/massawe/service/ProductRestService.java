package com.massawe.service;

import com.massawe.entity.Product;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ProductRestService {

    ResponseEntity<String> updateAssets(Map<String, String> requestMap);


//    Product findProductIdByUserName(String userName);
}
