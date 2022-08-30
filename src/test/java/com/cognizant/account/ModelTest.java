package com.cognizant.account;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cognizant.account.model.Account;
import com.cognizant.account.model.AccountCreationStatus;
import com.cognizant.account.model.Statement;
import com.cognizant.account.model.TransactionStatus;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ModelTest {
	
	Account account = new Account("ACC0", "SAVINGS", "CUST1", 1000);
	AccountCreationStatus accountCreationStatus = new AccountCreationStatus("CREATED", "ACC0");
	Statement statement;
	TransactionStatus transactionStatus;
	
	
	@Test
	void testStatementModel() {
		statement = new Statement("STAT01", "ACC0",new Date(), "TEST", "REF0", 1000, 2000, 3000);
		assertEquals("STAT01", statement.getStatementId());
		assertEquals("ACC0", statement.getAccountId());
		assertEquals(statement.getNarration(), "TEST");
		assertEquals("REF0", statement.getRefNo());
		assertEquals(1000, statement.getWithdrawal());
		assertEquals(2000, statement.getDeposit());
		assertEquals(3000, statement.getClosingBalance());
	}
	
	@Test
	void testAccountModel() {
		assertEquals("ACC0", account.getAccountId());
		assertEquals("SAVINGS", account.getAccountType());
		assertEquals("CUST1", account.getCustomerId());
		assertEquals(1000, account.getBalance());
	}
	
	@Test
	void testAccountCreationStatus() {
		assertEquals("CREATED", accountCreationStatus.getMessage());
		assertEquals("ACC0", accountCreationStatus.getAccountId());
	}
	
	@Test
	void testTransactionStatus() {
		transactionStatus = new TransactionStatus();
		transactionStatus.setBalance(1000);
		transactionStatus.setMessage("DONE");
		assertEquals(1000,transactionStatus.getBalance());
		assertEquals("DONE", transactionStatus.getMessage());
	}
	

}
