package com.example.demo.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.exceptions.AccountBalanceException;
import com.example.demo.exceptions.AccountNotFoundException;
import com.example.demo.infrastructure.AccountRepository;
import com.example.demo.infrastructure.TransactionRepository;
import com.example.demo.jpaentities.Account;
import com.example.demo.jpaentities.Transaction;
import com.example.demo.models.TransactionPayload;
import com.example.demo.models.TransactionStatusRequest;
import com.example.demo.models.TransactionStatusResponse;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

	@Mock
	private TransactionRepository transactionRepository;

	@Mock
	private AccountRepository accountRepository;

	@InjectMocks
	private TransactionServiceImpl transactionService;

	@Test
	public void createTransaction_accountNotFound() {
		TransactionPayload payload = new TransactionPayload();
		payload.setAccount_iban("nonexistentAccount");
		payload.setAmount(BigDecimal.TEN);
		payload.setDate(LocalDateTime.now());
		payload.setDescription("Test transaction");
		payload.setFee(BigDecimal.ZERO);
		payload.setReference("1234");

		when(accountRepository.findById(payload.getAccount_iban())).thenReturn(Optional.empty());

		assertThrows(AccountNotFoundException.class, () -> transactionService.createTransaction(payload));

		verify(transactionRepository, never()).save(any());
		verify(accountRepository, times(1)).findById("nonexistentAccount");
	}

	@Test
	public void createTransaction_insufficientBalance() {
		TransactionPayload payload = new TransactionPayload();
		payload.setAccount_iban("existingAccount");
		payload.setAmount(BigDecimal.TEN.negate());
		payload.setDate(LocalDateTime.now());
		payload.setDescription("Test transaction");
		payload.setFee(BigDecimal.ZERO);
		payload.setReference("1234aaa");

		Account account = new Account();
		account.setIban("existingAccount");
		account.setBalance(BigDecimal.ONE);

		when(accountRepository.findById(payload.getAccount_iban())).thenReturn(Optional.of(account));

		assertThrows(AccountBalanceException.class, () -> transactionService.createTransaction(payload));

		verify(accountRepository, times(1)).findById("existingAccount");
	}

	@Test
	public void createTransaction_success() {
		TransactionPayload payload = new TransactionPayload();
		payload.setAccount_iban("existingAccount");
		payload.setAmount(BigDecimal.TEN);
		payload.setDate(LocalDateTime.now());
		payload.setDescription("Test transaction");
		payload.setFee(BigDecimal.ZERO);
		payload.setReference("1234");

		Account account = new Account();
		account.setIban("existingAccount");
		account.setBalance(BigDecimal.valueOf(20));

		when(accountRepository.findById(payload.getAccount_iban())).thenReturn(Optional.of(account));

		assertTrue(transactionService.createTransaction(payload));

		verify(transactionRepository, times(1)).save(any());
		verify(accountRepository, times(1)).findById("existingAccount");
		verify(accountRepository, times(1)).save(any());
	}

	@Test
	public void testInvalidTransactionStatus() {
		// Given
		String reference = "12345asasa6";
		String channel = "ANY";

		TransactionStatusRequest request = new TransactionStatusRequest();
		request.setReference(reference);
		request.setChannel(channel);

		// When
		TransactionStatusResponse response = transactionService.getTransactionStatus(request);

		// Then
		assertEquals("INVALID", response.getStatus());
	}

	@Test
	public void testTransactionStatus() {

		String[][] testCases = { { "CLIENT" }, { "ATM" } };
		// Given
		for (String[] testCase : testCases) {
			String reference = "12345A";
			String channel = testCase[0];
			BigDecimal amount = BigDecimal.valueOf(1000.0);
			BigDecimal fee = BigDecimal.valueOf(10.0);

			Optional<Transaction> transaction = Optional.of(new Transaction());
			transaction.ifPresent(t -> {
				t.setReference(reference);
				t.setAmount(amount);
				t.setFee(fee);
				t.setDate(LocalDateTime.now().minusDays(1));
			});

			when(transactionService.findByReference(reference)).thenReturn(transaction);

			TransactionStatusRequest request = new TransactionStatusRequest();
			request.setReference(reference);
			request.setChannel(channel);

			// When
			TransactionStatusResponse response = transactionService.getTransactionStatus(request);

			// Then
			assertEquals("SETTLED", response.getStatus());
			assertEquals(amount.subtract(fee), response.getAmount());
		}
	}

	@Test
	public void testTransactionStatusTwo() {

		// Given
		String reference = "12345A";
		String channel = "INTERNAL";
		BigDecimal amount = BigDecimal.valueOf(1000.0);
		BigDecimal fee = BigDecimal.valueOf(10.0);

		Optional<Transaction> transaction = Optional.of(new Transaction());
		transaction.ifPresent(t -> {
			t.setReference(reference);
			t.setAmount(amount);
			t.setFee(fee);
			t.setDate(LocalDateTime.now().minusDays(1)); // Set date to yesterday
		});

		when(transactionService.findByReference(reference)).thenReturn(transaction);

		TransactionStatusRequest request = new TransactionStatusRequest();
		request.setReference(reference);
		request.setChannel(channel);

		// When
		TransactionStatusResponse response = transactionService.getTransactionStatus(request);

		// Then
		assertEquals("SETTLED", response.getStatus());
		assertEquals(amount, response.getAmount());
		assertEquals(fee, response.getFee());
	}

	@Test
	public void testTransactionStatusThree() {

		String[][] testCases = { { "CLIENT" }, { "ATM" } };
		// Given
		for (String[] testCase : testCases) {
			String reference = "12345A";
			String channel = testCase[0];
			BigDecimal amount = BigDecimal.valueOf(1000.0);
			BigDecimal fee = BigDecimal.valueOf(10.0);

			Optional<Transaction> transaction = Optional.of(new Transaction());
			transaction.ifPresent(t -> {
				t.setReference(reference);
				t.setAmount(amount);
				t.setFee(fee);
				t.setDate(LocalDateTime.now());
			});

			when(transactionService.findByReference(reference)).thenReturn(transaction);

			TransactionStatusRequest request = new TransactionStatusRequest();
			request.setReference(reference);
			request.setChannel(channel);

			// When
			TransactionStatusResponse response = transactionService.getTransactionStatus(request);

			// Then
			assertEquals("PENDING", response.getStatus());
			assertEquals(amount.subtract(fee), response.getAmount());
		}
	}

	@Test
	public void testTransactionStatusFour() {

		// Given
		String reference = "12345A";
		String channel = "INTERNAL";
		BigDecimal amount = BigDecimal.valueOf(1000.0);
		BigDecimal fee = BigDecimal.valueOf(10.0);

		Optional<Transaction> transaction = Optional.of(new Transaction());
		transaction.ifPresent(t -> {
			t.setReference(reference);
			t.setAmount(amount);
			t.setFee(fee);
			t.setDate(LocalDateTime.now());
		});

		when(transactionService.findByReference(reference)).thenReturn(transaction);

		TransactionStatusRequest request = new TransactionStatusRequest();
		request.setReference(reference);
		request.setChannel(channel);

		// When
		TransactionStatusResponse response = transactionService.getTransactionStatus(request);

		// Then
		assertEquals("PENDING", response.getStatus());
		assertEquals(amount, response.getAmount());
		assertEquals(fee, response.getFee());
	}

	@Test
	public void testTransactionStatusFive() {

		// Given
		String reference = "12345A";
		String channel = "CLIENT";
		BigDecimal amount = BigDecimal.valueOf(1000.0);
		BigDecimal fee = BigDecimal.valueOf(10.0);

		Optional<Transaction> transaction = Optional.of(new Transaction());
		transaction.ifPresent(t -> {
			t.setReference(reference);
			t.setAmount(amount);
			t.setFee(fee);
			t.setDate(LocalDateTime.now().plusDays(1));
		});

		when(transactionService.findByReference(reference)).thenReturn(transaction);

		TransactionStatusRequest request = new TransactionStatusRequest();
		request.setReference(reference);
		request.setChannel(channel);

		// When
		TransactionStatusResponse response = transactionService.getTransactionStatus(request);

		// Then
		assertEquals("FUTURE", response.getStatus());
		assertEquals(amount.subtract(fee), response.getAmount());

	}

	@Test
	public void testTransactionStatusSix() {

		// Given
		String reference = "12345A";
		String channel = "ATM";
		BigDecimal amount = BigDecimal.valueOf(1000.0);
		BigDecimal fee = BigDecimal.valueOf(10.0);

		Optional<Transaction> transaction = Optional.of(new Transaction());
		transaction.ifPresent(t -> {
			t.setReference(reference);
			t.setAmount(amount);
			t.setFee(fee);
			t.setDate(LocalDateTime.now().plusDays(1));
		});

		when(transactionService.findByReference(reference)).thenReturn(transaction);

		TransactionStatusRequest request = new TransactionStatusRequest();
		request.setReference(reference);
		request.setChannel(channel);

		// When
		TransactionStatusResponse response = transactionService.getTransactionStatus(request);

		// Then
		assertEquals("PENDING", response.getStatus());
		assertEquals(amount.subtract(fee), response.getAmount());

	}

	@Test
	public void testTransactionStatusSeven() {

		// Given
		String reference = "12345A";
		String channel = "INTERNAL";
		BigDecimal amount = BigDecimal.valueOf(1000.0);
		BigDecimal fee = BigDecimal.valueOf(10.0);

		Optional<Transaction> transaction = Optional.of(new Transaction());
		transaction.ifPresent(t -> {
			t.setReference(reference);
			t.setAmount(amount);
			t.setFee(fee);
			t.setDate(LocalDateTime.now().plusDays(1));
		});

		when(transactionService.findByReference(reference)).thenReturn(transaction);

		TransactionStatusRequest request = new TransactionStatusRequest();
		request.setReference(reference);
		request.setChannel(channel);

		// When
		TransactionStatusResponse response = transactionService.getTransactionStatus(request);

		// Then
		assertEquals("FUTURE", response.getStatus());
		assertEquals(amount, response.getAmount());
		assertEquals(fee, response.getFee());

	}
}
