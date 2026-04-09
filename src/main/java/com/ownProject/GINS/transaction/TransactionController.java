package com.ownProject.GINS.transaction;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ownProject.GINS.jpa.TransactionRepository;

@RestController
public class TransactionController {
	
	@Autowired
	public TransactionRepository transactionRepository;

	@GetMapping("/transactions")
	public List<Transaction> getAllTrans() {
		return transactionRepository.findAll();
	}
	
	@GetMapping("/transactions/pagination")
	public Page<Transaction> getInventory(Pageable pageable) {
		return transactionRepository.findAll(pageable);
	}
}
