//package com.workintech.ecommerce.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")  // Tüm endpoint'ler için
//                .allowedOrigins("http://localhost:5173")  // Frontend adresiniz
//                .allowedMethods("GET", "POST", "PUT", "DELETE")  // İzin verilen HTTP metodları
//                .allowedHeaders("*");
//               // .allowCredentials(true);  // Kimlik bilgileri (cookies)
//    }
//}
