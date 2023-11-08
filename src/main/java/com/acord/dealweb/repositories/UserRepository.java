package com.acord.dealweb.repositories;

import com.acord.dealweb.domain.WebUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<WebUser, String> {
  WebUser findByUsername(String username);
}
