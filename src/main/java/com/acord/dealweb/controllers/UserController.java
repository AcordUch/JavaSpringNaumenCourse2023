package com.acord.dealweb.controllers;

import com.acord.dealweb.domain.Room;
import com.acord.dealweb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

  @PostMapping("/api/v1/users/{username}/room/{roomId}")
  public ResponseEntity<?> addExistRoomToUser(
      @PathVariable String username, @PathVariable String roomId) {
    userService.addExistRoomToUser(username, roomId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("/api/v1/users/{username}/room")
  public ResponseEntity<?> deleteRoomFromUser(
      @PathVariable String username, @RequestBody Room room) {
    userService.deleteRoomFromUser(username, room);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
