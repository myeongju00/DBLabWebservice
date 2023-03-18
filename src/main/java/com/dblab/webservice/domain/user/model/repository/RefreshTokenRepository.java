package com.dblab.webservice.domain.user.model.repository;

import com.dblab.webservice.domain.user.model.entity.RefreshTokenEntity;
import com.dblab.webservice.domain.user.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    RefreshTokenEntity findByUser(UserEntity user);
}
