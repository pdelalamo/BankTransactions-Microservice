package com.example.demo.services.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exceptions.AccountBalanceException;
import com.example.demo.exceptions.AccountNotFoundException;
import com.example.demo.infrastructure.AccountRepository;
import com.example.demo.infrastructure.TransactionRepository;
import com.example.demo.jpaentities.Account;
import com.example.demo.jpaentities.Transaction;
import com.example.demo.models.TransactionPayload;
import com.example.demo.models.TransactionStatusRequest;
import com.example.demo.models.TransactionStatusResponse;
import com.example.demo.services.TransactionService;
import com.example.demo.utils.Utils;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	TransactionRepository transactionRepository;

	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	Utils utils;

	/**saves a transaction in the DB and updates the associated account
 	*@transactionPayload - the request**/
	public boolean createTransaction(TransactionPayload transactionPayload) {
		Optional<Account> account = searchAccount(transactionPayload);
		saveTransaction(transactionPayload);
		updateBalance(transactionPayload, account);
		return true;
	}

	/**finds an account in the DB
 	*@transactionPayload - the request**/
	@Transactional
	public Optional<Account> searchAccount(TransactionPayload transactionPayload) {
		// First we search the account by the IBAN
		Optional<Account> account = accountRepository.findById(transactionPayload.getAccount_iban());
		if (account.orElse(null) == null || account.isEmpty())
			throw new AccountNotFoundException(transactionPayload.getAccount_iban());
		return account;
	}

	/**saves a transaction in the DB
 	*@transactionPayload - the request**/	
	@Transactional
	public void saveTransaction(TransactionPayload transactionPayload) {

		// Create an instance of 'Transaction' entity
		Transaction transaction = new Transaction();
		if(transactionPayload.getReference() == null)
			transaction.setReference(utils.generateRandomString());
		else			
		transaction.setReference(transactionPayload.getReference());
		transaction.setAccount_iban(transactionPayload.getAccount_iban());
		transaction.setDate(transactionPayload.getDate());
		transaction.setAmount(transactionPayload.getAmount());
		transaction.setFee(transactionPayload.getFee());
		transaction.setDescription(transactionPayload.getDescription());

		// Save the instance of the Transaction entity using the repository
		// 'TransactionRepository'
		transaction = transactionRepository.save(transaction);
	}
	
	
	/**updates an account balance in DB
 	*@transactionPayload - the request
 	*@account - the account to update**/
	@Transactional
	public void updateBalance(TransactionPayload transactionPayload, Optional<Account> account) {

		// now we update the balance of the account
		BigDecimal newBalance = account.get().getBalance()
				.add(transactionPayload.getAmount().add(transactionPayload.getFee().negate()));
		if (newBalance.compareTo(BigDecimal.ZERO) < 0)
			throw new AccountBalanceException();

		account.get().setBalance(newBalance);
		accountRepository.save(account.get());
	}
	
	/**Returns the transaction with its status
 	*@transactionStatusRequest - the request that contains reference and date**/
	@Override
	public TransactionStatusResponse getTransactionStatus(TransactionStatusRequest transactionStatusRequest) {
		Optional<Transaction> transaction = findByReference(transactionStatusRequest.getReference());
		return manageTransactionResponse(transaction, transactionStatusRequest);
	}
	
	
	/**Manages the status of the transaction based on the status and the date
	 	*@transaction - The transaction stored in the DB 
	 	*@transactionStatusRequest - the request that contains reference and date**/
	public TransactionStatusResponse manageTransactionResponse(Optional<Transaction> transaction,
			TransactionStatusRequest transactionStatusRequest) {
		TransactionStatusResponse response = new TransactionStatusResponse();
		if (transaction.isEmpty()) {
			response.setReference(transactionStatusRequest.getReference());
			response.setStatus("INVALID");
			return response;
		}

		switch (transactionStatusRequest.getChannel()) {
		case "CLIENT":
			if (transaction.get().getDate().toLocalDate().isBefore(LocalDate.now())) {
				response.setReference(transactionStatusRequest.getReference());
				response.setStatus("SETTLED");
				response.setAmount(transaction.get().getAmount().subtract(transaction.get().getFee()));
			} else if (transaction.get().getDate().toLocalDate().equals(LocalDate.now())) {
				response.setReference(transactionStatusRequest.getReference());
				response.setStatus("PENDING");
				response.setAmount(transaction.get().getAmount().subtract(transaction.get().getFee()));
			} else if (transaction.get().getDate().toLocalDate().isAfter(LocalDate.now())) {
				response.setReference(transactionStatusRequest.getReference());
				response.setStatus("FUTURE");
				response.setAmount(transaction.get().getAmount().subtract(transaction.get().getFee()));
			}
			break;
		case "ATM":
			if (transaction.get().getDate().toLocalDate().isBefore(LocalDate.now())) {
				response.setReference(transactionStatusRequest.getReference());
				response.setStatus("SETTLED");
				response.setAmount(transaction.get().getAmount().subtract(transaction.get().getFee()));
			} else if (transaction.get().getDate().toLocalDate().equals(LocalDate.now())) {
				response.setReference(transactionStatusRequest.getReference());
				response.setStatus("PENDING");
				response.setAmount(transaction.get().getAmount().subtract(transaction.get().getFee()));
			} else if (transaction.get().getDate().toLocalDate().isAfter(LocalDate.now())) {
				response.setReference(transactionStatusRequest.getReference());
				response.setStatus("PENDING");
				response.setAmount(transaction.get().getAmount().subtract(transaction.get().getFee()));
			}
			break;
		case "INTERNAL":
			if (transaction.get().getDate().toLocalDate().isBefore(LocalDate.now())) {
				response.setReference(transactionStatusRequest.getReference());
				response.setStatus("SETTLED");
				response.setAmount(transaction.get().getAmount());
				response.setFee(transaction.get().getFee());
			} else if (transaction.get().getDate().toLocalDate().equals(LocalDate.now())) {
				response.setReference(transactionStatusRequest.getReference());
				response.setStatus("PENDING");
				response.setAmount(transaction.get().getAmount());
				response.setFee(transaction.get().getFee());
			} else if (transaction.get().getDate().toLocalDate().isAfter(LocalDate.now())) {
				response.setReference(transactionStatusRequest.getReference());
				response.setStatus("FUTURE");
				response.setAmount(transaction.get().getAmount());
				response.setFee(transaction.get().getFee());
			}
			break;
		default:
			break;
		}

		return response;
	}
	
	/**Finds a transaction by its reference
	 *@reference - the reference (id) of the transaction **/
	@Transactional
	public Optional<Transaction> findByReference(String reference) {
		return transactionRepository.findById(reference);
	}

}
