package com.massawe.serviceImpl;

import com.massawe.constants.MyConstant;
import com.massawe.dao.RoleDao;
import com.massawe.dao.UserDao;
import com.massawe.entity.Role;
import com.massawe.entity.User;
import com.massawe.service.UserService;
import com.massawe.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;


    @Override
    public ResponseEntity<String> registerNewUser(Map<String, String> requestMap) {

        try {
            if (validateSignUpMap(requestMap)) {
                User user = userDao.findByEmail(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    userDao.save(getUserFromMap(requestMap));
                    return MyUtils.getResponseEntity("Successfully Registered!", HttpStatus.OK);

                } else {
                    return MyUtils.getResponseEntity("Email Already Exist.", HttpStatus.BAD_REQUEST);
                }
            } else {
                return MyUtils.getResponseEntity(MyConstant.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            e.printStackTrace();
            return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean validateSignUpMap(Map<String, String> requestMap){
        if(requestMap.containsKey("userName") && requestMap.containsKey("userFirstName") && requestMap.containsKey("userLastName") && requestMap.containsKey("email")
                && requestMap.containsKey("userPassword")){
            return true;
        }
        return false;
    }
    private User getUserFromMap(Map<String, String> requestMap){
        User user = new User();
        user.setUserName(requestMap.get("userName"));
        user.setUserFirstName(requestMap.get("userFirstName"));
        user.setEmail(requestMap.get("email"));
        user.setUserPassword(requestMap.get("userPassword"));
        user.setUserLastName(requestMap.get("userLastName"));
        Role role = roleDao.findById("User").get();
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRole(roles);
        user.setUserPassword(getEncodedPassword(user.getUserPassword()));
        userDao.save(user);
        return user;
    }


    public void initialRoleAndUser(){
        Role adminRole = new Role();
        adminRole.setRoleName("Admin");
        adminRole.setRoleDescription("Admin Role");
        roleDao.save(adminRole);

        Role userRole = new Role();
        userRole.setRoleName("User");
        userRole.setRoleDescription("Default role for newly record");
        roleDao.save(userRole);

        User adminUser = new User();
        adminUser.setUserName("admin123");
        adminUser.setUserPassword(getEncodedPassword("123"));
        adminUser.setUserFirstName("admin");
        adminUser.setUserLastName("admin");
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        adminUser.setRole(adminRoles);
        userDao.save(adminUser);

//        User user = new User();
//        user.setUserFirstName("Kevoo");
//        user.setUserLastName("Mamkwe");
//        user.setUserName("kevoo");
//        user.setUserPassword(getEncodedPassword("123"));
//        Set<Role> userRoles = new HashSet<>();
//        user.setRole(userRoles);
//        userRoles.add(userRole);
//        userDao.save(user);

    }

    @Override
    public ResponseEntity<String> forAdmin(Map<String, String> requestMap) {
        try {
            return MyUtils.getResponseEntity("This URL is only accessible to the Admin.", HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forAUser(Map<String, String> requestMap) {
        try {
            return MyUtils.getResponseEntity("This URL is only accessible to the User.", HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public String getEncodedPassword(String password){
        return passwordEncoder.encode(password);
    }

}

