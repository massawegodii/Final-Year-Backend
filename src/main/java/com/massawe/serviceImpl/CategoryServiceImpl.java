package com.massawe.serviceImpl;

import com.massawe.constants.MyConstant;
import com.massawe.dao.CategoryDao;
import com.massawe.dao.DepartmentDao;
import com.massawe.entity.Category;
import com.massawe.entity.Department;
import com.massawe.service.CategoryService;
import com.massawe.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryDao categoryDao;

    @Override
    public ResponseEntity<String> addCategory(Map<String, String> requestMap) {
        try {
            if (requestMap.containsKey("name")) {
                String categoryName = requestMap.get("name");

                // Check if the Category name already exists
                if (categoryDao.existsByName(categoryName)) {
                    return MyUtils.getResponseEntity("Category name already exists", HttpStatus.CONFLICT);
                }

                Category newCategory = new Category();
                newCategory.setName(categoryName);
                categoryDao.save(newCategory);

                return MyUtils.getResponseEntity("Category added successfully", HttpStatus.OK);
            } else {
                return MyUtils.getResponseEntity("Name is required", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MyUtils.getResponseEntity(MyConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
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
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try {
            // Check if the request contains the required fields
            if (!requestMap.containsKey("id") || !requestMap.containsKey("name")) {
                return MyUtils.getResponseEntity("ID and Name are required for updating category", HttpStatus.BAD_REQUEST);
            }

            Long id = Long.parseLong(requestMap.get("id"));
            String updatedName = requestMap.get("name");

            // Check if the Category with the given ID exists
            Optional<Category> categoryOptional = categoryDao.findById(id);
            if (categoryOptional.isEmpty()) {
                return MyUtils.getResponseEntity("Category not found for ID: " + id, HttpStatus.NOT_FOUND);
            }

            Category existingCategory = categoryOptional.get();

            // Update the name of the existing Category
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
}
