package com.acord.dealweb.repositories;

import com.acord.dealweb.domain.Room;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends JpaRepository<Room, String> {
  @Query("select r from Room r where lower(r.name) like lower(concat('%', :searchTerm, '%')) ")
  List<Room> search(@Param("searchTerm") String searchTerm);
}
