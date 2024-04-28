package com.massawe.restImpl;

import com.massawe.constants.MyConstant;
import com.massawe.rest.Maintenance;
import com.massawe.service.MaintenanceService;
import com.massawe.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@RestController
public class MaintenanceImpl implements Maintenance {
    @Autowired
    MaintenanceService maintenanceService;

    @Override
    public ResponseEntity<String> add(Map<String, String> requestMap) {
        try {
            return maintenanceService.add(requestMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<?> getAllMaintenance() {
        try {
            return maintenanceService.getAllMaintenance();
        } catch (Exception e) {
            e.printStackTrace();
            return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
