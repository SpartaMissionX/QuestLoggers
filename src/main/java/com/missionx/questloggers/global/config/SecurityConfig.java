package com.missionx.questloggers.global.config;

import com.missionx.questloggers.domain.user.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    // 비밀번호 암호화
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Spring Security 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 나중에 프론트랑 연결하는 cors? 기능 추가
                // 커스텀 예외처리도 필요하다면 추가
                // csrf 보안 비활성
                .csrf(csrf -> csrf.disable())
                // 세션생성x
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                //url 접근권한
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/health", "/actuator/health/**").permitAll()
                        .requestMatchers("/api/auth/**", "/api/test/**").permitAll()
                        .anyRequest().authenticated()
                );

        http.addFilterBefore(new JwtAuthorizationFilter(jwtTokenProvider, userRepository),
                UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
