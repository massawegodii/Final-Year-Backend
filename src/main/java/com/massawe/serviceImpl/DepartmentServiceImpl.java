package com.massawe.serviceImpl;

import com.massawe.constants.MyConstant;
import com.massawe.dao.DepartmentDao;
import com.massawe.entity.Category;
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
    public ResponseEntity<String> addDepartment(Map<String, Object> requestMap) {
        try {
            // Retrieve and validate the department name
            String departmentName = (String) requestMap.get("name");
            if (departmentName == null || departmentName.trim().isEmpty()) {
                return MyUtils.getResponseEntity("Department cannot be empty", HttpStatus.BAD_REQUEST);
            }

            // Retrieve and validate the offices list
            Object officesObj = requestMap.get("offices");
            if (officesObj == null) {
                return MyUtils.getResponseEntity("Office cannot be null", HttpStatus.BAD_REQUEST);
            }

            if (!(officesObj instanceof List)) {
                return MyUtils.getResponseEntity("Office should be a list", HttpStatus.BAD_REQUEST);
            }

            List<?> departmentList = (List<?>) officesObj;
            if (departmentList.isEmpty()) {
                return MyUtils.getResponseEntity("Department cannot be empty", HttpStatus.BAD_REQUEST);
            }

            // Ensure all elements in the list are strings
            for (Object item : departmentList) {
                if (!(item instanceof String)) {
                    return MyUtils.getResponseEntity("All departments should be strings", HttpStatus.BAD_REQUEST);
                }
            }

            List<String> offices =  (List<String>) officesObj;

            // Check if the department name already exists
            if (departmentDao.existsByName(departmentName)) {
                return MyUtils.getResponseEntity("Department already exists", HttpStatus.BAD_REQUEST);
            }

            // Check if any of the categories already exist
            for (String office : offices) {
                if (departmentDao.existsByNameContaining(office)) {
                    return MyUtils.getResponseEntity("Department " + office + " already exists", HttpStatus.BAD_REQUEST);
                }
            }

            // Create and save the new office
            Department newDepartment = new Department();
            newDepartment.setName(departmentName);
            newDepartment.setOffices(offices);
            departmentDao.save(newDepartment);

            return MyUtils.getResponseEntity("Department added successfully", HttpStatus.OK);
        } catch (ClassCastException e) {
            return MyUtils.getResponseEntity("Invalid data format: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
    public ResponseEntity<String> updateDepartment(Map<String, Object> requestMap) {
        try {
            // Check if the request contains the required fields
            if (!requestMap.containsKey("id") || !requestMap.containsKey("name")) {
                return MyUtils.getResponseEntity("Missing field required for updating Department", HttpStatus.BAD_REQUEST);
            }

            Long id = Long.parseLong((String) requestMap.get("id"));
            String updatedName = (String) requestMap.get("name");

            // Check if the Category with the given ID exists
            Optional<Department> departmentOptional = departmentDao.findById(id);
            if (departmentOptional.isEmpty()) {
                return MyUtils.getResponseEntity("Department not found for ID: " + id, HttpStatus.NOT_FOUND);
            }

            Department existingDepartment = departmentOptional.get();

            // Update the name of the existing Category
            existingDepartment.setName(updatedName);

            // Save the updated Category
            departmentDao.save(existingDepartment);

            return MyUtils.getResponseEntity("Department updated successfully", HttpStatus.OK);
        } catch (NumberFormatException e) {
            return MyUtils.getResponseEntity("Invalid ID format", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<?> getDepartmentByName(String name) {
        try {
            List<Department> departments = departmentDao.getDepartmentByName(name);
            if (departments.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No departments found with the provided name");
            }
            return ResponseEntity.ok(departments);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while fetching categories");
        }
    }
}
