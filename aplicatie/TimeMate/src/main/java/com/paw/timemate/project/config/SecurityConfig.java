package com.paw.timemate.project.config;

import com.paw.timemate.project.filter.JwtAuthFilter;
import com.paw.timemate.project.services.userManagement.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * This class is used to configure the security of the application using Spring Security and JWT Authentication and Authorization.
 * It also creates the user and admin roles and provides the access to the endpoints based on the roles.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // Injecting the custom filter for JWT
    @Autowired
    private JwtAuthFilter authFilter;

    // Defining UserDetailsService to load user details from the database
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserInfoService();
    }

    // Configuring the security filter chain to disable CSRF protection and allow unauthenticated access to some endpoints
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable() // Disables CSRF protection
                .authorizeHttpRequests()
                .requestMatchers("/auth/welcome", "/auth/addNewUser", "/auth/generateToken", "/auth/validateToken/**").permitAll() // Allows unauthenticated access to these endpoints
                .and()
                .authorizeHttpRequests().requestMatchers("/auth/user/**").authenticated() // Requires authentication for user endpoints
                .and()
                .authorizeHttpRequests().requestMatchers("/auth/admin/**").authenticated() // Requires authentication for admin endpoints
                .and()
                .authorizeHttpRequests().requestMatchers("/badges/**").authenticated() // Requires authentication for admin endpoints
                .and()
                .authorizeHttpRequests().requestMatchers("/dashboard/**").authenticated() // Requires authentication for admin endpoints
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Uses stateless sessions
                .and()
                .authenticationProvider(authenticationProvider()) // Configuring the authentication provider
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class) // Adds JwtAuthFilter in the filter chain
                .build();
    }

    // Configuring the password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Uses BCrypt for password encoding
    }

    // Configuring the authentication provider to use the user details service and password encoder
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService()); // Sets the user details service
        authenticationProvider.setPasswordEncoder(passwordEncoder()); // Sets the password encoder
        return authenticationProvider;
    }

    // Configuring the authentication manager to use the authentication provider
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}