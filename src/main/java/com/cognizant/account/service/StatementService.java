package com.cognizant.account.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cognizant.account.dto.StatementDTO;
import com.cognizant.account.model.Statement;

@Service
public interface StatementService {

	List<Statement> getAllStatements(String fromDate, String toDate, String accountId) throws ParseException;

	void writeStatement(StatementDTO statement);

	List<Statement> getAllStatements(Date fromDate, Date toDate, String accountId) throws ParseException;

	

}
