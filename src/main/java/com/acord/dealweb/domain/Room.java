package com.acord.dealweb.domain;

import jakarta.persistence.*;
import lombok.*;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String uuid;
    private String name;
    private String password;

    @OneToMany
    @JoinColumn(name = "UUID")
    private List<Card> cardsInRoom;
}
