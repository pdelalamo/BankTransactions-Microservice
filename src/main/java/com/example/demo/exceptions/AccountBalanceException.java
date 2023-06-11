package com.example.demo.exceptions;

public class AccountBalanceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AccountBalanceException() {
		super("Can't complete transaction, not enough money in the account");
	}
}