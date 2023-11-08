package com.acord.dealweb.domain;

import com.acord.dealweb.domain.card.Card;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Room {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String uuid;

  private String name;
  private String password;

  @OneToMany(fetch=FetchType.EAGER)
  @CollectionTable(name = "room_cards", joinColumns = @JoinColumn(name = "room_id"))
  private List<Card> cardsInRoom = new ArrayList<>();

  public void addCardToRoom(Card card) {
    cardsInRoom.add(card);
  }

  public void deleteCardFromRoom(Card card) {
    cardsInRoom.remove(card);
  }
}
