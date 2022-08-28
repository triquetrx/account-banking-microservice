package com.cognizant.account.clients.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cognizant.account.clients.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

	Optional<Account> findByCustomerId(String customerId);
	
}
