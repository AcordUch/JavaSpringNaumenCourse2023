package com.acord.dealweb.controllers;

import com.acord.dealweb.domain.Room;
import com.acord.dealweb.domain.WebUser;
import com.acord.dealweb.domain.card.Card;
import com.acord.dealweb.services.RoomService;
import jakarta.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoomController {
  private final RoomService roomService;

  @Autowired
  public RoomController(RoomService roomService) {
    this.roomService = roomService;
  }

  @PostMapping("/api/v1/rooms")
  public ResponseEntity<?> add(@RequestBody Room room) {
    roomService.addOrUpdate(room);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/api/v1/rooms?filterText={filterText}")
  @ResponseBody
  public ResponseEntity<List<Room>> getAll(@PathVariable String filterText) {
    return new ResponseEntity<>(roomService.getAll(filterText), HttpStatus.OK);
  }

  @GetMapping(value = "/api/v1/rooms/{id}")
  @ResponseBody
  public ResponseEntity<Optional<Room>> getOne(@PathVariable String id) {
    return new ResponseEntity<>(Optional.ofNullable(roomService.getOne(id)), HttpStatus.OK);
  }

  @PutMapping("/api/v1/rooms")
  public ResponseEntity<?> update(@RequestBody Room room) {
    roomService.addOrUpdate(room);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("api/v1/rooms/{id}")
  public ResponseEntity<?> delete(@PathVariable String id) {
    roomService.delete(id); // TODO: it is wrong, remade
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/api/v1/rooms/{id}/card")
  public ResponseEntity<?> addCardToRoom(@PathVariable String id, @RequestBody Card card) {
    roomService.addCardToRoom(id, card);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/api/v1/rooms/{roomId}/card/{cardId}")
  public ResponseEntity<?> addExistCardToRoom(
      @PathVariable String roomId, @PathVariable String cardId) {
    roomService.addExistCardToRoom(roomId, cardId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("/api/v1/rooms/{id}/card")
  public ResponseEntity<?> deleteCardFromRoom(@PathVariable String id, @RequestBody Card card) {
    roomService.deleteCardFromRoom(id, card);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/api/v1/rooms/{cardId}/cards?filterText={filterText}")
  public ResponseEntity<List<Card>> getRoomCards(
      @PathVariable String roomId, @PathVariable @Nullable String filterText) {
    val cards = roomService.getRoomCards(roomId, filterText);
    return new ResponseEntity<>(cards, HttpStatus.OK);
  }

  @GetMapping("/api/v1/rooms/{roomId}/users")
  public ResponseEntity<List<WebUser>> getUsersInRoom(@PathVariable String roomId) {
    return new ResponseEntity<>(roomService.getUsersInRoom(roomId), HttpStatus.OK);
  }

  @GetMapping("/api/v1/rooms/card/{cardId}")
  public ResponseEntity<Room> getRoomByCardId(@PathVariable String cardId) {
    return new ResponseEntity<>(roomService.getRoomByCardId(cardId), HttpStatus.OK);
  }
}
