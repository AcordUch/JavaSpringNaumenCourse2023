package com.acord.dealweb.controllers;

import com.acord.dealweb.domain.card.Card;
import com.acord.dealweb.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CardController {
  private final CardService cardService;

  @Autowired
  public CardController(CardService cardService) {
    this.cardService = cardService;
  }

  @PostMapping("/api/v1/cards")
  public ResponseEntity<?> add(@RequestBody Card card) {
    cardService.addOrUpdate(card);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/api/v1/cards/{id}")
  public ResponseEntity<Card> get(@PathVariable String id) {
    Card card = cardService.get(id);
    HttpStatus status = card == null ? HttpStatus.NOT_FOUND : HttpStatus.OK;
    return new ResponseEntity<>(card, status);
  }

  @DeleteMapping("/api/v1/cards/{id}")
  public ResponseEntity<?> delete(@PathVariable String id) {
    cardService.delete(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
