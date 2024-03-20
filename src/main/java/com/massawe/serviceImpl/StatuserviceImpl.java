package com.massawe.serviceImpl;

import com.massawe.constants.MyConstant;
import com.massawe.dao.StatusDao;
import com.massawe.entity.Status;
import com.massawe.service.StatusService;
import com.massawe.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StatuserviceImpl implements StatusService {
    @Autowired
    StatusDao statusDao;

    @Override
    public ResponseEntity<String> addStatus(Map<String, String> requestMap) {
        try {
            if (requestMap.containsKey("name")) {
                String statusName = requestMap.get("name");

                // Check if the status name already exists
                if (statusDao.existsByName(statusName)) {
                    return MyUtils.getResponseEntity("Status name already exists", HttpStatus.CONFLICT);
                }

                Status newStatus = new Status();
                newStatus.setName(statusName);
                statusDao.save(newStatus);

                return MyUtils.getResponseEntity("Status added successfully", HttpStatus.OK);
            } else {
                return MyUtils.getResponseEntity("Name is required", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteStatus(String id) {
        try {
            Optional<Status> optionalStatus = statusDao.findById(Long.parseLong(id));
            if (optionalStatus.isPresent()) {
                Status status = optionalStatus.get();
                statusDao.delete(status);
                return MyUtils.getResponseEntity("Status deleted successfully", HttpStatus.OK);
            } else {
                return MyUtils.getResponseEntity("Status not found", HttpStatus.NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return MyUtils.getResponseEntity("Invalid ID format", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> viewStatusById(Long id) {
        try {
            // Call the getStatusById method to retrieve the Status entity
            Optional<Status> statusOptional = statusDao.findById(id);

            // Check if Status exists
            if (statusOptional.isPresent()) {
                Status status = statusOptional.get();
                return ResponseEntity.ok(status);
            } else {
                return MyUtils.getResponseEntity("Status not found for ID: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getAllStatus() {
        try {
            List<Status> statusList = statusDao.findAll();
            if (statusList.isEmpty()) {
                return MyUtils.getResponseEntity("No status found", HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(statusList);
        } catch (Exception e) {
            e.printStackTrace();
            return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try {
            // Check if the request contains the required fields
            if (!requestMap.containsKey("id") || !requestMap.containsKey("name")) {
                return MyUtils.getResponseEntity("ID and Name are required for updating status", HttpStatus.BAD_REQUEST);
            }

            Long id = Long.parseLong(requestMap.get("id"));
            String updatedName = requestMap.get("name");

            // Check if the status with the given ID exists
            Optional<Status> statusOptional = statusDao.findById(id);
            if (statusOptional.isEmpty()) {
                return MyUtils.getResponseEntity("Status not found for ID: " + id, HttpStatus.NOT_FOUND);
            }

            Status existingStatus = statusOptional.get();

            // Update the name of the existing status
            existingStatus.setName(updatedName);

            // Save the updated status
            statusDao.save(existingStatus);

            return MyUtils.getResponseEntity("Status updated successfully", HttpStatus.OK);
        } catch (NumberFormatException e) {
            return MyUtils.getResponseEntity("Invalid ID format", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
