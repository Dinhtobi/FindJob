package com.pblintern.web.Configs;

import com.pblintern.web.Security.Authentication.GoogleEntrypoint;
import com.pblintern.web.Security.Authentication.GoogleFilter;
import com.pblintern.web.Security.Authentication.GoogleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@ComponentScan("com.pblintern.web.security.Authentication")
public class WebConfig implements WebMvcConfigurer{

    @Autowired
    private GoogleEntrypoint googleEntrypoint;

    @Autowired
    private GoogleProvider googleProvider;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

//        http.cors(AbstractHttpConfigurer::disable)
//                .csrf(AbstractHttpConfigurer::disable)
//                .exceptionHandling(exception -> exception.authenticationEntryPoint(googleEntrypoint))
//                .authorizeHttpRequests(auth -> {
//            auth.requestMatchers("/account/**").permitAll();
//            auth.requestMatchers("/seeker/**").hasRole("SEEKER").anyRequest().authenticated();
//                })
////                .authenticationProvider(provider)
//                .addFilterBefore(new GoogleFilter(), BasicAuthenticationFilter.class);


        http.cors(AbstractHttpConfigurer::disable);		// Kích hoạt CORS
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeRequests().requestMatchers("/account/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll();
        http.authorizeRequests().requestMatchers("/**").authenticated().anyRequest().authenticated();
        http.exceptionHandling().authenticationEntryPoint(googleEntrypoint);
        http.addFilterBefore(new GoogleFilter(), BasicAuthenticationFilter.class);
        http.authenticationProvider(googleProvider);
        return   http.build();
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://127.0.0.1:5500" , "http://127.0.0.1:3000" , "http://localhost:3000", "https://go-webapp.vercel.app", "https://forlorn-bite-production.up.railway.app", "http://forlorn-bite-production.up.railway.app")
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "PATCH")
                .maxAge(-1)   // add maxAge
                .allowCredentials(false);
    }
}
