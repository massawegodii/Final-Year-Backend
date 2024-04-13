package com.massawe.restImpl;

import com.massawe.constants.MyConstant;
import com.massawe.entity.RequestAsset;
import com.massawe.entity.User;
import com.massawe.rest.RequestRest;
import com.massawe.service.RequestService;
import com.massawe.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@RestController
public class RequestRestImpl implements RequestRest {
    @Autowired
    RequestService requestService;
    @Override
    public ResponseEntity<String> requestAsset(Map<String, String> requestMap) {
        try {
            return requestService.requestAsset(requestMap);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<?> getRequestById(Long id) {
        try {
            return requestService.getRequestById(id);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<RequestAsset>> getAllRequest() {
        try {
            return requestService.getAllRequest();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<List<RequestAsset>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<RequestAsset> getAllRequestByUsername() {
        return requestService.getAllRequestByUsername();
    }

    @Override
    public ResponseEntity<?> changeRequestStatus(Long id, String status) {
        try {
            return requestService.changeRequestStatus(id, status);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteRequest(String id) {
        try {
            return requestService.deleteRequest(id);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
