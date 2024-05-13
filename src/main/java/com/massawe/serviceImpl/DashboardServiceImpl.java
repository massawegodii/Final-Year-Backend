package com.massawe.serviceImpl;

import com.massawe.dao.BillDao;
import com.massawe.dao.MaintenanceDao;
import com.massawe.dao.ProductDao;
import com.massawe.dao.UserDao;
import com.massawe.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    ProductDao productDao;
    @Autowired
    UserDao userDao;
    @Autowired
    BillDao billDao;

    @Autowired
    MaintenanceDao maintenanceDao;


    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        Map<String, Object> map = new HashMap<>();
        map.put("product", productDao.count());
        map.put("bill", billDao.count());
        map.put("users", userDao.count());
        map.put("maintenance", maintenanceDao.count());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
