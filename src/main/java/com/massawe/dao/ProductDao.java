package com.massawe.dao;

import com.massawe.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDao extends CrudRepository<Product, Integer> {
//    Product findByUser_UserName(String userName);
}
