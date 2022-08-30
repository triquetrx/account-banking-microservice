package com.cognizant.account;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cognizant.account.clients.AuthClient;
import com.cognizant.account.clients.CustomerClient;
import com.cognizant.account.clients.TransactionClient;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ClientTest {

	AuthClient authClient;
	TransactionClient transactionClient;
	CustomerClient customerClient;

	@Test
	void testAuthClientLoad() {
		assertThat(authClient).isNull();
	}

	@Test
	void testTransactionClientLoad() {
		assertThat(transactionClient).isNull();
	}

	@Test
	void testCustomerClientLoad() {
		assertThat(customerClient).isNull();
	}

}
