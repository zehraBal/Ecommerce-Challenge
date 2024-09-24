package com.workintech.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(UserDetailsService userDetailsService){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/welcome/**").permitAll();
                    auth.requestMatchers("/auth/**").permitAll();
                    auth.requestMatchers("/cart/**").permitAll();
                    auth.requestMatchers("/payment/**").permitAll();
                    auth.requestMatchers("/order/**").permitAll();
                    auth.requestMatchers("/product/**").permitAll();
                    auth.requestMatchers("/category/**").permitAll();;
                    auth.anyRequest().authenticated();
                })
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .build();
    }

}


//csrf => Cross Site resource forgery

//Authentication(Sisteme Login olmak, Giriş yapma) ve Authorization(Yetkilendirme-Rol based)

//STEP1: pom.xml security dependency eklenmeli
//STEP2: Veritabanı üzerinde user ve role tanımlanıp ilgili entityler oluşturulmalı
//STEP3: Security Config üzerinde SecurityFilterChain, passwordEncoder, authManager 3 ünün tanımlanması
//STEP4: UserRepository ve RoleRepository tanımla. İhtiyaç duyduğun metodları yaz.
//UserService with loadUserByUsername method(login)
//STEP5: AuthenticationService de register methodunu tanımlamak
//STEP6: SecurityFilterChain'i register a açmak
//STEP7: Endpointleri yazmak(/auht/register)
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource(){
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.setAllowedOrigins(Arrays.asList("https://cdpn.io"));
//        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
//        corsConfiguration.setAllowedHeaders(Arrays.asList(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", corsConfiguration);
//        return source;
//    }