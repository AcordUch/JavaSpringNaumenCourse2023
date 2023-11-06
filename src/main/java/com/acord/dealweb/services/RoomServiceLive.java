package com.acord.dealweb.services;

import com.acord.dealweb.domain.Room;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoomServiceLive implements RoomService {
    @Override
    public void add(Room room) {

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
        return null;
    }

    @Override
    public void update(String id, Room room) {

    }

    @Override
    public void delete(String id) {

    }
}
