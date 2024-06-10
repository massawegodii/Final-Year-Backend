package com.massawe.serviceImpl;

import com.massawe.constants.MyConstant;
import com.massawe.dao.CategoryDao;
import com.massawe.entity.Category;
import com.massawe.service.CategoryService;
import com.massawe.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryDao categoryDao;

    @Override
    public ResponseEntity<String> addCategory(Map<String, Object> requestMap) {
        try {
            // Retrieve and validate the category name
            String categoryName = (String) requestMap.get("name");
            if (categoryName == null || categoryName.trim().isEmpty()) {
                return MyUtils.getResponseEntity("Asset Type cannot be empty", HttpStatus.BAD_REQUEST);
            }

            // Retrieve and validate the categories list
            Object categoriesObj = requestMap.get("categories");
            if (categoriesObj == null) {
                return MyUtils.getResponseEntity("Categories cannot be null", HttpStatus.BAD_REQUEST);
            }

            if (!(categoriesObj instanceof List)) {
                return MyUtils.getResponseEntity("Categories should be a list", HttpStatus.BAD_REQUEST);
            }

            List<?> categoriesList = (List<?>) categoriesObj;
            if (categoriesList.isEmpty()) {
                return MyUtils.getResponseEntity("Categories cannot be empty", HttpStatus.BAD_REQUEST);
            }

            // Ensure all elements in the list are strings
            for (Object item : categoriesList) {
                if (!(item instanceof String)) {
                    return MyUtils.getResponseEntity("All categories should be strings", HttpStatus.BAD_REQUEST);
                }
            }

            List<String> categories =  (List<String>) categoriesObj;

            // Check if the category name already exists
            if (categoryDao.existsByName(categoryName)) {
                return MyUtils.getResponseEntity("Asset Type already exists", HttpStatus.BAD_REQUEST);
            }

            // Check if any of the categories already exist
            for (String category : categories) {
                if (categoryDao.existsByCategoriesContaining(category)) {
                    return MyUtils.getResponseEntity("Category " + category + " already exists", HttpStatus.BAD_REQUEST);
                }
            }

            // Create and save the new category
            Category newCategory = new Category();
            newCategory.setName(categoryName);
            newCategory.setCategories(categories);
            categoryDao.save(newCategory);

            return MyUtils.getResponseEntity("Category added successfully", HttpStatus.OK);
        } catch (ClassCastException e) {
            return MyUtils.getResponseEntity("Invalid data format: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> deleteCategory(String id) {
        try {
            Optional<Category> optionalCategory = categoryDao.findById(Long.parseLong(id));
            if (optionalCategory.isPresent()) {
                Category category = optionalCategory.get();
                categoryDao.delete(category);
                return MyUtils.getResponseEntity("Category deleted successfully!", HttpStatus.OK);
            } else {
                return MyUtils.getResponseEntity("Category not found", HttpStatus.NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return MyUtils.getResponseEntity("Invalid ID format", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public ResponseEntity<?> getAllCategory() {
        try {
            List<Category> categoryList = categoryDao.findAll();
            if (categoryList.isEmpty()) {
                return MyUtils.getResponseEntity("No Category found", HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(categoryList);
        } catch (Exception e) {
            e.printStackTrace();
            return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, Object> requestMap) {
        try {
            // Check if the request contains the required fields
            if (!requestMap.containsKey("id") || !requestMap.containsKey("name")) {
                return MyUtils.getResponseEntity("Missing field required for updating Category", HttpStatus.BAD_REQUEST);
            }

            Long id = Long.parseLong((String) requestMap.get("id"));
            String updatedName = (String) requestMap.get("name");

            // Check if the Category with the given ID exists
            Optional<Category> categoryOptional = categoryDao.findById(id);
            if (categoryOptional.isEmpty()) {
                return MyUtils.getResponseEntity("Category not found for ID: " + id, HttpStatus.NOT_FOUND);
            }

            Category existingCategory = categoryOptional.get();

            // Update the name and assetType of the existing Category
            existingCategory.setName(updatedName);

            // Save the updated Category
            categoryDao.save(existingCategory);

            return MyUtils.getResponseEntity("Category updated successfully", HttpStatus.OK);
        } catch (NumberFormatException e) {
            return MyUtils.getResponseEntity("Invalid ID format", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getCategoriesByName(String name) {
        try {
            List<Category> categories = categoryDao.getCategoriesByName(name);
            if (categories.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No categories found with the provided name");
            }
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while fetching categories");
        }
    }


}
