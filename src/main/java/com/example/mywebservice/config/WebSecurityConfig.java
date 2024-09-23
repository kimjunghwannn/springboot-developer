package com.example.mywebservice.config;

import com.example.mywebservice.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserDetailService userService;

    // 스프링 시큐리티 기능 비활성화
    @Bean
    public WebSecurityCustomizer configure(){
        return (web)-> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers("/static/**");
    }

    // 특정 HTTP 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/login", "/signup", "/user").permitAll()
                        .anyRequest().authenticated()) // 모든 요청에 대해 인증 요구
                .formLogin(formLogin->formLogin
                        .loginPage("/login") // 커스텀 로그인 페이지 설정
                        .defaultSuccessUrl("/posts") // 로그인 성공 후 이동할 url
                        .permitAll()
                 )//로그인 성공 시 이동할 페이지 설정
                .logout(logout->logout // 로그아웃 설정
                        .logoutSuccessUrl("/login")// 로그아웃 후 로그인 페이지로 이동
                        .invalidateHttpSession(true)) // 로그아웃 시 세션 무효화
                .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화(테스트를 위해)
                .build();
    }

    // 인증 관리자 관련 설정
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       BCryptPasswordEncoder bCryptPasswordEncoder,
                                                       UserDetailService userDetailService) throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService); // 사용자 정보 서비스 설정
        authProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return new ProviderManager(authProvider); // 인증관리자 설정
    }

    // 패스워드 인코더로 사용할 빈 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
