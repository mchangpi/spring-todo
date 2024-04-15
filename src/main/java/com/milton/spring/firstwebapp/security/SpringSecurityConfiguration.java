package com.milton.spring.firstwebapp.security;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfiguration {
  // LDAP or Database
  // In Memory

  // InMemoryUserDetailsManager
  // InMemoryUserDetailsManager(UserDetails... users)

  @Bean
  public InMemoryUserDetailsManager createUserDetailsManager() {
    UserDetails userDetails1 = createNewUser("milton", "milton");
    UserDetails userDetails2 = createNewUser("test", "test");

    return new InMemoryUserDetailsManager(userDetails1, userDetails2);
  }

  private UserDetails createNewUser(String username, String password) {
    Function<String, String> encoder = input -> passwordEncoder().encode(input);

    UserDetails userDetails = User.builder().passwordEncoder(encoder)
        .username(username).password(password).roles("USER", "ADMIN").build();

    return userDetails;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  
  // All URLs are protected, login form is shown for unauthorized requests
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    
    http.authorizeHttpRequests(
        auth -> auth.anyRequest().authenticated());
    http.formLogin(withDefaults());
    
    http.csrf(csrf -> csrf.disable()); // POST or PUT

    // http.headers(headers -> headers.frameOptions(
    //    frameOptionsConfig -> frameOptionsConfig.disable()));
    
    return http.build();
  }

}
