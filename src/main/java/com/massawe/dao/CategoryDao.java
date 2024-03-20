package com.massawe.dao;

import com.massawe.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryDao extends JpaRepository<Category, Long> {
    boolean existsByName(String categoryName);
}
