package com.massawe.rest;

import com.massawe.constants.MyConstant;
import com.massawe.entity.ImageModel;
import com.massawe.entity.Product;
import com.massawe.entity.User;
import com.massawe.serviceImpl.ProductService;
import com.massawe.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/products")
@RestController
public class ProductRest {
    @Autowired
    private ProductService productService;



    @PreAuthorize("hasRole('Admin')")
    @PostMapping(value = {"/addNewProduct"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> addNewProduct(@RequestPart Product product,
                                 @RequestPart("imageFile") MultipartFile[] file){

        try {
            Set<ImageModel> images = uploadImage(file);
            product.setProductImages(images);
            productService.addNewProduct(product);
            return MyUtils.getResponseEntity("Product Added Successfully", HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Set<ImageModel> uploadImage(MultipartFile[] multipartFiles) throws IOException {
        Set<ImageModel> imageModels = new HashSet<>();

        for (MultipartFile file: multipartFiles) {
            ImageModel imageModel = new ImageModel(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
            );
            imageModels.add(imageModel);
        }

        return imageModels;
    }

    @GetMapping({"/getAllProducts"})
    public List<Product> getAllProduct(){
        return productService.getAllProducts();
    }

    @DeleteMapping({"/deleteProductDetails/{productId}"})
    public void deleteProductDetails(@PathVariable("productId") Integer productId) {
        productService.deleteProductDetails(productId);
    }

    @GetMapping({"/getProductDetailsById/{productId}"})
    public Product getProductDetailsById(@PathVariable("productId") Integer productId){
        return productService.getProductDetailsById(productId);
    }
}
