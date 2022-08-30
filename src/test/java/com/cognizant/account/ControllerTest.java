package com.cognizant.account;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cognizant.account.controller.AccountController;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ControllerTest {
	
	AccountController accountController = new AccountController();
	
	@Test
	void testControllerLoad() {
		assertThat(accountController).isNotNull();
	}

}
