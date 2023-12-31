package com.acord.dealweb.services;

import com.acord.dealweb.domain.Room;
import com.acord.dealweb.domain.WebUser;
import com.acord.dealweb.domain.card.Card;
import com.acord.dealweb.repositories.CardRepository;
import com.acord.dealweb.repositories.RoomRepository;
import java.util.List;

import com.acord.dealweb.repositories.UserRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceLive implements RoomService {
  private final RoomRepository roomRepository;
  private final CardRepository cardRepository;
  private final UserRepository userRepository;

  @Autowired
  public RoomServiceLive(
      RoomRepository roomRepository, CardRepository cardRepository, UserRepository userRepository) {
    this.roomRepository = roomRepository;
    this.cardRepository = cardRepository;
    this.userRepository = userRepository;
  }

  @Override
  public void addOrUpdate(Room room) {
    roomRepository.save(room);
  }

  @Override
  public void addCardToRoom(String roomId, Card card) {
    Room room = roomRepository.getReferenceById(roomId);
    room.addCardToRoom(card);
    addOrUpdate(room);
  }

  @Override
  public void addExistCardToRoom(String roomId, String cardId) {
    Room room = roomRepository.findById(roomId).orElse(null);
    Card card = cardRepository.getReferenceById(cardId);
    room.addCardToRoom(card); // TODO: add check on null
    addOrUpdate(room);
  }

  @Override
  public void deleteCardFromRoom(String roomId, Card card) {
    Room room = roomRepository.getReferenceById(roomId);
    room.deleteCardFromRoom(card);
    addOrUpdate(room);
  }

  @Override
  public Room getOne(String id) {
    //    val map = new HashMap<String, Room>();
    //    map.put("1", Room.builder().name("11").uuid("1").build());
    //    map.put("2", Room.builder().name("kek").uuid("2").build());
    //    return map.get(id);
    return roomRepository.getReferenceById(id);
  }

  @Override
  public List<Room> getAll() {
    //    return List.of(
    //        Room.builder().name("11").uuid("1").build(),
    // Room.builder().name("kek").uuid("2").build());
    return roomRepository.findAll();
  }

  @Override
  public List<Room> getAll(String stringFilter) {
    if (stringFilter == null || stringFilter.isEmpty()) {
      return roomRepository.findAll();
    } else {
      return roomRepository.search(stringFilter);
    }
  }

  @Override
  public void delete(String id) {
    userRepository.deleteRoomFromUsers(id);
    roomRepository.deleteById(id);
  }

  @Override
  public List<Card> getRoomCards(String roomId, String filterText) {
    if (filterText == null || filterText.isEmpty()) {
      return roomRepository.getCardsByRoomId(roomId);
    } else {
      return roomRepository.getCardsByRoomId(roomId, filterText);
    }
  }

  @Override
  public List<WebUser> getUsersInRoom(String roomId) {
    return roomRepository.getUsersByRoomId(roomId).stream()
        .map(userRepository::findByUsername)
        .toList();
  }

  @Override
  public Room getRoomByCardId(String cardId) {
    return roomRepository.getRoomByCardId(cardId);
  }
}
