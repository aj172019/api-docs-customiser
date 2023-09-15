package com.example.apidocs.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers(request -> request.getServletPath().equals("/favicon.ico")).permitAll()
                                .requestMatchers(PathRequest.toH2Console()).access(hasIpAddress("0.0.0.1"))
                                .requestMatchers(request -> request.getServletPath().equals("/register")).access(hasIpAddress("127.0.0.1"))
                                .anyRequest().permitAll()
                )
                .formLogin(
                        login -> login
                                .loginPage("/login")
                                .permitAll().defaultSuccessUrl("/swagger-ui/index.html")
                )
                .userDetailsService(userDetailsService);
        return http.build();
    }

    private static AuthorizationManager<RequestAuthorizationContext> hasIpAddress(String ipAddress) {
        return (authentication, context) -> {
            HttpServletRequest request = context.getRequest();
            return new AuthorizationDecision(new IpAddressMatcher(ipAddress).matches(request));
        };
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
