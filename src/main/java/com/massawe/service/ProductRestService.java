package com.massawe.service;
import org.springframework.http.ResponseEntity;

import java.util.Map;


public interface ProductRestService {

    ResponseEntity<String> updateAssets(Map<String, String> requestMap);


}
