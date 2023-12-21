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
  public CardAccumulate(String cardId, int goal) {
    this.id = cardId;
    this.goal = goal;
  }

  @Id @Getter @Setter private String id;

  @Getter @Setter private int goal;

  @ElementCollection(fetch = FetchType.EAGER)
  @MapKeyColumn(name = "username")
  @CollectionTable(
      name = "accumulate_fraction",
      joinColumns = @JoinColumn(name = "card_accumulate_id"))
  @Getter
  @Setter
  private Map<String, Integer> accumulateFraction = new HashMap<>();

  public int totalAccumulate() {
    return accumulateFraction.values().stream().reduce(Integer::sum).orElse(0);
  }

  public void clearAccumulate() {
    accumulateFraction.clear();
  }

  public void increase(String username, int value) {
    if (accumulateFraction.containsKey(username)) {
      int currentValue = accumulateFraction.get(username);
      accumulateFraction.replace(username, currentValue + value);
    } else {
      accumulateFraction.put(username, value);
    }
  }

  public void reduce(String username, int value) {
    if (accumulateFraction.containsKey(username)) {
      int currentValue = accumulateFraction.get(username);
      accumulateFraction.replace(username, currentValue - value);
    } else {
      accumulateFraction.put(username, -value);
    }
  }
}
