package com.example.backend.config;

import com.example.backend.security.CustomOAuth2UserService;
import com.example.backend.security.JwtAuthenticationFilter;
import com.example.backend.security.JwtTokenProvider;
import com.example.backend.security.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider tokenProvider;
    private final CustomOAuth2UserService customOAuth2UserService; // 추가
    private final OAuth2SuccessHandler oauth2SuccessHandler; // 추가


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(f -> f.disable())
                .httpBasic(h -> h.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        
                        // 🚩 여기입니다! 인그레스 로드밸런서가 통과할 수 있게 이 줄을 추가합니다.
                        .requestMatchers("/api/health").permitAll() 
                        
                        .requestMatchers("/api/auth/**", "/login/**", "/oauth2/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/posts/**", "/api/attachments/**").permitAll()

                        // ⚠️ 이 줄이 항상 가장 마지막에 있어야 합니다.
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(oauth2SuccessHandler)
                )
                .addFilterBefore(new JwtAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);
    
        return http.build();
    }
    
    // @Bean
    // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    //     http
    //             .csrf(csrf -> csrf.disable())
    //             .cors(cors -> cors.configurationSource(corsConfigurationSource()))
    //             .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    //             .formLogin(f -> f.disable())
    //             .httpBasic(h -> h.disable())
    //             .authorizeHttpRequests(auth -> auth
    //                     .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
    //                     .requestMatchers("/api/auth/**", "/login/**", "/oauth2/**").permitAll() // OAuth2 경로 허용
    //                     .requestMatchers(HttpMethod.GET, "/api/posts/**", "/api/attachments/**").permitAll()
    //                     .anyRequest().authenticated()
    //             )
    //             // OAuth2 로그인 설정 추가
    //             .oauth2Login(oauth2 -> oauth2
    //                     .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
    //                     .successHandler(oauth2SuccessHandler)
    //             )
    //             .addFilterBefore(new JwtAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);

    //     return http.build();
    // }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://34.8.68.57"));
        config.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true); // 쿠키 쓸 때 필요(지금은 없어도 OK)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
