package com.acord.dealweb.repositories;

import com.acord.dealweb.domain.card.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, String> { }
