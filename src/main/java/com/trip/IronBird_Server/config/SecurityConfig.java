package com.trip.IronBird_Server.config;

import com.trip.IronBird_Server.oauth.handler.CustomAuthExceptionHandler;
import com.trip.IronBird_Server.oauth.handler.CustomOAuth2SuccessHandler;
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

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {


    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    private final CustomAuthExceptionHandler customAuthExceptionHandler;


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
                .formLogin((form)-> form
                        .loginPage("/login")
                        .permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable) //csrf disable
                .cors(AbstractHttpConfigurer::disable) //cors disable
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout((logout) -> logout.permitAll()); //http Basic certification disable

        http.oauth2Login((config)-> config
                .successHandler(customOAuth2SuccessHandler)
                .failureHandler(customAuthExceptionHandler));


        return http.build();
    }




    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
