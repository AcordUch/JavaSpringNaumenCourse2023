package com.acord.dealweb.services;

import com.acord.dealweb.domain.Room;

import java.util.List;

public interface RoomService {
    void add(Room room);
    Room getOne(String id);
    List<Room> getAll();
    void update(String id, Room room);
    void delete(String id);
}
