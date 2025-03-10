package com.example.mingle.config;

import com.example.mingle.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // CSRF 보호 비활성화 (필요에 따라 변경)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/bottom/**", "/guestOrHost", "/host/register", "/guests/register", "/about", "/restaurants/**",
                                "/accommodation/**", "/contact", "/css/**", "/js/**", "/images/**", "/reviews/**").permitAll()  // 공개 경로들
                        .anyRequest().authenticated()  // 나머지 요청은 인증 필요
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/loginSuccess", true)
                        .failureUrl("/login?error=true")
                        .usernameParameter("idid")  // 사용자명으로 idid 사용
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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // 비밀번호 암호화
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetailsService)  // CustomUserDetailsService 사용
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }
}
