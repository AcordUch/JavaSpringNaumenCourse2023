package com.acord.dealweb.services;

import com.acord.dealweb.domain.Role;
import com.acord.dealweb.domain.WebUser;
import com.acord.dealweb.domain.exception.AlreadyRegisteredException;
import com.acord.dealweb.repositories.UserRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserService implements UserDetailsService {
  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    WebUser webUser = userRepository.findByUsername(username);
    if (webUser == null) {
      throw new UsernameNotFoundException("User doesn't exist!");
    }
    return new User(
        webUser.getUsername(),
        webUser.getPassword(),
        mapRolesToAuthority(Set.of(webUser.getRole())));
  }

  private List<? extends GrantedAuthority> mapRolesToAuthority(Set<Role> roles) {
    return roles.stream()
        .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
        .collect(Collectors.toList());
  }

  public void addUser(WebUser user) throws AlreadyRegisteredException {
    WebUser webUserFromDb = userRepository.findByUsername(user.getUsername());
    if (webUserFromDb != null) {
      throw new AlreadyRegisteredException();
    }
    user.setRole(Role.USER);
    userRepository.save(user);
  }
}
