package com.acord.dealweb.controllers;

import com.acord.dealweb.domain.card.Card;
import com.acord.dealweb.services.CardService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
  public ResponseEntity<Optional<Card>> get(@PathVariable String id) {
    val card = Optional.ofNullable(cardService.get(id));
    HttpStatus status = card.isEmpty() ? HttpStatus.NOT_FOUND : HttpStatus.OK;
    return new ResponseEntity<>(card, status);
  }

  @DeleteMapping("/api/v1/cards/{id}")
  public ResponseEntity<?> delete(@PathVariable String id) {
    cardService.delete(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/api/v1/cards/{cardId}/deposit/{deposit}?username={username}")
  public ResponseEntity<?> updateDeposit(
      @PathVariable String cardId, @PathVariable Integer deposit, @PathVariable String username) {
    if (username == null) {
      username = SecurityContextHolder.getContext().getAuthentication().getName();
    }
    val card = cardService.get(cardId);
    card.increaseDeposit(username, deposit);
    cardService.addOrUpdate(card);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
