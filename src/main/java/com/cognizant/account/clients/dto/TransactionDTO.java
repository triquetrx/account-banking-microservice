package com.cognizant.account.clients.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public @Data @AllArgsConstructor @NoArgsConstructor class TransactionDTO {
	
	private String accountId;
	private String narration;
	private double amount;
	private String refNo;

}
