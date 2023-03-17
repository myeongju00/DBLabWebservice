package com.dblab.webservice.domain.user.handler;

import static com.dblab.webservice.domain.user.model.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

import com.dblab.webservice.domain.user.model.entity.ProviderType;
import com.dblab.webservice.domain.user.model.entity.RefreshTokenEntity;
import com.dblab.webservice.domain.user.model.entity.Role;
import com.dblab.webservice.domain.user.model.entity.UserEntity;
import com.dblab.webservice.domain.user.model.info.OAuth2UserInfo;
import com.dblab.webservice.domain.user.model.info.OAuth2UserInfoFactory;
import com.dblab.webservice.domain.user.model.repository.RefreshTokenRepository;
import com.dblab.webservice.domain.user.model.repository.UserRepository;
import com.dblab.webservice.domain.user.util.CookieUtil;
import com.dblab.webservice.global.exception.ApiException;
import com.dblab.webservice.global.message.UserMessage;
import com.dblab.webservice.oauth.jwt.TokenProvider;
import com.dblab.webservice.oauth.jwt.dto.TokenResponse;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        // TODO : RedirecURL 유효성 검사
//        if(redirectUri.isPresent()) {
//            throw new IllegalArgumentException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
//        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        ProviderType providerType = ProviderType.valueOf(authToken.getAuthorizedClientRegistrationId().toUpperCase());

        DefaultOAuth2User user = ((DefaultOAuth2User) authentication.getPrincipal());
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());

        TokenResponse tokenResponse  = createToken(userInfo.getId());
        String accessToken = tokenResponse.getAccessToken();
        String refreshToken = tokenResponse.getRefreshToken();

        int cookieMaxAge = tokenProvider.getExpiration(refreshToken).intValue() / 60;

        CookieUtil.deleteCookie(request, response, "refresh_token");
        CookieUtil.addCookie(response, "refresh_token", refreshToken, cookieMaxAge);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", accessToken)
                .build().toUriString();
    }

    private TokenResponse createToken(String socialId) {
        UserEntity user = userRepository.findBySocialId(socialId)
                .orElseThrow(() -> new ApiException(UserMessage.USER_NOT_FOUND));

        TokenResponse tokenResponse = tokenProvider.generateJwtToken(Long.toString(user.getUserId()), Role.GUEST);
        // DB 저장
        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .user(user)
                .refreshToken(tokenResponse.getRefreshToken())
                .build();
        refreshTokenRepository.save(refreshToken);

        return tokenResponse;
    }
}
