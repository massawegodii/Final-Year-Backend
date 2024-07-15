package com.massawe.dao;

import com.massawe.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentDao extends JpaRepository<Department, Long> {
    boolean existsByName(String departmentName);
    boolean existsByNameContaining(String name);
    List<Department> getDepartmentByName(String name);
}
