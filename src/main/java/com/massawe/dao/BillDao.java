package com.massawe.dao;

import com.massawe.entity.Bill;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BillDao extends CrudRepository<Bill, Integer> {
    List<Bill> getAllBills();

    List<Bill> getBillByUserName(@Param("username") String username);
}
