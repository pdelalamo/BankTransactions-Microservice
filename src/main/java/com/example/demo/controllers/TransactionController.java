package com.example.demo.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.TransactionPayload;
import com.example.demo.models.TransactionStatusRequest;
import com.example.demo.models.TransactionStatusResponse;
import com.example.demo.services.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
	@Autowired
	TransactionService transactionService;

	@PostMapping("/newTransaction")
	public ResponseEntity<String> createTransaction(@Valid @RequestBody TransactionPayload transactionPayload) {
		boolean created = transactionService.createTransaction(transactionPayload);
		if (created)
			return ResponseEntity.ok().body("Transaction stored successfully.");
		else
			return ResponseEntity.badRequest().body("Transaction could not be created");

	}

	@PostMapping("/status")
	public TransactionStatusResponse getTransactionStatus(@RequestBody TransactionStatusRequest request) {
		return transactionService.getTransactionStatus(request);
	}
}