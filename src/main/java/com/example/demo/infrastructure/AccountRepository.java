package com.example.demo.infrastructure;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.jpaentities.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, String> {
}