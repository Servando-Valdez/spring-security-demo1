package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    //configuration one
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity
//                .authorizeHttpRequests()
//                    .requestMatchers("v1/index2").permitAll()
//                    .anyRequest().authenticated()
//                .and()
//                .formLogin().permitAll()
//                .and()
//                .build();
//    }

    //configuration 2
    //configurate with lamda function
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("v1/index2").permitAll();
                    auth.anyRequest().authenticated(succesHandler());
                })
                .formLogin()
                    .successHandler() //URL where redirect after login
                    .permitAll()
                .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) //ALWAYS - IF_REQUIRED - NEVER -STATELESS
                    .invalidSessionUrl("/login")
                    .maximumSessions(1)
                    .expiredUrl("/login")
                    .sessionRegistry(SessionRegistry())
                .and()
                .sessionFixation()
                //migrateSession, proteccion de sesiones, genera otros id de sesion
                // newSession, hace lo mismo que el migrate pero no copia los datos
                //none, no recomendable, inabilidta todo, la recomendada es migrateSession
                    .migrateSession()
                .and()
                .build();
    }
}

@Bean
public SessionRegistry sessionRegisty(){
    //rastreo de los datos del usuario que se ha registrado
    return new SessionRegistryImpl();
}

public AuthenticationSuccessHandler succesHandler(){
    return ((request, response, authentication)->{
        response.sendRedirect("/v1/session");
    });
}
