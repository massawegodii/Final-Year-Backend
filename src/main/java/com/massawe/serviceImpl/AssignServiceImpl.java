package com.massawe.serviceImpl;

import com.massawe.configuration.JwtRequestFilter;
import com.massawe.constants.MyConstant;
import com.massawe.dao.ProductDao;
import com.massawe.dao.UserDao;
import com.massawe.entity.Product;
import com.massawe.entity.User;
import com.massawe.service.AssignService;
import com.massawe.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
@Service
public class AssignServiceImpl implements AssignService {
    @Autowired
    ProductDao productDao;

    @Autowired
    UserDao userDao;

    @Override
    public ResponseEntity<String> assignToUser(Map<String, String> requestMap) {
        try {
            // Check if the request map contains productId and userName
            if (!requestMap.containsKey("productId") || !requestMap.containsKey("userName")) {
                return MyUtils.getResponseEntity("AssetId and Username are required for assigning assets.", HttpStatus.BAD_REQUEST);
            }

            Integer productId = Integer.parseInt(requestMap.get("productId"));
            String userName = requestMap.get("userName");

            // Retrieve the Product entity from the database
            Optional<Product> optionalProduct = productDao.findById(productId);

            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();

                // Retrieve the User entity by userName
                User user = userDao.findByUserName(userName);

                if (user != null) {
                    // Update the fields if they exist in the requestMap
                    if (requestMap.containsKey("productName")) {
                        product.setProductName(requestMap.get("productName"));
                    }
                    if (requestMap.containsKey("productDescription")) {
                        product.setProductDescription(requestMap.get("productDescription"));
                    }
                    if (requestMap.containsKey("productPrice")) {
                        product.setProductPrice(Double.parseDouble(requestMap.get("productPrice")));
                    }
                    if (requestMap.containsKey("productModel")) {
                        product.setProductModel(requestMap.get("productModel"));
                    }
                    if (requestMap.containsKey("productSerialNo")) {
                        product.setProductSerialNo(Integer.parseInt(requestMap.get("productSerialNo")));
                    }

                    // Assign the product to the user
                    product.setUser(user);

                    // Save the updated Product entity
                    productDao.save(product);

                    return MyUtils.getResponseEntity("Asset with ID " + productId + " assigned to User with username " + userName + " successfully.", HttpStatus.OK);
                } else {
                    return MyUtils.getResponseEntity("User with username " + userName + " not found.", HttpStatus.NOT_FOUND);
                }
            } else {
                return MyUtils.getResponseEntity("Asset with ID " + productId + " not found.", HttpStatus.NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            return MyUtils.getResponseEntity("Invalid number format for one of the fields.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<Product> getAssignDetails() {
        String username = JwtRequestFilter.CURRENT_USER;
        User user = userDao.findById(username).get();
        return productDao.findByUser(user);
    }

    @Override
    public ResponseEntity<String> removeUserFromProduct(Integer productId, String userName) {
        try {
            // Retrieve the Product entity from the database
            Optional<Product> optionalProduct = productDao.findById(productId);

            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();

                // Check if the product already has the specified user assigned
                User assignedUser = product.getUser();
                if (assignedUser != null && assignedUser.getUserName().equals(userName)) {
                    // If the user is assigned, remove the association
                    product.setUser(null);

                    // Save the updated Product entity
                    productDao.save(product);

                    return MyUtils.getResponseEntity( userName + " return the Asset with ID " + productId + " successfully.", HttpStatus.OK);
                } else {
                    return MyUtils.getResponseEntity( userName + " is not assigned to Asset with ID " + productId + ".", HttpStatus.NOT_FOUND);
                }
            } else {
                return MyUtils.getResponseEntity("Asset with ID " + productId + " not found.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
