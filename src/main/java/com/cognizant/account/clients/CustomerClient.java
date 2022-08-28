package com.cognizant.account.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.cognizant.account.clients.dto.CustomerDTO;

@FeignClient(name="customer-microservice",url="http://localhost:8002")
public interface CustomerClient {
	
	@GetMapping("get-customer-details/{customerId}")
	public CustomerDTO getAllCustomerdetails(@RequestHeader(name = "Authorization") String token,@PathVariable String customerId);
	
	@GetMapping("mark-account-created/{customerId}")
	public boolean markAccountAsCreated(@RequestHeader(name="Authorization") String token,@PathVariable String customerId);

}
