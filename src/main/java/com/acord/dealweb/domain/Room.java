package com.acord.dealweb.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String uuid;

  private String name;
  private String password;

  @OneToMany
  //    @JoinColumn(name = "UUID")
  //    @ElementCollection(targetClass = Card.class, fetch = FetchType.EAGER)
  @CollectionTable(name = "room_cards", joinColumns = @JoinColumn(name = "room_id"))
  private List<Card> cardsInRoom = new ArrayList<>();

  public void addCardToRoom(Card card) {
    cardsInRoom.add(card);
  }

  public void deleteCardFromRoom(Card card) {
    cardsInRoom.remove(card);
  }
}
