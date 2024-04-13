package com.massawe.serviceImpl;


import com.massawe.Configuration.JwtRequestFilter;
import com.massawe.constants.MyConstant;
import com.massawe.dao.RequestAssetDao;
import com.massawe.dao.UserDao;
import com.massawe.entity.Department;
import com.massawe.entity.RequestAsset;
import com.massawe.entity.User;
import com.massawe.enums.RequestAssetStatus;
import com.massawe.service.RequestService;
import com.massawe.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class RequestRestServiceImpl implements RequestService {
    @Autowired
    RequestAssetDao requestAssetDao;

    @Autowired
    UserDao userDao;

    @Override
    public ResponseEntity<String> requestAsset(Map<String, String> requestMap) {

        if (requestMap == null || requestMap.isEmpty()) {
            return MyUtils.getResponseEntity("Request map is empty", HttpStatus.BAD_REQUEST);
        }

        try {
            // Get the current user from JwtRequestFilter
            String currentUser = JwtRequestFilter.CURRENT_USER;

            // Create a new RequestAsset instance
            RequestAsset newRequestAsset = new RequestAsset();

            // Populate fields from requestMap
            newRequestAsset.setName(requestMap.get("name"));
            newRequestAsset.setDepartment(requestMap.get("department"));
            newRequestAsset.setNumberOfAsset(Integer.parseInt(requestMap.get("numberOfAsset")));
            newRequestAsset.setDate(new Date());

            // Parse and set startDate and endDate
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

            if (requestMap.containsKey("startDate") && requestMap.get("startDate") != null) {
                Date startDate = dateFormat.parse(requestMap.get("startDate"));
                newRequestAsset.setStartDate(startDate);
            }

            if (requestMap.containsKey("endDate") && requestMap.get("endDate") != null) {
                Date endDate = dateFormat.parse(requestMap.get("endDate"));
                newRequestAsset.setEndDate(endDate);
            }
            // Set default status to PENDING
            newRequestAsset.setStatusAsset(RequestAssetStatus.PENDING);

            // Set the user for the request
            User user = userDao.findById(currentUser).orElse(null);
            if (user != null) {
                newRequestAsset.setUser(user);
            } else {
                return MyUtils.getResponseEntity("User not found", HttpStatus.NOT_FOUND);
            }

            // Save the RequestAsset
            RequestAsset savedRequestAsset = requestAssetDao.save(newRequestAsset);

            if (savedRequestAsset != null) {
                return MyUtils.getResponseEntity("Request Sent successfully ", HttpStatus.OK);
            } else {
                return MyUtils.getResponseEntity("Failed to create RequestAsset", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (NumberFormatException e) {
            return MyUtils.getResponseEntity("Invalid format for numberOfAsset or days", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public ResponseEntity<?> getRequestById(Long id) {
        try {
            // Find the RequestAsset by ID
            Optional<RequestAsset> optionalRequestAsset = requestAssetDao.findById(id);

            if (optionalRequestAsset.isPresent()) {
                RequestAsset requestAsset = optionalRequestAsset.get();
                // You can construct the response message as needed
                return MyUtils.getResponseEntity("RequestAsset found with ID " + requestAsset, HttpStatus.OK);
            } else {
                return MyUtils.getResponseEntity("Request not found with ID: " + id, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<RequestAsset>> getAllRequest() {
        try {
            return new ResponseEntity<List<RequestAsset>>((List<RequestAsset>) requestAssetDao.findAll(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<RequestAsset>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<?> changeRequestStatus(Long id, String status) {
        try {
            Optional<RequestAsset> assetOptional = requestAssetDao.findById(id);
            if (assetOptional.isPresent()) {
                RequestAsset existingAsset = assetOptional.get();
                if (status.equalsIgnoreCase("Approved")) {
                    existingAsset.setStatusAsset(RequestAssetStatus.APPROVED);
                } else if (status.equalsIgnoreCase("Rejected")) {
                    existingAsset.setStatusAsset(RequestAssetStatus.REJECTED);
                }
                requestAssetDao.save(existingAsset);
                return MyUtils.getResponseEntity("Request " + status + " Successfully", HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public List<RequestAsset> getAllRequestByUsername() {
        String username = JwtRequestFilter.CURRENT_USER;
        User user = userDao.findById(username).get();
        return requestAssetDao.findByUser(user);
    }

    @Override
    public ResponseEntity<String> deleteRequest(String id) {
        try {
            Optional<RequestAsset> optionalRequest = requestAssetDao.findById(Long.parseLong(id));
            if (optionalRequest.isPresent()) {
                RequestAsset requestAsset = optionalRequest.get();
                requestAssetDao.delete(requestAsset);
                return MyUtils.getResponseEntity("Request deleted successfully", HttpStatus.OK);
            } else {
                return MyUtils.getResponseEntity("Request not found", HttpStatus.NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return MyUtils.getResponseEntity("Invalid ID format", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
