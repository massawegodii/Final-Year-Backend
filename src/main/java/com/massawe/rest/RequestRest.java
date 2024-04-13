package com.massawe.rest;

import com.massawe.entity.RequestAsset;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/request")
@CrossOrigin(origins = "http://localhost:4200")
public interface RequestRest {
    @PostMapping("/asset")
    public ResponseEntity<String> requestAsset(@RequestBody(required = true) Map<String, String> requestMap);

    @GetMapping("/getRequestById/{id}")
    ResponseEntity<?> getRequestById(@PathVariable Long id);

    @GetMapping("/getAllRequest")
    public ResponseEntity<List<RequestAsset>> getAllRequest();

    @GetMapping("/getAllRequestByUsername")
    public List<RequestAsset> getAllRequestByUsername();

    @GetMapping("/changeStatus/{id}/{status}")
    ResponseEntity<?> changeRequestStatus(@PathVariable Long id, @PathVariable String status);
    @DeleteMapping("/deleteRequest/{id}")
    public ResponseEntity<String> deleteRequest(@PathVariable String id);
}
