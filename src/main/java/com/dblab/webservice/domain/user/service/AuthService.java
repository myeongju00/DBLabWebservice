package com.dblab.webservice.domain.user.service;

import com.dblab.webservice.domain.user.model.entity.Role;
import com.dblab.webservice.domain.user.model.info.impl.GoogleOAuth2UserInfo;
import com.dblab.webservice.domain.user.model.request.TokenReissueRequest;
import com.dblab.webservice.domain.user.model.response.UserInfoResponse;
import com.dblab.webservice.global.message.AuthMessage;
import com.dblab.webservice.global.message.UserMessage;
import com.dblab.webservice.oauth.SecurityUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.dblab.webservice.domain.user.model.entity.ProviderType;
import com.dblab.webservice.domain.user.model.entity.RefreshTokenEntity;
import com.dblab.webservice.domain.user.model.entity.UserEntity;
import com.dblab.webservice.domain.user.model.info.OAuth2UserInfo;
import com.dblab.webservice.domain.user.model.info.OAuth2UserInfoFactory;
import com.dblab.webservice.domain.user.model.repository.RefreshTokenRepository;
import com.dblab.webservice.domain.user.model.repository.UserRepository;
import com.dblab.webservice.global.exception.ApiException;
import com.dblab.webservice.global.message.DefaultMessage;
import com.dblab.webservice.oauth.jwt.TokenProvider;
import com.dblab.webservice.oauth.jwt.dto.TokenResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ObjectMapper objectMapper;


//    @Transactional
//    public UserInfoResponse signup(UserSignUpRequest userSignInRequest) {
//        if (userRepository.existsByNickname(userSignInRequest.getNickname())) {
//            throw new ApiException(UserResponseMessage.USER_ALREADY_REGISTERED);
//        }
//
//        UserEntity user = new UserEntity(userSignInRequest);
//        return userRepository.save(user).toDto().toResponse();
//    }
//
//    public String oauthLogin(String code) {
//        ResponseEntity<String> accessTokenResponse = oauthService.createPostRequest(code);
//        OAuthToken oAuthToken = oauthService.getAccessToken(accessTokenResponse);
//
//        ResponseEntity<String> userInfoResponse = oauthService.createGetRequest(oAuthToken);
//        GoogleUser googleUser = oauthService.getUserInfo(userInfoResponse);
//
//        if (!isJoinedUser(googleUser)) {
//            signUp(googleUser, oAuthToken);
//        }
//        User user = userRepository.findByEmail(googleUser.getEmail()).orElseThrow(UserNotFoundException::new);
//        return jwtTokenProvider.createToken(user.getId());
//    }

    @Transactional
    TokenResponse createToken(UserEntity user) {
        TokenResponse jwtTokenResponse = tokenProvider.generateJwtToken(user.getName(), user.getEmail(), Role.GUEST);

        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .user(user)
                .refreshToken(jwtTokenResponse.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return jwtTokenResponse;
    }

    @Transactional
    public UserEntity createUser(OAuth2UserInfo userInfo, ProviderType providerType) {
        UserEntity user = UserEntity.builder()
                .socialId(userInfo.getId())
                .providerType(providerType)
                .email(userInfo.getEmail())
                .picture(userInfo.getImageUrl())
                .build();

        return userRepository.save(user);
    }

    private UserEntity getSocialUser(String body, ProviderType providerType) {
        try {
            var attributes = objectMapper.readValue(body, Map.class);
            OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, attributes);

            return userRepository.findBySocialId(userInfo.getId())
                    .orElse(createUser(userInfo, providerType));
        } catch (Exception e) {
            throw new ApiException(DefaultMessage.INTERNAL_SERVER_ERROR);
        }
    }



    private UserEntity createGoogleUser(GoogleOAuth2UserInfo userInfo) {
        try {
            UserEntity user = userRepository.findBySocialId(userInfo.getId())
                    .orElse(createUser(userInfo, ProviderType.GOOGLE));
            return user;
        } catch (Exception e) {
            throw new ApiException(DefaultMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public TokenResponse reissue(TokenReissueRequest tokenReissueRequest) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenReissueRequest.getRefreshToken())) {
            throw new ApiException(AuthMessage.INVALID_REFRESH_TOKEN);
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenReissueRequest.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        // TODO : 지금 UserFind 하는 것이 중복적으로 코드가 발생하는데 Controller Layer 에서 userService를 이용하여
        // authService에 전달하는게 좋을듯?? (Entity가 mapsId 때문에 이렇긴한데....)
        // 논의가 필요!!!!
        String id = authentication.getName();
        Optional<UserEntity> userEntity = userRepository.findById(Long.parseLong(id));
        if (userEntity.isEmpty())
            throw new ApiException(UserMessage.USER_NOT_FOUND);

        RefreshTokenEntity refreshToken = refreshTokenRepository.findByUser(userEntity.get());

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getRefreshToken().equals(tokenReissueRequest.getRefreshToken())) {
            throw new ApiException(AuthMessage.INVALID_REFRESH_TOKEN);
        }

        // 5. 새로운 토큰 생성
        TokenResponse newRefreshToken = tokenProvider.generateJwtToken(id, userEntity.get().getEmail(), Role.GUEST);

        // 6. 저장소 정보 업데이트
        refreshToken.setRefreshToken(newRefreshToken.getRefreshToken());
        refreshTokenRepository.save(refreshToken);

        // 토큰 발급
        return newRefreshToken;
    }

    @Transactional
    public void logout(String accessToken, String refreshToken) {
        // 1. Access Token 검증
        if (!tokenProvider.validateToken(accessToken)) {
            throw new ApiException(DefaultMessage.UNAUTHORIZED);
        }

        // 2. Access Token 에서 authentication 을 가져옵니다.
        Authentication authentication = tokenProvider.getAuthentication(accessToken);

        // 3. DB에 저장된 Refresh Token 제거
        Long userId = Long.parseLong(authentication.getName());
        refreshTokenRepository.deleteById(userId);

        // 4. Access Token blacklist에 등록하여 만료시키기
        Long expiration = tokenProvider.getExpiration(accessToken);
    }

    @Transactional
    public UserInfoResponse signupUpdate(UserInfoResponse userInfoResponse) {
        Long id = SecurityUtil.getCurrentUserId();
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new ApiException(UserMessage.USER_NOT_FOUND));

        userEntity.update(userInfoResponse.getName(), userInfoResponse.getPicture());

        return userRepository.save(userEntity).toDto().toUserInfoResponse();
    }
}
