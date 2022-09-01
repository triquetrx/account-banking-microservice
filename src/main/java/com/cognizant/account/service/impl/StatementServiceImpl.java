package com.cognizant.account.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognizant.account.dto.StatementDTO;
import com.cognizant.account.model.Statement;
import com.cognizant.account.repository.StatementRepository;
import com.cognizant.account.service.StatementService;

@Service
public class StatementServiceImpl implements StatementService {
	
	@Autowired
	StatementRepository statementRepository;

	@Override
	@Transactional
	public void writeStatement(StatementDTO statement) {
		String id = "STATEMENT" + statementRepository.count();
		statementRepository
				.save(new Statement(id,statement.getAccountId(), statement.getStatementDate(), statement.getNarration(), statement.getRefNo(),
						statement.getWithdrawal(), statement.getDeposit(), statement.getClosingBalance()));
	}
	
	@Override
	@Transactional
	public List<Statement> getAllStatements(String fromDate, String toDate,String accountId) throws ParseException{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		List<Statement> statementDateBetween = statementRepository.findByStatementDateBetween(dateFormat.parse(fromDate), dateFormat.parse(toDate));
		List<Statement> result = new ArrayList<>();		
		for(Statement statement: statementDateBetween) {
			if(statement.getAccountId().equalsIgnoreCase(accountId)) {
				result.add(statement);
			}
		}
		return result;
	}
	
	@Override
	public List<Statement> getAllStatements(Date fromDate, Date toDate,String accountId) throws ParseException{
		List<Statement> statementDateBetween = statementRepository.findByStatementDateBetween(fromDate, toDate);
		List<Statement> result = new ArrayList<>();		
		for(Statement statement: statementDateBetween) {
			if(statement.getAccountId().equalsIgnoreCase(accountId)) {
				result.add(statement);
			}
		}
		return result;
	}

}
