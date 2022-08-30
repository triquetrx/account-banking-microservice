package com.cognizant.account;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cognizant.account.dto.AccountDTO;
import com.cognizant.account.dto.StatementDTO;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DTOTest {

	AccountDTO accountDTO = new AccountDTO("CUST00", "SAVINGS", 1000);
	StatementDTO statementDTO;

	@Test
	void testStatementDTO() {
		
		statementDTO =new StatementDTO(new Date(), "ACC0", "TEST", "REF0", 1000, 2000, 3000);

		assertEquals(new Date(), statementDTO.getStatementDate());
		assertEquals("ACC0", statementDTO.getAccountId());
		assertEquals("TEST", statementDTO.getNarration());
		assertEquals("REF0", statementDTO.getRefNo());
		assertEquals(1000, statementDTO.getWithdrawal());
		assertEquals(2000, statementDTO.getDeposit());
		assertEquals(3000, statementDTO.getClosingBalance());

	}
	
	@Test
	void testAccountDTO() {
		assertEquals(accountDTO.getAccountType(), "SAVINGS");
		assertEquals(accountDTO.getCustomerId(), "CUST00");
		assertEquals(accountDTO.getDeposit(), 1000);
	}

}
