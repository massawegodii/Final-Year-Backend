package com.massawe.restImpl;

import com.massawe.constants.MyConstant;
import com.massawe.rest.RoleRest;
import com.massawe.service.RoleService;
import com.massawe.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@RestController
public class RoleRestImpl implements RoleRest {

    @Autowired
    private RoleService roleService;


    @Override
    public ResponseEntity<String> createNewRole(Map<String, String> requestMap) {
        try {
            return roleService.createNewRole(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
