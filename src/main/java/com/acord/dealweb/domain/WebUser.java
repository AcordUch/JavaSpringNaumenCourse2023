package com.acord.dealweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "web_users")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class WebUser {
  @Id private String username;
  private String password;
  private String firstName;
  private String surname;
  private Role role;

  @OneToMany(fetch = FetchType.EAGER)
  @CollectionTable(name = "user_rooms", joinColumns = @JoinColumn(name = "user_id"))
  private List<Room> userRooms = new ArrayList<>();

  @ManyToMany(fetch = FetchType.EAGER)
  @CollectionTable(name = "user_friends", joinColumns = @JoinColumn(name = "user_id"))
  private List<WebUser> friends = new ArrayList<>();

  public void addRoomToUser(Room room) {
    userRooms.add(room);
  }

  public void deleteRoomFromUser(Room room) {
    userRooms.remove(room);
  }

  public void addFriend(WebUser friend) {
    friends.add(friend);
  }

  public void deleteFriend(String friendUsername) {
    friends.removeIf(fr -> fr.username.equals(friendUsername));
  }
}
