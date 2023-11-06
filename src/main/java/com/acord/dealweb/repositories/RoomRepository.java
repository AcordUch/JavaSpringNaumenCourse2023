package com.acord.dealweb.repositories;

import com.acord.dealweb.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, String> {
}
