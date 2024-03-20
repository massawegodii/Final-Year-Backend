package com.massawe.serviceImpl;

import com.massawe.constants.MyConstant;
import com.massawe.dao.DepartmentDao;
import com.massawe.entity.Department;
import com.massawe.service.DepartmentService;
import com.massawe.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    DepartmentDao departmentDao;

    @Override
    public ResponseEntity<String> addDepartment(Map<String, String> requestMap) {
        try {
            if (requestMap.containsKey("name")) {
                String departmentName = requestMap.get("name");

                // Check if the Department name already exists
                if (departmentDao.existsByName(departmentName)) {
                    return MyUtils.getResponseEntity("Department name already exists", HttpStatus.CONFLICT);
                }

                Department newDepartment = new Department();
                newDepartment.setName(departmentName);
                departmentDao.save(newDepartment);

                return MyUtils.getResponseEntity("Department added successfully", HttpStatus.OK);
            } else {
                return MyUtils.getResponseEntity("Name is required", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteDepartment(String id) {
        try {
            Optional<Department> optionalDepartment = departmentDao.findById(Long.parseLong(id));
            if (optionalDepartment.isPresent()) {
                Department department = optionalDepartment.get();
                departmentDao.delete(department);
                return MyUtils.getResponseEntity("Department deleted successfully", HttpStatus.OK);
            } else {
                return MyUtils.getResponseEntity("Department not found", HttpStatus.NOT_FOUND);
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
    public ResponseEntity<?> getAllDepartment() {
        try {
            List<Department> departmentsList = departmentDao.findAll();
            if (departmentsList.isEmpty()) {
                return MyUtils.getResponseEntity("No Department found", HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(departmentsList);
        } catch (Exception e) {
            e.printStackTrace();
            return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> updateDepartment(Map<String, String> requestMap) {
        try {
            // Check if the request contains the required fields
            if (!requestMap.containsKey("id") || !requestMap.containsKey("name")) {
                return MyUtils.getResponseEntity("ID and Name are required for updating department", HttpStatus.BAD_REQUEST);
            }

            Long id = Long.parseLong(requestMap.get("id"));
            String updatedName = requestMap.get("name");

            // Check if the status with the given ID exists
            Optional<Department> departmentOptional = departmentDao.findById(id);
            if (departmentOptional.isEmpty()) {
                return MyUtils.getResponseEntity("Department not found for ID: " + id, HttpStatus.NOT_FOUND);
            }

            Department existingDepartment = departmentOptional.get();

            // Update the name of the existing Department
            existingDepartment.setName(updatedName);

            // Save the updated status
            departmentDao.save(existingDepartment);

            return MyUtils.getResponseEntity("Department updated successfully", HttpStatus.OK);
        } catch (NumberFormatException e) {
            return MyUtils.getResponseEntity("Invalid ID format", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
