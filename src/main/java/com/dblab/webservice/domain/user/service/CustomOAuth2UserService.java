package com.dblab.webservice.domain.user.service;

import com.dblab.webservice.domain.user.model.dto.OAuthAttributes;
import com.dblab.webservice.domain.user.model.entity.ProviderType;
import com.dblab.webservice.domain.user.model.entity.Role;
import com.dblab.webservice.domain.user.model.entity.UserEntity;
import com.dblab.webservice.domain.user.model.info.OAuth2UserInfo;
import com.dblab.webservice.domain.user.model.info.OAuth2UserInfoFactory;
import com.dblab.webservice.domain.user.model.repository.UserRepository;
import com.dblab.webservice.global.exception.ApiException;
import com.dblab.webservice.global.message.UserMessage;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        try {
            return this.process(userRequest, user);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {
        ProviderType providerType = ProviderType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());
        UserEntity savedUser = userRepository.findBySocialId(userInfo.getId())
                .orElse(createUser(userInfo, providerType));

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        return new DefaultOAuth2User(Collections.singletonList(new SimpleGrantedAuthority(Role.GUEST.toString()))
                ,user.getAttributes(), userNameAttributeName);
    }

    // 로직 테스트 못해봄.
    private UserEntity createUser(OAuth2UserInfo userInfo, ProviderType providerType) {
        UserEntity user = UserEntity.builder()
                .socialId(userInfo.getId())
                .name(userInfo.getName())
                .email(userInfo.getEmail())
                .picture(userInfo.getImageUrl())
                .role(Role.GUEST)
                .providerType(providerType)
                .build();

        return userRepository.save(user);
    }

    private UserEntity saveOrUpdate(OAuthAttributes attributes) {
        UserEntity user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());
        return userRepository.save(user);
    }

}
