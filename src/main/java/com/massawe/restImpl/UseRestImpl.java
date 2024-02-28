package com.massawe.restImpl;

import com.massawe.constants.MyConstant;
import com.massawe.rest.UserRest;
import com.massawe.service.UserService;
import com.massawe.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Map;

@RestController
public class UseRestImpl implements UserRest {
    @Autowired
    private UserService userService;

    @Override
    public ResponseEntity<String> registerNewUser(Map<String, String> requestMap) {
        try {
            return userService.registerNewUser(requestMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @PostConstruct
    public void initialRoleAndUser() {
        userService.initialRoleAndUser();
    }

    @Override
    public ResponseEntity<String> forAdmin(Map<String, String> requestMap) {
        return userService.forAdmin(requestMap);
    }

    @Override
    public ResponseEntity<String> forUser(Map<String, String> requestMap) {
        return userService.forAUser(requestMap);
    }


}
