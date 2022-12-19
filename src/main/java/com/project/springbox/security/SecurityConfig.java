package com.project.springbox.security;

import com.project.springbox.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            UserService userService,
            AuthenticationManager authManager
    ) throws Exception {
        return http
                .csrf().disable()
                .addFilter(new JWTLoginFilter(authManager))
                .addFilterAfter(new JWTVerifyFilter(userService), JWTLoginFilter.class)
                .authorizeRequests()
                .antMatchers("/register")
                .permitAll() // Man behöver inte vara inloggad för att registrera sig
                .antMatchers(HttpMethod.GET, "/files")
                .hasRole("USER") // Man ska vara registrerad för kunna se sina filer
                .antMatchers(HttpMethod.DELETE,"/delete/*")
                .hasRole("USER") // Man ska vara registrerad för kunna ta bort filer
                .antMatchers(HttpMethod.GET,"/all-files")
                .hasRole("ADMIN") // Man ska vara registrerad som admin för kunna se alla filer
                .anyRequest()
                .authenticated()
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(10);
    }
}
