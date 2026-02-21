package com.products_service.configuration;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.Customizer;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.web.SecurityFilterChain;

// @Configuration
// @EnableWebSecurity
// public class authConfiguration {

//     @Bean
//     SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//                 .csrf(csrf -> csrf.disable())
//                 .authorizeHttpRequests(auth -> auth
//                         .anyRequest().permitAll())
//                 .headers(headers -> headers
//                         .frameOptions(frame -> frame.sameOrigin()));

//         return http.build();
//     }

//     @Configuration
//     public class H2SecurityBypassConfig {

//         @Bean
//         WebSecurityCustomizer webSecurityCustomizer() {
//             return web -> web.ignoring()
//                     .requestMatchers("/h2-console/**");
//         }
//     }
// }