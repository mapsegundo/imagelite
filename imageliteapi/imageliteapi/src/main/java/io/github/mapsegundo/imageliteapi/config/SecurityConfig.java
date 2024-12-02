package io.github.mapsegundo.imageliteapi.config;

import io.github.mapsegundo.imageliteapi.application.jwt.JwtService;
import io.github.mapsegundo.imageliteapi.config.filter.JwtFilter;
import io.github.mapsegundo.imageliteapi.domain.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Classe de configuração de segurança para a aplicação.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public JwtFilter jwtFilter(JwtService jwtService, UserService userService) {
        return new JwtFilter(jwtService, userService);
    }

    /**
     * Define um bean para o codificador de senhas usando BCrypt.
     *
     * @return uma instância de PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Define a cadeia de filtros de segurança para a aplicação.
     *
     * @param httpSecurity o objeto HttpSecurity para configurar
     * @return a cadeia de filtros de segurança configurada
     * @throws Exception se ocorrer um erro durante a configuração
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtFilter jwtFilter) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configure(httpSecurity))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/v1/users/**").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "v1/images/**").permitAll();
                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Define a fonte de configuração CORS para a aplicação.
     *
     * @return a fonte de configuração CORS configurada
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
        UrlBasedCorsConfigurationSource cors = new UrlBasedCorsConfigurationSource();
        cors.registerCorsConfiguration("/**", config);

        return cors;
    }
}