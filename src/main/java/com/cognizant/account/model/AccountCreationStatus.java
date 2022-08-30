package com.cognizant.account.model;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

public @Data @NoArgsConstructor @AllArgsConstructor class AccountCreationStatus {

	private String message;
	private String accountId;
	
}
