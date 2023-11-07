package com.acord.dealweb.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "web_users")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebUser {
  //  @Id
  //  @GeneratedValue(strategy = GenerationType.UUID)
  //  private String id;

  @Id private String username;
  private String password;
  private String firstName;
  private String surname;
  private Role role;
}
