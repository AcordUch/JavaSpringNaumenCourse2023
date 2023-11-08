package com.acord.dealweb.services;

import com.acord.dealweb.domain.card.Card;

public interface CardService {
    void addOrUpdate(Card card);
    Card get(String id);
    void delete(String id);
}
