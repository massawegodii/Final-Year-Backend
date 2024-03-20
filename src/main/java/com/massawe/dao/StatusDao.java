package com.massawe.dao;

import com.massawe.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusDao extends JpaRepository<Status, Long> {
    boolean existsByName(String statusName);
}
