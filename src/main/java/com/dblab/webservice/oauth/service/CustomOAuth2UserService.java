package com.dblab.webservice.oauth.service;

import com.dblab.webservice.domain.user.model.dto.OAuthAttributes;
import com.dblab.webservice.domain.user.model.entity.UserEntity;
import com.dblab.webservice.domain.user.model.repository.UserRepository;
import com.dblab.webservice.domain.user.model.dto.UserDto;
import java.util.Collections;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
//
//@RequiredArgsConstructor
//@Service
//public class CustomOAuth2UserService implements OAuth2UserService {
//
//    private final UserRepository userRepository;
//    private final HttpSession session;
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
//        OAuth2User oAuth2User = delegate.loadUser(userRequest);
//
//        /* OAuth2 서비스 id 구분코드 ( 구글, 카카오, 네이버 ) */
//        String registrationId = userRequest.getClientRegistration().getRegistrationId();
//
//        /* OAuth2 로그인 진행시 키가 되는 필드 값 (PK) (구글의 기본 코드는 "sub") */
//        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
//        .getUserInfoEndpoint().getUserNameAttributeName();
//
//        /* OAuth2UserService */
//        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
//
//        UserEntity user = saveOrUpdate(attributes);
//
//        /* 세션 정보를 저장하는 직렬화된 dto 클래스*/
//        session.setAttribute("user", new UserDto(user));
//
//        return new DefaultOAuth2User(
//                Collections.singleton(new SimpleGrantedAuthority(user.getRole().getKey())),
//                attributes.getAttributes(),
//                attributes.getNameAttributeKey());
//    }
//    /* 소셜로그인시 기존 회원이 존재하면 수정날짜 정보만 업데이트해 기존의 데이터는 그대로 보존 */
//    private UserEntity saveOrUpdate(OAuthAttributes attributes) {
//        UserEntity user = userRepository.findByEmail(attributes.getEmail())
//                .map(UserEntity::updateModifiedDate)
//                .orElse(attributes.toEntity());
//        return userRepository.save(user);
//    }
//
//
//
//
//}
