package com.acord.dealweb.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
            (authorize) ->
                authorize
                    .requestMatchers(
                        AntPathRequestMatcher.antMatcher("/swagger-ui/**"),
                        AntPathRequestMatcher.antMatcher("/v3/api-docs/**"),
                        AntPathRequestMatcher.antMatcher("/api/v1/rooms"))
                    .hasRole("ADMIN")
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/api/**"))
                    .authenticated()
                    .requestMatchers(
                        AntPathRequestMatcher.antMatcher("/registration"),
                        AntPathRequestMatcher.antMatcher("/signup"),
                        AntPathRequestMatcher.antMatcher("/login"),
                        AntPathRequestMatcher.antMatcher("/logout"),
                        AntPathRequestMatcher.antMatcher("/ui/**"),
                        AntPathRequestMatcher.antMatcher("/"))
                    .permitAll()
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/**"))
                    .hasRole("ADMIN"))
        .formLogin(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable);
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return NoOpPasswordEncoder.getInstance();
  }

  //  @Bean
  //  public UserDetailsService getUsers() {
  //    System.out.println("hi1");
  //    UserDetails user = User.builder().username("user").password("user").roles("USER").build();
  //    UserDetails admin =
  // User.builder().username("admin").password("admin").roles("ADMIN").build();
  //    return new InMemoryUserDetailsManager(user, admin);
  //  }
}
