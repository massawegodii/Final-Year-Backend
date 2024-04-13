package com.massawe.dao;

import com.massawe.entity.RequestAsset;
import com.massawe.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestAssetDao extends CrudRepository<RequestAsset, Long> {
    List<RequestAsset> findByUser(User user);
}
