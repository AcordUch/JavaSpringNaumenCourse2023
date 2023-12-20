package com.acord.dealweb.services;

import com.acord.dealweb.domain.Room;
import com.acord.dealweb.domain.card.Card;
import java.util.List;

public interface RoomService {
  void addOrUpdate(Room room);

  void addCardToRoom(String roomId, Card card);

  void addExistCardToRoom(String roomId, String cardId);

  void deleteCardFromRoom(String roomId, Card card);

  Room getOne(String id);

  List<Room> getAll();

  List<Room> getAll(String filterText);

  void delete(String id);

  List<Card> getRoomCards(String roomId, String filterText);
}
