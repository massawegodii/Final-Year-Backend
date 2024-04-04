package com.massawe.rest;

import com.massawe.entity.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/assign")
@CrossOrigin(origins = "http://localhost:4200")
public interface AssignRest {
    @PostMapping(path = "/assignToUser")
    public ResponseEntity<String> assignToUser(@RequestBody(required = true) Map<String, String> requestMap);

    @GetMapping({"/getAssignDetails"})
    public List<Product> getAssignDetails();

    @DeleteMapping(path = "/unAssignUser/{productId}/{userName}")
    public ResponseEntity<String> removeUserFromProduct(@PathVariable Integer productId, @PathVariable String userName);

}
