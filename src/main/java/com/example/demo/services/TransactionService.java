package com.example.demo.services;

import com.example.demo.models.TransactionPayload;
import com.example.demo.models.TransactionStatusRequest;
import com.example.demo.models.TransactionStatusResponse;

public interface TransactionService {
	boolean createTransaction(TransactionPayload transactionPayload);
	
	TransactionStatusResponse getTransactionStatus(TransactionStatusRequest transactionStatusRequest);
}
