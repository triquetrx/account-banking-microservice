package com.cognizant.account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public @Data @AllArgsConstructor @NoArgsConstructor class AccountDTO {
	
	private String customerId;
	private String accountType;
	private double deposit;

}
