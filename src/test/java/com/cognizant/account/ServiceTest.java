package com.cognizant.account;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cognizant.account.service.AccountService;
import com.cognizant.account.service.StatementService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ServiceTest {
	
	AccountService accountService;
	
	StatementService statementService;
	
	@Test
	void testAccountLoad() {
		assertThat(accountService).isNull();
	}
	
	@Test
	void testStatementLoad() {
		assertThat(statementService).isNull();
	}

}
