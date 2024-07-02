package com.massawe.dao;

import com.massawe.entity.UserTrack;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthLogDao  extends JpaRepository<UserTrack, Long> {
    UserTrack findFirstByUsernameOrderByTimestampDesc(String username);
}
