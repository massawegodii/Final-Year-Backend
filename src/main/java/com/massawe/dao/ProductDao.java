package com.massawe.dao;

import com.massawe.entity.Product;
import com.massawe.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDao extends CrudRepository<Product, Integer> {

    List<Product> findByUser(User user);
}
