package com.acord.dealweb.services;

import com.acord.dealweb.domain.Room;
import com.acord.dealweb.repositories.RoomRepository;
import java.util.HashMap;
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceLive implements RoomService {
  private final RoomRepository roomRepository;

  @Autowired
  public RoomServiceLive(RoomRepository roomRepository) {
    this.roomRepository = roomRepository;
  }

  @Override
  public void addOrUpdate(Room room) {
    roomRepository.save(room);
  }

  @Override
  public Room getOne(String id) {
    val map = new HashMap<String, Room>();
    map.put("1", Room.builder().name("11").uuid("1").build());
    map.put("2", Room.builder().name("kek").uuid("2").build());
    return map.get(id);
  }

  @Override
  public List<Room> getAll() {
    return List.of(
        Room.builder().name("11").uuid("1").build(), Room.builder().name("kek").uuid("2").build());
  }

  @Override
  public void delete(String id) {
    roomRepository.deleteById(id);
  }
}
