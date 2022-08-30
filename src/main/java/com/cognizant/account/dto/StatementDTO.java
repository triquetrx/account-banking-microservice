package com.cognizant.account.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public @Data @AllArgsConstructor @NoArgsConstructor class StatementDTO {
	
	private Date statementDate;
	private String accountId;
	private String narration;
	private String refNo;
	private double withdrawal;
	private double deposit;
	private double closingBalance;

}
