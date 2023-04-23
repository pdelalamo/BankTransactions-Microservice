package com.example.demo.controllers;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.exceptions.AccountBalanceException;
import com.example.demo.exceptions.AccountNotFoundException;

@RestControllerAdvice
public class AccountBalanceAdvice {

  @ExceptionHandler(AccountBalanceException.class)
  public String accountNotFoundHandler(AccountBalanceException ex) {
    return ex.getMessage();
  }
}