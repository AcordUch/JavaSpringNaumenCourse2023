package com.acord.dealweb;

import com.acord.dealweb.domain.Role;
import com.acord.dealweb.domain.WebUser;
import com.acord.dealweb.repositories.UserRepository;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(
      ApplicationContext ctx, @Autowired UserRepository userRepository) {
    userRepository.save(
        WebUser.builder().username("admin").password("admin").role(Role.ADMIN).build());
    userRepository.save(
        WebUser.builder().username("user").password("user").role(Role.USER).build());
    return args -> {
      System.out.println("Let's inspect the beans provided by Spring Boot:");

      String[] beanNames = ctx.getBeanDefinitionNames();
      Arrays.sort(beanNames);
      for (String beanName : beanNames) {
        System.out.println(beanName);
      }
    };
  }
}
