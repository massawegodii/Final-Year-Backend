package com.massawe.serviceImpl;

import com.massawe.dao.ProductDao;
import com.massawe.entity.Product;
import com.massawe.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ProductService {
    @Autowired
    private ProductDao productDao;

    public Product addNewProduct(Product product) {
            return productDao.save(product);
    }

    public List<Product> getAllProducts(){
        return (List<Product>) productDao.findAll();
    }

    public void deleteProductDetails(Integer productId){
         productDao.deleteById(productId);
    }

    public Product getProductDetailsById(Integer productId){
        return productDao.findById(productId).get();
    }

}
