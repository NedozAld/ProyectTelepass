package com.peaje.telepass.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecutiryConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/", "/registro", "/login", "/recuperarEmail", "/css/**", "/js/**", "/Imagenes/**", "/usuarios", "/sesion", "/api/recuperar-email", "/cambiar-contrasena", "/sesion/iniciar", "/sesion/cerrar", "/api/usuario/**").permitAll();
                    auth.anyRequest().authenticated();
                })
                .formLogin(form -> form
                        .loginPage("/login")
                        //.successHandler(successHandler())
                        .permitAll())
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
                    //session.invalidSessionUrl("/login");


                })
                .build();
    }
    /*
    public AuthenticationSuccessHandler successHandler() {
        return (((request, response, authentication) -> {
            response.sendRedirect("/api/usuario");
        }));
    }
*/
}
