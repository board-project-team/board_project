package com.example.backend.security;

import com.example.backend.user.entity.User;
import com.example.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 1. 소셜 서비스 구별 (github, google 등)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 2. OAuth2 로그인 진행 시 키가 되는 필드값 (Primary Key와 같은 의미)
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // 3. 데이터를 규격화하여 추출
        OAuth2Attributes attributes = OAuth2Attributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        // 4. DB에 저장하거나 업데이트
        User user = saveOrUpdate(attributes);

        // 5. Spring Security 세션에 저장할 객체 반환
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    private User saveOrUpdate(OAuth2Attributes attributes) {
        User user = userRepository.findByProviderAndProviderId(attributes.getProvider(), attributes.getProviderId())
                .map(entity -> entity.update(attributes.getName()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}