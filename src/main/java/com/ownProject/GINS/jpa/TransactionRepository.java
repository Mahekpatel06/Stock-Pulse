package com.ownProject.GINS.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ownProject.GINS.transaction.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

	
}
