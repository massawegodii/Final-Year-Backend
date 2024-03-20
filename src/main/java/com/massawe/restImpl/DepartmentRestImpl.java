package com.massawe.restImpl;

import com.massawe.constants.MyConstant;
import com.massawe.rest.DepartmentRest;
import com.massawe.service.DepartmentService;
import com.massawe.service.StatusService;
import com.massawe.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DepartmentRestImpl implements DepartmentRest {
    @Autowired
    DepartmentService departmentService;


    @Override
    public ResponseEntity<String> addDepartment(Map<String, String> requestMap) {
        try {
            return departmentService.addDepartment(requestMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteDepartment(String id) {
        try {
            return departmentService.deleteDepartment(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    public ResponseEntity<?> getAllDepartment() {
        try {
            return departmentService.getAllDepartment();
        } catch (Exception e) {
            e.printStackTrace();
            return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> updateDepartment(Map<String, String> requestMap) {
        try {
            return departmentService.updateDepartment(requestMap);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
