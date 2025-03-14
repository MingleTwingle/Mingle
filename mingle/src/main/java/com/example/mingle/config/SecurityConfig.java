package com.example.mingle.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Configuration
@EnableWebSecurity

public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                .csrf(csrf -> csrf.disable()) // ✅ CSRF 보호 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login","/bottom/**",
                                "/guestOrHost","/host/register","/guests/register","/about","/restaurants/**","/accommodation/**","/accommodationDetail/**",
                                "/contact", "/css/**", "/js/**", "/images/**"
                                ,"/reviews/**","/review/**").permitAll()
                        .requestMatchers("/mypage/**").authenticated()  // mypage에 로그인해야지만 접근
                        .requestMatchers("/mypage/guest").hasRole("USER")
                        .requestMatchers("/mypage/host").hasRole("HOST")
                        .requestMatchers("/mypage/profile").hasRole("USER") // 게스트 전용 마이페이지
                        .requestMatchers("/mypage/hostprofile").hasRole("HOST") // 호스트 전용 마이페이지
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")  // 로그인 페이지 설정
                        .defaultSuccessUrl("/loginSuccess", true) // ✅ 로그인 후 홈으로 이동
                        .failureUrl("/login?error=true")
                        .usernameParameter("idid")
                        .passwordParameter("password")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }
    @Bean
    public AuthenticationSuccessHandler customLoginSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_HOST"))) {
                    response.sendRedirect("/mypage/hostprofile"); // 호스트는 호스트 마이페이지로 이동
                } else {
                    response.sendRedirect("/mypage/profile"); // 게스트는 게스트 마이페이지로 이동
                }
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
