package com.dblab.webservice.domain.user.model.repository;

import com.dblab.webservice.domain.user.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findBySocialId(String socialId);
}
