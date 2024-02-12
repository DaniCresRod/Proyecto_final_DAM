package com.example.mySchedule.config;

import com.example.mySchedule.jwt.AuthTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration                  //Indica que se trata de un archivo de configuracion
@EnableWebSecurity              //Indica que usaremos Spring Security
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final AuthTokenFilter authTokenFilter;

    //Cadena de filtros antes de acceder
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //Para hacer loggin
        http
                .csrf(csrf -> csrf.disable())                                   //Eliminar la proteccion csrf (si no, no consigo que acepte POST)
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                        .requestMatchers(HttpMethod.POST,"/auth").permitAll()
                        //.anyRequest().permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());

        //Para iniciar la sesion propiamente
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sessionManager->
                        sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
