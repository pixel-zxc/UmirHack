package org.bacljan.product_monitoring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/demo/**", "/api/events").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/me").hasAnyRole("ADMIN", "KITCHEN", "WAITER")
                        .requestMatchers(HttpMethod.POST, "/api/products", "/api/dishes", "/api/shipment",
                                "/api/stocks/write-off-expired").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/analytics").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/orders").hasAnyRole("ADMIN", "KITCHEN", "WAITER")
                        .requestMatchers(HttpMethod.PATCH, "/api/orders/*/complete").hasAnyRole("ADMIN", "KITCHEN")
                        .requestMatchers(HttpMethod.GET,
                                "/api/dishes",
                                "/api/stoplist",
                                "/api/stoplist/**",
                                "/api/hot-offers"
                        ).hasAnyRole("ADMIN", "KITCHEN", "WAITER")
                        .requestMatchers(HttpMethod.GET, "/api/orders")
                        .hasAnyRole("ADMIN", "KITCHEN", "WAITER")
                        .requestMatchers(HttpMethod.GET,
                                "/api/products",
                                "/api/stocks",
                                "/api/stocks/**"
                        ).hasAnyRole("ADMIN", "KITCHEN")
                        .requestMatchers("/api/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(401);
                            response.setCharacterEncoding("UTF-8");
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\":\"Неверный логин или пароль\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(403);
                            response.setCharacterEncoding("UTF-8");
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\":\"Недостаточно прав\"}");
                        })
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public UserDetailsService users(PasswordEncoder encoder) {
        return new InMemoryUserDetailsManager(
                User.withUsername("admin")
                        .password(encoder.encode("admin"))
                        .roles("ADMIN")
                        .build(),
                User.withUsername("kitchen")
                        .password(encoder.encode("kitchen"))
                        .roles("KITCHEN")
                        .build(),
                User.withUsername("waiter")
                        .password(encoder.encode("waiter"))
                        .roles("WAITER")
                        .build()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
