package com.cognizant.account.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cognizant.account.dto.AccountDTO;
import com.cognizant.account.dto.TransactionDTO;
import com.cognizant.account.exceptions.InvalidAccessException;
import com.cognizant.account.exceptions.UserDoesNotExistsException;
import com.cognizant.account.model.Account;
import com.cognizant.account.model.AccountCreationStatus;
import com.cognizant.account.model.TransactionStatus;

@Service
public interface AccountService {

	AccountCreationStatus createAccount(String token, AccountDTO accountDTO) throws InvalidAccessException;

	List<Account> getAllAccounts(String token) throws InvalidAccessException;

	Account getAccountById(String token, String id) throws InvalidAccessException, UserDoesNotExistsException;

	Account getAccountByCustomerId(String token, String id) throws InvalidAccessException, UserDoesNotExistsException;

	TransactionStatus withdraw(String token, TransactionDTO transactionDTO)
			throws UserDoesNotExistsException, InvalidAccessException;

	TransactionStatus deposit(String token, TransactionDTO transactionDTO)
			throws UserDoesNotExistsException, InvalidAccessException;

	Account getMyAccount(String token) throws InvalidAccessException;

	
}
