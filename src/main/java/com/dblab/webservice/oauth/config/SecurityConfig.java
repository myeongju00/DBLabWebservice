package com.dblab.webservice.oauth.config;

import com.dblab.webservice.domain.user.handler.OAuth2AuthenticationFailureHandler;
import com.dblab.webservice.domain.user.handler.OAuth2AuthenticationSuccessHandler;
import com.dblab.webservice.domain.user.model.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.dblab.webservice.domain.user.service.CustomOAuth2UserService;
import com.dblab.webservice.oauth.jwt.JwtAccessDeniedHandler;
import com.dblab.webservice.oauth.jwt.JwtAuthenticationEntryPoint;
import com.dblab.webservice.oauth.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer configure() {
        return (web -> {
            web.ignoring().antMatchers(HttpMethod.GET,
                    "/swagger-ui/**", "/v2/api-docs"
                    ,"/h2-console/**", "/favicon.ico");
        });
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.cors().and().csrf().disable()
                // exception handling 할 때 우리가 만든 클래스를 추가
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)// h2-console 을 위한 설정을 추가

                // h2-console 을 위한 설정을 추가
                .and()
                    .headers()
                    .frameOptions()
                    .sameOrigin()

                // 시큐리티는 기본적으로 세션을 사용
                // 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 로그인, 회원가입 API 는 토큰이 없는 상태에서 요청이 들어오기 때문에 permitAll 설정
                .and()
                    .authorizeHttpRequests()
                .antMatchers("/**/auth/**", "/swagger-ui/**", "/swagger-resources/**").permitAll()
                .antMatchers(HttpMethod.POST, "/**/post", "/**/post/{postId}/comment",
                        "/**/post/{id}/like", "/**/post/{id}/scrap", "/**/post/{id}/vote",
                        "/report/comment/{commentId}").authenticated()
                .antMatchers(HttpMethod.PUT, "/**/post").authenticated()
                .antMatchers(HttpMethod.DELETE, "/**/post/{id}",
                        "/**/post/{postId}/comment/{commentId}").authenticated()
                .antMatchers(HttpMethod.GET, "/**/userInfo", "/**/post/comment",
                        "/**/post/my", "/**/post/my/scrap", "/**/post/my/voted").authenticated()

                // JwtFilter 를 addFilterBefore 로 등록했던 JwtSecurityConfig 클래스를 적용
                .and()
                    .apply(new JwtFilterConfig(tokenProvider))
                .and()
                    .oauth2Login()
                    .authorizationEndpoint()
                    .baseUri("/oauth2/authorization")
                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
                .and()
                    .userInfoEndpoint()
                    .userService(customOAuth2UserService)    // 소셜로그인 성공 시 이후 처리를 담당한 서비스 등록
                .and()
                    .successHandler(oAuth2AuthenticationSuccessHandler)
                    .failureHandler(oAuth2AuthenticationFailureHandler())
                .and()
                    .build();
    }

    /*
     * 쿠키 기반 인가 Repository
     * 소셜 로그인 시 정보를 가져오기 위한 인가 응답을 연계 하고 검증할 때 사용.
     * */
    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    /*
     * Oauth 인증 실패 핸들러
     * */
    @Bean
    public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler(oAuth2AuthorizationRequestBasedOnCookieRepository());
    }
}
