package com.peaje.telepass.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtFilter jwtFilter;

    @SuppressWarnings("deprecation")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeRequests(auth -> auth
                        // Permitir acceso público a estas rutas
                        .requestMatchers("/api/auth/**", "/auth", "/dashboard", "/api/**", "/api/vehiculo-categorias/**", "/login", "/registro",
                                "/css/**", "/Imagenes/**", "/admin/**","/usuarios", "/usuario/**").permitAll()
                        // Requiere rol USER para estas rutas
                        .requestMatchers("/usuario/**").hasRole("USER")
                        // Requiere rol ADMIN para estas rutas
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // Todas las demás rutas requieren autenticación
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /*
    public AuthenticationSuccessHandler successHandler() {
        return (((request, response, authentication) -> {
            response.sendRedirect("/api/usuario");
        }));
    }
*/
}
