package com.cognizant.account.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.account.dto.AccountDTO;
import com.cognizant.account.dto.TransactionDTO;
import com.cognizant.account.exceptions.InvalidAccessException;
import com.cognizant.account.exceptions.UserDoesNotExistsException;
import com.cognizant.account.model.Account;
import com.cognizant.account.model.AccountCreationStatus;
import com.cognizant.account.model.TransactionStatus;
import com.cognizant.account.service.AccountService;
import com.cognizant.account.service.StatementService;

import feign.FeignException.FeignClientException;

@RestController
public class AccountController {
	
	@Autowired
	AccountService accountService;
	
	@Autowired
	StatementService statementService;
	
	@PostMapping("/create-account")
	@CrossOrigin(origins ="http://localhost:5000")
	public ResponseEntity<?> createAccount(@RequestHeader(name="Authorization")String token,@RequestBody AccountDTO accountDTO) {
		try {
			AccountCreationStatus creationStatus = accountService.createAccount(token, accountDTO);
			if(creationStatus.getMessage().equalsIgnoreCase("INVALID_REQUEST")) {				
				return new ResponseEntity<>(creationStatus,HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>(creationStatus,HttpStatus.OK);
		} catch (InvalidAccessException e) {
			return new ResponseEntity<>("UNAUTHORIZED_ACCESS",HttpStatus.UNAUTHORIZED);
		} catch(FeignClientException e) {
			String[] message = e.getMessage().split(" ");
			int errCode = Integer.parseInt(message[0].split("")[1]+message[0].split("")[2]+message[0].split("")[3]);
			return new ResponseEntity<>(message[5],HttpStatus.valueOf(errCode));
		}
	}

	@CrossOrigin(origins ="http://localhost:5000")
	@GetMapping("/get-accounts/{accountId}")
	public ResponseEntity<?> getAccounts(@RequestHeader(name="Authorization")String token,@PathVariable String accountId) {
		try {
			return new ResponseEntity<>(accountService.getAccountById(token, accountId),HttpStatus.OK);
		} catch (InvalidAccessException e) {
			return new ResponseEntity<>("UNAUTHORIZED_ACCESS",HttpStatus.UNAUTHORIZED);
		} catch (UserDoesNotExistsException e) {
			return new ResponseEntity<>("ACCOUNT_DOES_NOT_EXISTS",HttpStatus.BAD_REQUEST);
		} catch(FeignClientException e) {
			String[] message = e.getMessage().split(" ");
			int errCode = Integer.parseInt(message[0].split("")[1]+message[0].split("")[2]+message[0].split("")[3]);
			return new ResponseEntity<>(message[5],HttpStatus.valueOf(errCode));
		}
	}
	
	@CrossOrigin(origins ="http://localhost:5000")
	@GetMapping("/get-customer-accounts/{customerId}")
	public ResponseEntity<?> getCustomerAccounts(@RequestHeader(name="Authorization")String token,@PathVariable String customerId) {
		try {
			return new ResponseEntity<>(accountService.getAccountByCustomerId(token, customerId),HttpStatus.OK);
		} catch (InvalidAccessException e) {
			return new ResponseEntity<>("UNAUTHORIZED_ACCESS",HttpStatus.UNAUTHORIZED);
		} catch (UserDoesNotExistsException e) {
			return new ResponseEntity<>("ACCOUNT_DOES_NOT_EXISTS",HttpStatus.BAD_REQUEST);
		} catch(FeignClientException e) {
			String[] message = e.getMessage().split(" ");
			int errCode = Integer.parseInt(message[0].split("")[1]+message[0].split("")[2]+message[0].split("")[3]);
			return new ResponseEntity<>(message[5],HttpStatus.valueOf(errCode));
		}
	}
	
	@CrossOrigin(origins = "http://localhost:5000")
	@GetMapping("/get-my-account")
	public ResponseEntity<?> getMyAccount(@RequestHeader(name = "Authorization")String token){
		try {
			Account account = accountService.getMyAccount(token);
			return new ResponseEntity<>(account,HttpStatus.OK);
		} catch (InvalidAccessException e) {
			return new ResponseEntity<>("UNAUTHORIZED_ACCESS",HttpStatus.UNAUTHORIZED);
		} catch(FeignClientException e) {
			String[] message = e.getMessage().split(" ");
			int errCode = Integer.parseInt(message[0].split("")[1]+message[0].split("")[2]+message[0].split("")[3]);
			return new ResponseEntity<>(message[5],HttpStatus.valueOf(errCode));
		}
	}
	
	@CrossOrigin(origins ="http://localhost:5000")
	@GetMapping("/getAccountStatement/{accountId}/{fromDate}/{toDate}")
	public ResponseEntity<?> getAccountStatement(@RequestHeader(name="Authorization")String token,@PathVariable String fromDate, @PathVariable String toDate, @PathVariable String accountId){
		try {
			return new ResponseEntity<>(statementService.getAllStatements(fromDate, toDate, accountId),HttpStatus.OK);
		} catch (ParseException e) {
			return new ResponseEntity<>("DATE_MUST_BE_OF_PATTERN_YYYY-MM-DD",HttpStatus.BAD_REQUEST);
		}
	}
	
	@CrossOrigin(origins ="http://localhost:5000")
	@GetMapping("/getAccountStatement/{accountId}")
	public ResponseEntity<?> getAccountStatement(@RequestHeader(name="Authorization")String token,@PathVariable String accountId){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		int currentMonth = LocalDate.now().getMonthValue();
		int currentYear = LocalDate.now().getYear();
		int today = LocalDate.now().getDayOfMonth();
		calendar.set(currentYear, currentMonth,today);
		int firstDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
		int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		try {
			Date fromDate = simpleDateFormat.parse(currentYear+"-"+currentMonth+"-"+firstDay);
			Date toDate = simpleDateFormat.parse(currentYear+"-"+currentMonth+"-"+lastDay);
			return new ResponseEntity<>(statementService.getAllStatements(fromDate, toDate,accountId),HttpStatus.OK);
		} catch (ParseException e) {
			return new ResponseEntity<>("OOPS_SOMETHING_WENT_WRONG",HttpStatus.METHOD_NOT_ALLOWED);
		}
	}
	
	@CrossOrigin(origins ="http://localhost:5000")
	@PostMapping("/deposit")
	public ResponseEntity<?> deposit(@RequestHeader(name="Authorization")String token,@RequestBody TransactionDTO transactionDTO){
		try {
			TransactionStatus transactionStatus = accountService.deposit(token, transactionDTO);
			return new ResponseEntity<>(transactionStatus,HttpStatus.OK);
		} catch (UserDoesNotExistsException e) {
			return new ResponseEntity<>("USER_DOES_NOT_EXISTS",HttpStatus.BAD_REQUEST);
		} catch (InvalidAccessException e) {
			return new ResponseEntity<>("UNAUTHORIZED_ACCESS",HttpStatus.UNAUTHORIZED);
		} catch(FeignClientException e) {
			String[] message = e.getMessage().split(" ");
			int errCode = Integer.parseInt(message[0].split("")[1]+message[0].split("")[2]+message[0].split("")[3]);
			return new ResponseEntity<>(message[5],HttpStatus.valueOf(errCode));
		}
	}
	
	@CrossOrigin(origins ="http://localhost:5000")
	@PostMapping("/withdraw")
	public ResponseEntity<?> withdraw(@RequestHeader(name="Authorization")String token,@RequestBody TransactionDTO transactionDTO){
		try {
			TransactionStatus transactionStatus = accountService.withdraw(token, transactionDTO);
			return new ResponseEntity<>(transactionStatus,HttpStatus.OK);
		} catch (UserDoesNotExistsException e) {
			return new ResponseEntity<>("USER_DOES_NOT_EXISTS",HttpStatus.BAD_REQUEST);
		} catch (InvalidAccessException e) {
			return new ResponseEntity<>("UNAUTHORIZED_ACCESS",HttpStatus.UNAUTHORIZED);
		} catch(FeignClientException e) {
			String[] message = e.getMessage().split(" ");
			int errCode = Integer.parseInt(message[0].split("")[1]+message[0].split("")[2]+message[0].split("")[3]);
			return new ResponseEntity<>(message[5],HttpStatus.valueOf(errCode));
		}
	}
	
}
