package com.example.demo.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;


public class TransactionPayload {
    private String reference;
    @NotNull
    private String account_iban;
    private LocalDateTime date;
    @NotNull
    private BigDecimal amount;
    private BigDecimal fee;
    private String description;
    
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getAccount_iban() {
		return account_iban;
	}
	public void setAccount_iban(String account_iban) {
		this.account_iban = account_iban;
	}
	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getFee() {
		return fee;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
    
    
    
}