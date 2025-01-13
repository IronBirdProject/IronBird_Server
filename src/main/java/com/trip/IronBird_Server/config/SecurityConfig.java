package com.trip.IronBird_Server.config;

import com.trip.IronBird_Server.jwt.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring()

                //Error EndPoint를 열어줘야 됌.
                //favicon.ico 추가
                .requestMatchers("/error","/favicon.ico");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception{
        // Authentication and Authorization Configuration
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers((AllowedUrls.allowUrls)).permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable) //csrf disable
                .cors(AbstractHttpConfigurer::disable) //cors disable
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout((logout) -> logout.permitAll()) //http Basic certification disable


        // JwtAuthFilter를 UsernamePasswordAuthenticationFilter 전에 추가
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
