package com.acord.dealweb.domain;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserDeposit {
  private String username;
  private Integer deposit;
}
