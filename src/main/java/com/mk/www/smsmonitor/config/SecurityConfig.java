package com.mk.www.smsmonitor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // API 중심이므로 CSRF는 일단 끕니다.
                .authorizeHttpRequests(auth -> auth
                        // Swagger UI 및 관련 리소스는 반드시 인증이 필요하도록 설정
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/docs/**").authenticated()
                        // 그 외 API들은 현재 테스트 편의를 위해 일단 열어둘 수도 있으나, 요청대로 전부 막겠습니다.
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults()) // 브라우저 팝업 형태의 Basic Auth 사용
                .formLogin(Customizer.withDefaults()); // 폼 기반 로그인도 가능하게 함

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // 지정하신 비밀번호 303030을 사용하는 인메모리 유저 생성
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("303030")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }
}
