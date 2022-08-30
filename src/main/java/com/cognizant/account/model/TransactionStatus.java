package com.cognizant.account.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public @Data @NoArgsConstructor @AllArgsConstructor class TransactionStatus {

	private String message;
	private double balance;
	
}
