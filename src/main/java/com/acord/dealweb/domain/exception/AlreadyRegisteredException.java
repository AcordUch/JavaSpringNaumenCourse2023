package com.acord.dealweb.domain.exception;

public class AlreadyRegisteredException extends Exception {
  public AlreadyRegisteredException() {
    super("User already registered!");
  }
}
