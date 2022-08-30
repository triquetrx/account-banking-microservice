package com.cognizant.account.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public @Data @AllArgsConstructor @NoArgsConstructor class CustomerDTO {
	
	private String customerId;
	private String name;
	private String addressLine1;
	private String addressLine2;
	private String panNo;
	private Date dateOfBirth;
	private long phoneNumber;
	private String email;
	private String username;
	private boolean isAccountCreated;

}
