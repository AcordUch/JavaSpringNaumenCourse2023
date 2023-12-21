package com.acord.dealweb.repositories;

import com.acord.dealweb.domain.Room;
import com.acord.dealweb.domain.WebUser;
import com.acord.dealweb.domain.card.Card;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends JpaRepository<Room, String> {
  @Query("select r from Room r where lower(r.name) like lower(concat('%', :searchTerm, '%')) ")
  List<Room> search(@Param("searchTerm") String searchTerm);

  @Query(
      "select rc from Room r"
          + " join r.cardsInRoom rc"
          + " where r.uuid=:uuid and lower(rc.name) like lower(concat('%', :searchTerm, '%'))")
  List<Card> getCardsByRoomId(String uuid, String searchTerm);

  @Query("select rc from Room r join r.cardsInRoom rc where r.uuid=:uuid")
  List<Card> getCardsByRoomId(String uuid);

  //  @Query(
  //      value =
  //          "select u.role, u.first_name, u.password, u.surname, u.username "
  //              + "from web_users as u "
  //              + "join user_rooms ur on u.username = ur.user_id "
  //              + "where ur.user_rooms_uuid=:uuid",
  //      nativeQuery = true)
  //  List<WebUser> getUsersByRoomId(String uuid);
  @Query(
      value = "select ur.user_id from user_rooms ur where ur.user_rooms_uuid=:uuid",
      nativeQuery = true)
  List<String> getUsersByRoomId(String uuid);

  @Query("select r from Room r join r.cardsInRoom cr where cr.id=:cardId")
  Room getRoomByCardId(String cardId);
}
