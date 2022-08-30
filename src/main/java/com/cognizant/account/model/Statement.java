package com.cognizant.account.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
public @Data @NoArgsConstructor @AllArgsConstructor class Statement {

	@Id
	private String statementId;
	private String accountId;
	private Date statementDate;
	private String narration;
	private String refNo;
	private double withdrawal;
	private double deposit;
	private double closingBalance;
	
}
