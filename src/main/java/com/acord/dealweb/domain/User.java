package com.acord.dealweb.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "web_users")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String username;
  private String firstName;
  private String surname;
  private String login;
  private String password;
  private Role role;
}
