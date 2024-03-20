package com.massawe.serviceImpl;

import com.massawe.constants.MyConstant;
import com.massawe.dao.ProductDao;
import com.massawe.entity.Product;
import com.massawe.service.ProductRestService;
import com.massawe.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductRestServiceImpl implements ProductRestService {
    @Autowired
    ProductDao productDao;
    @Override
    public ResponseEntity<String> updateAssets(Map<String, String> requestMap) {
        try {
            // Check if the request map contains productId
            if (!requestMap.containsKey("productId")) {
                return MyUtils.getResponseEntity("ProductId is required for updating assets.", HttpStatus.BAD_REQUEST);
            }

            Integer productId = Integer.parseInt(requestMap.get("productId"));

            // Retrieve the Product entity from the database
            Optional<Product> optionalProduct = productDao.findById(productId);

            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();

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

                // Save the updated Product entity
                productDao.save(product);

                return MyUtils.getResponseEntity("Product with ID " + productId + " updated successfully.", HttpStatus.OK);
            } else {
                return MyUtils.getResponseEntity("Product with ID " + productId + " not found.", HttpStatus.NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            return MyUtils.getResponseEntity("Invalid number format for one of the fields.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @Override
//    public Product findProductIdByUserName(String userName) {
//        return productDao.findByUser_UserName(userName);
//    }

}
