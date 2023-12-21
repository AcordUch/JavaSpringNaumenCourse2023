package com.acord.dealweb.repositories;

import com.acord.dealweb.domain.Room;
import com.acord.dealweb.domain.WebUser;
import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

  @Query(
      "select ufr from WebUser u"
          + " join u.friends ufr"
          + " where u.username=:username and lower(ufr.username) like lower(concat('%', :searchTerm, '%'))")
  List<WebUser> getFriendsByUsername(String username, String searchTerm);

  @Query("select ufr from WebUser u join u.friends ufr where u.username=:username")
  List<WebUser> getFriendsByUsername(String username);

  @Modifying
  @Transactional
  @Query(
      value = "delete from user_rooms as ur where ur.user_rooms_uuid=:roomId",
      nativeQuery = true)
  void deleteRoomFromUsers(String roomId);
}
