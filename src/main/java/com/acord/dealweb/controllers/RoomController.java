package com.acord.dealweb.controllers;

import com.acord.dealweb.domain.Room;
import com.acord.dealweb.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RoomController {
    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/api/v1/rooms")
    public ResponseEntity<?> add(@RequestBody Room room) {
        roomService.add(room);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/api/v1/rooms")
    @ResponseBody
    public ResponseEntity<List<Room>> getAll() {
        return new ResponseEntity<>(roomService.getAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/api/v1/rooms/{id}")
    @ResponseBody
    public ResponseEntity<Room> getOne(@PathVariable String id) {
        return new ResponseEntity<>(roomService.getOne(id), HttpStatus.OK);
    }

    @PutMapping("/api/v1/rooms/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Room room) {
        roomService.update(id, room);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("api/v1/rooms/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        roomService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
