package oauth.oauth.security;


import oauth.oauth.domain.User;
import oauth.oauth.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class CustomOAuthUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    // 액세스 토큰과 클라이언트 정보를 포함하는 OAuth2UserRequest 객체가 생성됩니다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("2. 인가코드를 통해 AccessToken을 받은 userRequest 생성됨");
        printAccessToken(userRequest);

        System.out.println("3. AccessToken으로 유저정보 받아옴");
        OAuth2User oAuth2User = getOAuth2User(userRequest);
        System.out.println("    유저정보 : " + oAuth2User.toString());

        System.out.println("4. 유저 생성");
        userCreate(userRequest, oAuth2User);

        throw new OAuth2AuthenticationException("의도적인 실패 exception.");
//        return oAuth2User;
    }

    private OAuth2User getOAuth2User(OAuth2UserRequest userRequest) {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        return delegate.loadUser(userRequest);
    }

    private static void printAccessToken(OAuth2UserRequest userRequest) {
        // 1. ClientRegistration 정보 출력
        // 클라이언트(즉, 여러분의 서버)가 OAuth 2.0 인증 서버(예: 네이버, 구글)와 통신하기 위해 필요한 설정 정보를 담고 있습니다
        System.out.println("    Client Registration: " + userRequest.getClientRegistration());

        // 2. Access Token 정보 출력
        System.out.println("    Access Token: " + userRequest.getAccessToken().getTokenValue());
        System.out.println("    Access Token Expiration: " + userRequest.getAccessToken().getExpiresAt());
        System.out.println("    Access Token Scopes: " + userRequest.getAccessToken().getScopes());

        // 3. Additional Parameters 출력
        Map<String, Object> additionalParameters = userRequest.getAdditionalParameters();
        System.out.println("    Additional Parameters: " + additionalParameters);
    }
    
    private void userCreate(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        System.out.println("    registrationId = " + registrationId);
        String email = null;
        String name = null;

        if("naver".equals(registrationId)) {
            Object response = attributes.get("response");
            if (response instanceof Map) {
                Map<String, Object> responseAttributes = (Map<String, Object>) response;
                email = (String) responseAttributes.get("email");
                name = (String) responseAttributes.get("name");
            }
        } else if ("kakao".equals(registrationId)) {
            Object kakaoAccount = attributes.get("kakao_account"); // "kakao_account" 내부 정보 가져오기
            if (kakaoAccount instanceof Map) {
                Map<String, Object> kakaoAccountAttributes = (Map<String, Object>) kakaoAccount;
                email = (String) kakaoAccountAttributes.get("email");

                // 닉네임 추출 (kakao_account.profile.nickname)
                Object profile = kakaoAccountAttributes.get("profile");
                if (profile instanceof Map) {
                    Map<String, Object> profileAttributes = (Map<String, Object>) profile;
                    name = (String) profileAttributes.get("nickname");
                }
            }
        }
        User user = new User(email, name);
        userRepository.save(user);
    }
}
