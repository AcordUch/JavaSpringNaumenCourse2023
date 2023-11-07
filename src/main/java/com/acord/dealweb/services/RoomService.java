package com.acord.dealweb.services;

import com.acord.dealweb.domain.Room;
import java.util.List;

public interface RoomService {
  void addOrUpdate(Room room);

  Room getOne(String id);

  List<Room> getAll();

  void delete(String id);
}
