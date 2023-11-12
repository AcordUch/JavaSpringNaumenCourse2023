package com.acord.dealweb.repositories;

import com.acord.dealweb.domain.Room;
import com.acord.dealweb.domain.WebUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<WebUser, String> {
  WebUser findByUsername(String username);

  @Query(
      "select ur from WebUser u"
          + " join u.userRooms ur"
          + " where u.username=:username and lower(ur.name) like lower(concat('%', :searchTerm, '%'))")
  List<Room> getRoomsByUsername(String username, String searchTerm);

  @Query("select ur from WebUser u join u.userRooms ur where u.username=:username")
  List<Room> getRoomsByUsername(String username);
}
