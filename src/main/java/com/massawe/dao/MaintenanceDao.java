package com.massawe.dao;

import com.massawe.entity.Maintenance;
import org.springframework.data.repository.CrudRepository;

public interface MaintenanceDao extends CrudRepository<Maintenance, Long> {
    boolean existsByNote(String note);
}
