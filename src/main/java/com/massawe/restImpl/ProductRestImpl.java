package com.massawe.restImpl;

import com.massawe.constants.MyConstant;
import com.massawe.entity.Product;
import com.massawe.rest.ProductRest1;
import com.massawe.service.ProductRestService;
import com.massawe.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class ProductRestImpl implements ProductRest1 {
    @Autowired
    ProductRestService productRestService;
    @Override
    public ResponseEntity<String> updateAssets(Map<String, String> requestMap) {
        try {
            return productRestService.updateAssets(requestMap);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }



}