package com.example.demo.controllers;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.exceptions.AccountNotFoundException;

@RestControllerAdvice
public class AccountNotFoundAdvice {

  @ExceptionHandler(AccountNotFoundException.class)
  public String accountNotFoundHandler(AccountNotFoundException ex) {
    return ex.getMessage();
  }
}