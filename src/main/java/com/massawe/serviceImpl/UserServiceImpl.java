package com.massawe.serviceImpl;

import com.google.common.base.Strings;
import com.massawe.configuration.JwtRequestFilter;
import com.massawe.JwtService.JwtService;
import com.massawe.constants.MyConstant;
import com.massawe.dao.RoleDao;
import com.massawe.dao.UserDao;
import com.massawe.entity.Role;
import com.massawe.entity.User;
import com.massawe.service.UserService;
import com.massawe.utils.EmailUtils;
import com.massawe.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    JwtRequestFilter jwtFilter;
    @Autowired
    private EmailUtils emailUtils;
    @Autowired
    private JwtService jwtService;


    @Override
    public ResponseEntity<String> registerNewUser(Map<String, String> requestMap) {

        try {
            if (requestMap.isEmpty()) {
                return MyUtils.getResponseEntity("Request data is empty.", HttpStatus.BAD_REQUEST);
            }
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

    @Override
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            return new ResponseEntity<>(userDao.findAll(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<User>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateSignUpMap(Map<String, String> requestMap){
        if(requestMap.containsKey("userName") && requestMap.containsKey("userFirstName") && requestMap.containsKey("userLastName") && requestMap.containsKey("phoneNumber") && requestMap.containsKey("imageUrl") && requestMap.containsKey("email")
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
        user.setImageUrl(requestMap.get("imageUrl"));
        user.setUserPassword(requestMap.get("userPassword"));
        user.setPhoneNumber(requestMap.get("phoneNumber"));
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
        adminUser.setUserName("admin");
        adminUser.setUserPassword(getEncodedPassword("123"));
        adminUser.setUserFirstName("admin");
        adminUser.setUserLastName("admin");
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        adminUser.setRole(adminRoles);
        userDao.save(adminUser);

//        User user = new User();
//        user.setUserFirstName("keni");
//        user.setUserLastName("jane");
//        user.setUserName("lee");
//        user.setUserPassword(getEncodedPassword("123"));
//        Set<Role> userRoles = new HashSet<>();
//        user.setRole(userRoles);
//        userRoles.add(userRole);
//        userDao.save(user);

    }

    @Override
    public ResponseEntity<String> deleteAllUser(String userName) {
        try {
            if (userName == null || userName.isEmpty()) {
                return MyUtils.getResponseEntity("UserName cannot be empty.", HttpStatus.BAD_REQUEST);
            }

            User userToDelete = userDao.findByUserName(userName);
            if (userToDelete == null) {
                return MyUtils.getResponseEntity("User with UserName " + userName + " not found.", HttpStatus.NOT_FOUND);
            }

            // Remove the User from all associated Roles
            userToDelete.getRole().clear();

            // Now delete the User
            userDao.delete(userToDelete);

            return MyUtils.getResponseEntity("User with UserName " + userName + " deleted successfully.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> updateUsers(Map<String, String> requestMap) {
        try {
            // Validate the request
            if (!validateUserUpdateRequest(requestMap)) {
                return MyUtils.getResponseEntity(MyConstant.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }

            String userName = requestMap.get("userName");
            User existingUser = userDao.findByUserName(userName);
            if (existingUser == null) {
                return MyUtils.getResponseEntity(MyConstant.NOT_FOUND, HttpStatus.NOT_FOUND);
            }

            // Update the user fields
            if (requestMap.containsKey("email")) {
                existingUser.setEmail(requestMap.get("email"));
            }
            if (requestMap.containsKey("userFirstName")) {
                existingUser.setUserFirstName(requestMap.get("userFirstName"));
            }
            if (requestMap.containsKey("userLastName")) {
                existingUser.setUserLastName(requestMap.get("userLastName"));
            }
            if (requestMap.containsKey("userPassword")) {
                existingUser.setUserPassword(requestMap.get("userPassword"));
            }
            if (requestMap.containsKey("phoneNumber")) {
                existingUser.setPhoneNumber(requestMap.get("phoneNumber"));
            }
            if (requestMap.containsKey("imageUrl")) {
                existingUser.setImageUrl(requestMap.get("imageUrl"));
            }

            // Save the updated user
            userDao.save(existingUser);

            return MyUtils.getResponseEntity("User with UserName " + userName + " updated successfully.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateUserUpdateRequest(Map<String, String> requestMap) {
        return requestMap.containsKey("userName")
                || (requestMap.containsKey("email")
                || requestMap.containsKey("userFirstName")
                || requestMap.containsKey("userLastName")
                || requestMap.containsKey("phoneNumber")
                || requestMap.containsKey("imageUrl")
                || requestMap.containsKey("role")
                || requestMap.containsKey("userPassword"));
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

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            if (validateChangePasswordMap(requestMap)) {
                String email = requestMap.get("email");
                String newPassword = requestMap.get("newPassword");

                if (email.isEmpty() || newPassword.isEmpty()) {
                    return MyUtils.getResponseEntity("Email or password cannot be empty.", HttpStatus.BAD_REQUEST);
                }

                User user = userDao.findByEmail(email);

                if (user != null) {
                    String encodedPassword = getEncodedPassword(newPassword);
                    user.setUserPassword(encodedPassword);
                    userDao.save(user);

                    return MyUtils.getResponseEntity("Password changed successfully!", HttpStatus.OK);
                } else {
                    return MyUtils.getResponseEntity("User not found.", HttpStatus.NOT_FOUND);
                }
            } else {
                return MyUtils.getResponseEntity(MyConstant.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean validateChangePasswordMap(Map<String, String> requestMap){
        return requestMap.containsKey("email") && requestMap.containsKey("newPassword") &&
                !requestMap.get("email").isEmpty() && !requestMap.get("newPassword").isEmpty();
    }

    public String getEncodedPassword(String password){
        return passwordEncoder.encode(password);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            User user = userDao.findByEmail(requestMap.get("email"));
            if (!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail()))
                emailUtils.forgetMail(user.getEmail(), "Credential by Smart Asset Management Management System", user.getUserPassword());
            return MyUtils.getResponseEntity("Check your mail for Credentials.", HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<User> getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserName = authentication.getName();

            User user = userDao.findByUserName(currentUserName);

            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(user);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<String> blockUser(String username) {
        try {
            if (username.isEmpty()) {
                return MyUtils.getResponseEntity("Username is not found.", HttpStatus.NOT_FOUND);
            }
            jwtService.blockUser(username);
            return MyUtils.getResponseEntity("User " + username + " has been blocked. Please Contact with Administrator!", HttpStatus.OK);
        } catch (UsernameNotFoundException ex) {
            return MyUtils.getResponseEntity("Username is not found .", HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
    @Override
    public ResponseEntity<String> unblockUser(String username) {
        try {
            if (username.isEmpty()) {
                return MyUtils.getResponseEntity("Username is not Found.", HttpStatus.NOT_FOUND);
            }
            jwtService.unblockUser(username);
            return MyUtils.getResponseEntity("User " + username + " has been unblocked.", HttpStatus.OK);
        } catch (UsernameNotFoundException ex) {
            return MyUtils.getResponseEntity("Username is not found .", HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<User> getUserByUsername(String username) {
        try {
            User user = userDao.findByUserName(username);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

