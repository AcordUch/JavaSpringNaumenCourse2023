package com.acord.dealweb.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
}
