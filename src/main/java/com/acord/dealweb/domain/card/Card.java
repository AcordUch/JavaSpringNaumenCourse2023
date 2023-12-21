package com.acord.dealweb.domain.card;

import com.acord.dealweb.domain.CardType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

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
  @Id private String id = UUID.randomUUID().toString();

  private String name;
  private String description;
  private int goal;

  @Column(nullable = false)
  private final CardType type = CardType.Accumulate;

  @Column(nullable = false)
  private Instant fromTime = Instant.now();

  @Column(nullable = false)
  private Instant toTime = Instant.now().plus(Duration.ofHours(1));

  @OneToOne(fetch = FetchType.EAGER)
  @Cascade(org.hibernate.annotations.CascadeType.ALL)
  @PrimaryKeyJoinColumn()
  private CardAccumulate accumulate = new CardAccumulate(this.id, this.goal);

  public void setGoal(int goal) {
    this.goal = goal;
    accumulate.setGoal(goal);
  }

  public int getGoal() {
    return accumulate.getGoal();
  }

  public void increaseDeposit(String username, int deposit){
    accumulate.increase(username, deposit);
  }
}
