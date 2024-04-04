package com.massawe.restImpl;

import com.massawe.constants.MyConstant;
import com.massawe.entity.Product;
import com.massawe.rest.AssignRest;
import com.massawe.service.AssignService;
import com.massawe.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
@RestController
public class AssignRestImpl implements AssignRest {
    @Autowired
    AssignService assignService;
    @Override
    public ResponseEntity<String> assignToUser(Map<String, String> requestMap) {

        try {
            return assignService.assignToUser(requestMap);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public List<Product> getAssignDetails() {
        return assignService.getAssignDetails();
    }

    @Override
    public ResponseEntity<String> removeUserFromProduct(Integer productId, String userName) {
        return assignService.removeUserFromProduct(productId, userName);
    }


}
