package com.cognizant.account.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.cognizant.account.clients.dto.ValidatingDTO;

@FeignClient(name = "auth-client", url = "http://localhost:8001")
public interface AuthClient {

	@GetMapping("/validate")
	public ValidatingDTO validatingToken(@RequestHeader(name = "Authorization") String token);

}
