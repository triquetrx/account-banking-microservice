package com.cognizant.account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public @Data @AllArgsConstructor @NoArgsConstructor class OneWayTransactionDTO {

	private String accountId;
	private String narration;
	private String transactionType;
	private double amount;
	
}
