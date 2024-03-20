package com.massawe.dao;

import com.massawe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserDao extends JpaRepository<User, String> {
    User findByEmail(@Param("email") String email);

    User findByUserName(String userName);
}
