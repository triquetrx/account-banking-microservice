package com.cognizant.account.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognizant.account.clients.AuthClient;
import com.cognizant.account.clients.CustomerClient;
import com.cognizant.account.clients.TransactionClient;
import com.cognizant.account.dto.AccountDTO;
import com.cognizant.account.dto.CustomerDTO;
import com.cognizant.account.dto.OneWayTransactionDTO;
import com.cognizant.account.dto.StatementDTO;
import com.cognizant.account.dto.TransactionDTO;
import com.cognizant.account.exceptions.InvalidAccessException;
import com.cognizant.account.exceptions.UserDoesNotExistsException;
import com.cognizant.account.model.Account;
import com.cognizant.account.model.AccountCreationStatus;
import com.cognizant.account.model.TransactionStatus;
import com.cognizant.account.repository.AccountRepository;
import com.cognizant.account.service.AccountService;
import com.cognizant.account.service.StatementService;

@Service
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	AccountRepository accountRepository;

	@Autowired
	AuthClient authClient;

	@Autowired
	StatementService statementService;
	
	@Autowired
	TransactionClient transactionClient;

	@Autowired
	CustomerClient customerClient;

	@Override
	@Transactional
	public AccountCreationStatus createAccount(String token, AccountDTO accountDTO) throws InvalidAccessException {

		if (authClient.validatingToken(token).isValidStatus()
				&& authClient.validatingToken(token).getUserRole().equalsIgnoreCase("ROLE_EMPLOYEE")) {
			CustomerDTO customers = customerClient.getAllCustomerdetails(token, accountDTO.getCustomerId());
			String id = "ACC" + accountRepository.count();
			if (accountDTO.getAccountType().equalsIgnoreCase("SAVINGS") && accountDTO.getDeposit() >= 400) {
				accountRepository.save(new Account(id, accountDTO.getAccountType(), customers.getCustomerId(),
						0));
				customerClient.markAccountAsCreated(token, accountDTO.getCustomerId());
				transactionClient.deposit(token, new OneWayTransactionDTO(id, "NEW_ACCOUNT_OPEN", "CASH", accountDTO.getDeposit()));
				return new AccountCreationStatus(accountDTO.getAccountType() + "_ACCOUNT_CREATED", id);
			} else if (accountDTO.getAccountType().equalsIgnoreCase("CURRENT") && accountDTO.getDeposit() >= 1000) {
				accountRepository.save(new Account(id, accountDTO.getAccountType(), customers.getCustomerId(),
						0));
				customerClient.markAccountAsCreated(token, accountDTO.getCustomerId());
				transactionClient.deposit(token, new OneWayTransactionDTO(id, "NEW_ACCOUNT_OPEN", "CASH", accountDTO.getDeposit()));
				return new AccountCreationStatus(accountDTO.getAccountType() + "_ACCOUNT_CREATED", id);
			} else if (accountDTO.getAccountType().equalsIgnoreCase("ZERO BALANCE")) {
				accountRepository.save(new Account(id, accountDTO.getAccountType(), customers.getCustomerId(),
						0));
				customerClient.markAccountAsCreated(token, accountDTO.getCustomerId());
				transactionClient.deposit(token, new OneWayTransactionDTO(id, "NEW_ACCOUNT_OPEN", "CASH", accountDTO.getDeposit()));
				return new AccountCreationStatus(accountDTO.getAccountType() + "_ACCOUNT_CREATED", id);
			} else {
				return new AccountCreationStatus("INVALID_REQUEST", "NOT_APPLICABLE");
			}
		}
		throw new InvalidAccessException();

	}

	@Override
	@Transactional
	public List<Account> getAllAccounts(String token) throws InvalidAccessException {

		if (authClient.validatingToken(token).isValidStatus()
				&& authClient.validatingToken(token).getUserRole().equalsIgnoreCase("ROLE_EMPLOYEE")) {
			return accountRepository.findAll();
		}
		throw new InvalidAccessException();

	}

	@Override
	public Account getAccountById(String token, String id) throws InvalidAccessException, UserDoesNotExistsException {

		if (authClient.validatingToken(token).isValidStatus()) {
			Optional<Account> account = accountRepository.findById(id);
			if (!account.isEmpty()) {
				return account.get();
			}
			throw new UserDoesNotExistsException();
		}
		throw new InvalidAccessException();

	}

	@Override
	public Account getAccountByCustomerId(String token, String id)
			throws InvalidAccessException, UserDoesNotExistsException {

		if (authClient.validatingToken(token).isValidStatus()
				&& authClient.validatingToken(token).getUserRole().equalsIgnoreCase("ROLE_EMPLOYEE")) {
			Optional<Account> account = accountRepository.findByCustomerId(id);
			if (!account.isEmpty()) {
				return account.get();
			}
			throw new UserDoesNotExistsException();
		}
		throw new InvalidAccessException();

	}

	@Override
	public TransactionStatus withdraw(String token, TransactionDTO transactionDTO)
			throws UserDoesNotExistsException, InvalidAccessException {
		if (authClient.validatingToken(token).isValidStatus()) {
			Optional<Account> account = accountRepository.findById(transactionDTO.getAccountId());
			if (!account.isEmpty()) {
				Account customerAccount = account.get();
				double balance = customerAccount.getBalance() - transactionDTO.getAmount();
				customerAccount.setBalance(balance);
				accountRepository.save(customerAccount);
				statementService.writeStatement(
						new StatementDTO(new Date(), transactionDTO.getAccountId(), transactionDTO.getNarration(),
								transactionDTO.getRefNo(), transactionDTO.getAmount(), 0, balance));
				return new TransactionStatus("WITHDRAW_SUCCESSFULL_FROM_" + transactionDTO.getAccountId(), balance);
			}
			throw new UserDoesNotExistsException();
		}
		throw new InvalidAccessException();
	}

	@Override
	public TransactionStatus deposit(String token, TransactionDTO transactionDTO)
			throws UserDoesNotExistsException, InvalidAccessException {
		if (authClient.validatingToken(token).isValidStatus()) {
			Optional<Account> account = accountRepository.findById(transactionDTO.getAccountId());
			if (!account.isEmpty()) {
				Account customerAccount = account.get();
				double balance = customerAccount.getBalance() + transactionDTO.getAmount();
				customerAccount.setBalance(balance);
				accountRepository.save(customerAccount);
				statementService.writeStatement(
						new StatementDTO(new Date(), transactionDTO.getAccountId(), transactionDTO.getNarration(),
								transactionDTO.getRefNo(), 0,transactionDTO.getAmount(), balance));
				return new TransactionStatus("DEPOSIT_SUCCESSFULL_INTO_" + transactionDTO.getAccountId(), balance);
			}
			throw new UserDoesNotExistsException();
		}
		throw new InvalidAccessException();
	}
	
	@Override
	public Account getMyAccount(String token) throws InvalidAccessException {
		if(authClient.validatingToken(token).isValidStatus()) {
			if(authClient.validatingToken(token).getUserRole().equalsIgnoreCase("ROLE_EMPLOYEE")) {
				return null;
			}
			String customerId = authClient.validatingToken(token).getCustomerId();
			return accountRepository.findByCustomerId(customerId).get();
		}
		throw new InvalidAccessException();
	}


}
