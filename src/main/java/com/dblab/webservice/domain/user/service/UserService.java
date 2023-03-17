package com.dblab.webservice.domain.user.service;

import com.dblab.webservice.domain.user.model.dto.UserDto;
import com.dblab.webservice.domain.user.model.entity.UserEntity;
import com.dblab.webservice.domain.user.model.repository.UserRepository;
import com.dblab.webservice.global.exception.ApiException;
import com.dblab.webservice.global.message.UserMessage;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserDto findUserEntity(Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);

        if(userEntity.isEmpty())
            throw new ApiException(UserMessage.USER_NOT_FOUND);
        else
            return userEntity.get().toDto();
    }
}
