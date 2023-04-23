package com.example.demo.infrastructure;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.jpaentities.Transaction;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, String> {
}
