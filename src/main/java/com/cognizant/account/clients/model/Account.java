package com.cognizant.account.clients.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
public @Data @NoArgsConstructor @AllArgsConstructor class Account {
	
	@Id
	private String accountId;
	private String accountType;
	private String customerId;
	private double balance;

}
