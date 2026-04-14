package com.ownProject.GINS.transaction;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ownProject.GINS.jpa.TransactionRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Transaction APIs", description = "Audit trail for all financial and stock movements within the Stock Pulse system.")
public class TransactionController {
	
	@Autowired
	public TransactionRepository transactionRepository;

	@GetMapping("/transactions")
	@Operation(summary = "get all transaction history")
	public List<Transaction> getAllTrans() {
		return transactionRepository.findAll();
	}
	
	@GetMapping("/transactions/pagination")
	@Operation(summary = "get transactins in diff pages acc. to your choice")
	public Page<Transaction> getInventory(Pageable pageable) {
		return transactionRepository.findAll(pageable);
	}
}
