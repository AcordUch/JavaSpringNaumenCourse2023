package com.acord.dealweb.services;

import com.acord.dealweb.domain.Role;
import com.acord.dealweb.domain.Room;
import com.acord.dealweb.domain.WebUser;
import com.acord.dealweb.domain.exception.AlreadyRegisteredException;
import com.acord.dealweb.repositories.RoomRepository;
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
  private final RoomRepository roomRepository;

  @Autowired
  public UserService(UserRepository userRepository, RoomRepository roomRepository) {
    this.userRepository = userRepository;
    this.roomRepository = roomRepository;
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

  public void update(WebUser user) { // TODO: add check on exist
    userRepository.save(user);
  }

  public void addRoomToUser(String username, Room room) {
    WebUser webUser = userRepository.findByUsername(username);
    webUser.addRoomToUser(room);
    update(webUser);
  }

  public void addExistRoomToUser(String username, String roomId) {
    WebUser webUser = userRepository.findByUsername(username);
    Room room = roomRepository.getReferenceById(roomId);
    webUser.addRoomToUser(room);
    update(webUser);
  }

  public void deleteRoomFromUser(String username, Room room) { // TODO: rework
    WebUser webUser = userRepository.findByUsername(username);
    webUser.deleteRoomFromUser(room);
    update(webUser);
  }

  public List<Room> getUserRooms(String username, String filter) {
    if (filter == null || filter.isEmpty()) {
      return userRepository.getRoomsByUsername(username);
    } else {
      return userRepository.getRoomsByUsername(username, filter);
    }
  }
}
