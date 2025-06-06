package com.minh.simple_typing_game.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    /**
     * Configuration for http security of my project.
     *
     * @param http HttpSecurity object to configure security settings
     * @return SecurityFilterChain configured security filter chain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .headers(headers -> headers.frameOptions(frameOption -> frameOption.disable())).authorizeHttpRequests(
                auth -> auth
                        .requestMatchers("/oauth2/**").permitAll() // Allow OAuth2 endpoints for authorization
                        .requestMatchers("/login/oauth2/**").permitAll() // Allow OAuth2 login endpoints
                        .requestMatchers("/auth/**").permitAll() // Allow auth controller endpoints
                        .requestMatchers("/logout").permitAll() // Allow logout endpoint
                        .requestMatchers("/h2-console/**").permitAll() // Allow H2 console for development purposes
                        .requestMatchers("/error").permitAll() // Allow error endpoint for handling errors

                        // .requestMatchers("/api/auth/**").permitAll()
                        // .requestMatchers("/api/words/**").permitAll()
                        // .requestMatchers("/api/leaderboard/**").permitAll()
                        // .anyRequest().authenticated()

                        .anyRequest().authenticated() // Only use it for the development phase while there's no api
        ).oauth2Login(oauth2 -> oauth2
                .defaultSuccessUrl("/auth/login/success", true)
                .failureUrl("/auth/login/failure")
        )
                .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("http://localhost:5173/login?logout=success")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                )
                .formLogin(form -> form.disable());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*")); // Allow all origins for development
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
