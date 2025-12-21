package com.example.backend.security;

import com.example.backend.user.entity.Role;
import com.example.backend.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuth2Attributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String provider;
    private String providerId;

    @Builder
    public OAuth2Attributes(Map<String, Object> attributes, String nameAttributeKey,
                            String name, String email, String provider, String providerId) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
    }

    // GitHub, Google 등 제공자별로 데이터를 추출하는 정적 메서드
    public static OAuth2Attributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if ("github".equals(registrationId)) {
            return ofGithub(userNameAttributeName, attributes);
        }
        // 나중에 google 등 추가 가능
        return null;
    }

    private static OAuth2Attributes ofGithub(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .provider("github")
                .providerId(String.valueOf(attributes.get("id")))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    // 처음 가입할 때 User 엔티티로 변환
    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .provider(provider)
                .providerId(providerId)
                .role(Role.USER) // 기본 권한 USER
                .build();
    }
}