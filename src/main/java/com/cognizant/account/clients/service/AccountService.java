package com.cognizant.account.clients.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognizant.account.clients.AuthClient;
import com.cognizant.account.clients.CustomerClient;
import com.cognizant.account.clients.TransactionClient;
import com.cognizant.account.clients.dto.AccountDTO;
import com.cognizant.account.clients.dto.CustomerDTO;
import com.cognizant.account.clients.dto.OneWayTransactionDTO;
import com.cognizant.account.clients.dto.StatementDTO;
import com.cognizant.account.clients.dto.TransactionDTO;
import com.cognizant.account.clients.exceptions.InvalidAccessException;
import com.cognizant.account.clients.exceptions.UserDoesNotExistsException;
import com.cognizant.account.clients.model.Account;
import com.cognizant.account.clients.model.AccountCreationStatus;
import com.cognizant.account.clients.model.TransactionStatus;
import com.cognizant.account.clients.repository.AccountRepository;

@Service
public class AccountService {

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

	public AccountCreationStatus createAccount(String token, AccountDTO accountDTO) throws InvalidAccessException {

		if (authClient.validatingToken(token).isValidStatus()
				&& authClient.validatingToken(token).getUserRole().equalsIgnoreCase("ROLE_EMPLOYEE")) {
			CustomerDTO customers = customerClient.getAllCustomerdetails(token, accountDTO.getCustomerId());
			String id = "ACC" + accountRepository.count();
			if (accountDTO.getAccountType().equalsIgnoreCase("SAVINGS") && accountDTO.getDeposit() >= 400) {
				accountRepository.save(new Account(id, accountDTO.getAccountType(), customers.getCustomerId(),
						accountDTO.getDeposit()));
				customerClient.markAccountAsCreated(token, accountDTO.getCustomerId());
				transactionClient.deposit(token, new OneWayTransactionDTO(id, "NEW_ACCOUNT_OPEN", "CASH", accountDTO.getDeposit()));
				return new AccountCreationStatus(accountDTO.getAccountType() + "_ACCOUNT_CREATED", id);
			} else if (accountDTO.getAccountType().equalsIgnoreCase("CURRENT") && accountDTO.getDeposit() >= 1000) {
				accountRepository.save(new Account(id, accountDTO.getAccountType(), customers.getCustomerId(),
						accountDTO.getDeposit()));
				customerClient.markAccountAsCreated(token, accountDTO.getCustomerId());
				transactionClient.deposit(token, new OneWayTransactionDTO(id, "NEW_ACCOUNT_OPEN", "CASH", accountDTO.getDeposit()));
				return new AccountCreationStatus(accountDTO.getAccountType() + "_ACCOUNT_CREATED", id);
			} else if (accountDTO.getAccountType().equalsIgnoreCase("ZERO BALANCE")) {
				accountRepository.save(new Account(id, accountDTO.getAccountType(), customers.getCustomerId(),
						accountDTO.getDeposit()));
				customerClient.markAccountAsCreated(token, accountDTO.getCustomerId());
				transactionClient.deposit(token, new OneWayTransactionDTO(id, "NEW_ACCOUNT_OPEN", "CASH", accountDTO.getDeposit()));
				return new AccountCreationStatus(accountDTO.getAccountType() + "_ACCOUNT_CREATED", id);
			} else {
				return new AccountCreationStatus("INVALID_REQUEST", "NOT_APPLICABLE");
			}
		}
		throw new InvalidAccessException();

	}

	public List<Account> getAllAccounts(String token) throws InvalidAccessException {

		if (authClient.validatingToken(token).isValidStatus()
				&& authClient.validatingToken(token).getUserRole().equalsIgnoreCase("ROLE_EMPLOYEE")) {
			return accountRepository.findAll();
		}
		throw new InvalidAccessException();

	}

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
	
	public Account getMyAccount(String token) throws InvalidAccessException {
		if(authClient.validatingToken(token).isValidStatus()) {
			String customerId = authClient.validatingToken(token).getCustomerId();
			return accountRepository.findByCustomerId(customerId).get();
		}
		throw new InvalidAccessException();
	}

}
