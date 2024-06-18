package com.massawe.service;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.util.Map;


public interface ProductRestService {

    ResponseEntity<String> updateAssets(Map<String, String> requestMap);

    byte[] generatePdf();
}
