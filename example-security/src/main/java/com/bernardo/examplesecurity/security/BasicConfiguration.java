package com.bernardo.examplesecurity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class BasicConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers("/user").hasAnyRole("USER_ROLE", "ADMIN_ROLE")
                                .requestMatchers("/admin").hasRole("ADMIN_ROLE")
                                .anyRequest().permitAll()
                        )
                .httpBasic(Customizer.withDefaults());


        return http.build();

    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {

        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        UserDetails user = User
                .withUsername("user")
                .password(passwordEncoder.encode("password"))
                .roles("USER_ROLE")
                .build();

        UserDetails admin = User
                .withUsername("admin")
                .password(passwordEncoder.encode("password_admin"))
                .roles("ADMIN_ROLE")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

}
