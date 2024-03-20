package com.massawe.rest;

import com.massawe.entity.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RequestMapping(path = "/products")
@CrossOrigin(origins = "http://localhost:4200")
public interface ProductRest1 {
    @PostMapping(path = "/updateAssets")
    public ResponseEntity<String> updateAssets(@RequestBody(required = true) Map<String, String> requestMap);

//    @GetMapping("/user/{userName}")
//    public ResponseEntity<?> getProductByUserName(@PathVariable String userName);


}
