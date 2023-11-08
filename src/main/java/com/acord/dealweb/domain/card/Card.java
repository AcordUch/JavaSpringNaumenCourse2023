package com.acord.dealweb.domain.card;

import com.acord.dealweb.domain.CardType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.Duration;
import java.time.LocalTime;
import lombok.*;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "cards")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Card {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String name;
  private String Description;

  @Column(nullable = false)
  private final CardType type = CardType.Accumulate;

  @Column(nullable = false)
  private LocalTime fromTime = LocalTime.now();

  @Column(nullable = false)
  private LocalTime toTime = LocalTime.now().plus(Duration.ofHours(1));

  @OneToOne(fetch=FetchType.EAGER)
  @Cascade(org.hibernate.annotations.CascadeType.ALL)
  @PrimaryKeyJoinColumn()
  private CardAccumulate accumulate = new CardAccumulate();
}
