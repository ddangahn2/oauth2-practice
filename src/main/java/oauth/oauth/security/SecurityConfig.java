package oauth.oauth.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuthUserService customOAuthUserService;
    private final SuccessHandler successHandler;

    // /oauth2/authorization/{registrationId} 경로는 Spring Security의 OAuth 2.0 Client 기능에서 미리 정의된 기본 엔드포인트입니다.
    // 여기서 {registrationId}는 설정한 OAuth 2.0 제공자의 ID(예: naver)입니다.

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("1. HTTP 요청을 처리하는 filter체인 설정 초기화");
        return http
                .oauth2Login(oauth2 -> oauth2
                    .userInfoEndpoint(userInfo -> userInfo.userService(customOAuthUserService)) // AccessToken 발급시 loadUser 실행.
                        // loadUser가 호출된 이후 Authentication 객체는 인증상태로 전환된다.
                    .successHandler(successHandler)
                    .failureHandler((request, response, exception) -> {
                        response.sendRedirect("/oauth2/failure"); // 실패 redirect
                    })
                )
                .build();
    }
}
