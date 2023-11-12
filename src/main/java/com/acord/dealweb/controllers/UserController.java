package com.acord.dealweb.controllers;

import com.acord.dealweb.domain.Room;
import com.acord.dealweb.services.UserService;
import jakarta.annotation.Nullable;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/api/v1/users/{username}/room")
  public ResponseEntity<?> addRoomToUser(@PathVariable String username, @RequestBody Room room) {
    userService.addRoomToUser(username, room);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * @param username in null case taken from security context
   * @param roomId room uuid
   * @return Empty response
   */
  @PostMapping("/api/v1/users/room/{roomId}?username={username}")
  public ResponseEntity<?> addExistRoomToUser(
      @PathVariable String username, @PathVariable String roomId) {
    if (username == null) {
      username = SecurityContextHolder.getContext().getAuthentication().getName();
    }
    userService.addExistRoomToUser(username, roomId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("/api/v1/users/{username}/room")
  public ResponseEntity<?> deleteRoomFromUser(
      @PathVariable String username, @RequestBody Room room) {
    userService.deleteRoomFromUser(username, room);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  /**
   * @param username in null case taken from security context
   * @param filterText fiter by room name
   * @return List of users rooms
   */
  @GetMapping("/api/v1/users/rooms?username={username}&filterText={filterText}")
  public ResponseEntity<List<Room>> getUserRooms(
      @PathVariable @Nullable String username, @PathVariable @Nullable String filterText) {
    if (username == null) {
      username = SecurityContextHolder.getContext().getAuthentication().getName();
    }
    List<Room> rooms = userService.getUserRooms(username, filterText);
    return new ResponseEntity<>(rooms, HttpStatus.OK);
  }
}
