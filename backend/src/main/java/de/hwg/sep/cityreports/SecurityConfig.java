package de.hwg.sep.cityreports;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SCAFFOLD-STAGE security. The real public/staff split (below) will be enforced
 * once JWT auth is implemented. For now every endpoint is permitted so the skeleton
 * runs and the Android/Web teams can call the stubbed endpoints during parallel dev.
 *
 * Target contract (to enforce later):
 *   PUBLIC  — POST /api/reports, POST/GET /api/reports/{id}/photo, POST /api/auth/login
 *   STAFF   — GET /api/reports, GET/PUT/DELETE /api/reports/{id}
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Stateless JSON API: CSRF tokens don't apply.
                .csrf(csrf -> csrf.disable())
                // H2 console renders in a frame during local dev.
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints (already final per the contract):
                        .requestMatchers(HttpMethod.POST, "/api/reports").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/reports/*/photo").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/reports/*/photo").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        // TODO: tighten to .authenticated() / .hasRole("STAFF") once JWT auth exists.
                        .anyRequest().permitAll()
                );
        return http.build();
    }

    /** BCrypt is how we hash staff passwords (matches User.passwordHash). */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
