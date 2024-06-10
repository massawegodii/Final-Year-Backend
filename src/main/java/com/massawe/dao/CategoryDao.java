package com.massawe.dao;

import com.massawe.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryDao extends JpaRepository<Category, Long> {
    boolean existsByName(String categoryName);
    boolean existsByCategoriesContaining(String category);
    List<Category> getCategoriesByName(String name);
}
