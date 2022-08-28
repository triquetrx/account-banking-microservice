package com.cognizant.account.clients.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public @NoArgsConstructor @Data @AllArgsConstructor class ValidatingDTO {
	
	private boolean validStatus;
	private String userRole;
	private String customerId;

}
