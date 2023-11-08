package com.acord.dealweb.domain.card;

import jakarta.persistence.*;
import java.util.HashMap;
import java.util.Map;
import lombok.*;

@Entity
@Table(name = "cards_accumulate")
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CardAccumulate {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Getter @Setter
  private String id;

  @Getter @Setter private Long goal = 0L;

  @ElementCollection(fetch=FetchType.EAGER)
  @MapKeyColumn(name = "username")
  @CollectionTable(
      name = "accumulate_fraction",
      joinColumns = @JoinColumn(name = "card_accumulate_id"))
  @Getter @Setter
  private Map<String, Long> accumulateFraction = new HashMap<>();

  public Long totalAccumulate() {
    return accumulateFraction.values().stream().reduce(Long::sum).orElse(0L);
  }

  public void clearAccumulate() {
    accumulateFraction.clear();
  }

  public void increase(String username, Long value) {
    Long currentValue = accumulateFraction.get(username);
    accumulateFraction.replace(username, currentValue + value);
  }

  public void reduce(String username, Long value) {
    Long currentValue = accumulateFraction.get(username);
    accumulateFraction.replace(username, currentValue - value);
  }
}
