package com.cognizant.account.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.cognizant.account.clients.dto.OneWayTransactionDTO;

@FeignClient(name = "transaction-client", url = "http://localhost:8004")
public interface TransactionClient {

	@PostMapping("/deposit")
	public ResponseEntity<?> deposit(@RequestHeader(name = "Authorization") String token,
			@RequestBody OneWayTransactionDTO transactionDTO);

}
