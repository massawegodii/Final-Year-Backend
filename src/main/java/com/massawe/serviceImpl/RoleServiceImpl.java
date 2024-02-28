package com.massawe.serviceImpl;

import com.massawe.constants.MyConstant;
import com.massawe.dao.RoleDao;
import com.massawe.entity.Role;
import com.massawe.service.RoleService;
import com.massawe.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public ResponseEntity<String> createNewRole(Map<String, String> requestMap) {
        try {
            String roleName = requestMap.get("roleName");
            String roleDescription = requestMap.get("roleDescription");

            if (roleName == null || roleName.isEmpty() || roleDescription == null || roleDescription.isEmpty()) {
                return MyUtils.getResponseEntity("RoleName and RoleDescription are required.", HttpStatus.BAD_REQUEST);
            }

            Role role = new Role(roleName, roleDescription);
            roleDao.save(role);

            return MyUtils.getResponseEntity("Role created successfully.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
