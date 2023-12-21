package com.acord.dealweb.services;

import com.acord.dealweb.domain.card.Card;
import com.acord.dealweb.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CardServiceLive implements CardService {
  private final CardRepository cardRepository;

  @Autowired
  public CardServiceLive(CardRepository cardRepository) {
    this.cardRepository = cardRepository;
  }

  @Override
  public void addOrUpdate(Card card) {
    cardRepository.save(card);
  }

  @Override
  public Card get(String id) {
    return cardRepository.findById(id).orElse(null);
  }

  @Override
  public void delete(String id) {
    cardRepository.deleteById(id);
  }
}
