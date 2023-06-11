package com.example.demo.exceptions;

public class AccountNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AccountNotFoundException(String iban) {
		super("Account not found for iban: " + iban);
	}
}
