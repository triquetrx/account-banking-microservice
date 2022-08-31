package com.cognizant.account.transactions;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
@Component
public class Logging {
	
	@Pointcut("within(@org.springframework.stereotype.Repository *)"
			+ " || within(@org.springframework.stereotype.Service *)"
			+ " || within(@org.springframework.web.bind.annotation.RestController *)")
	public void springPointCut() {
		// Point cut for Spring boot
	}

	@Pointcut("within(com.cognizant.account.service..*)"
			+ " || within(com.cognizant.account.controller..*)"
			+ " || within(com.cognizant.account..*)")
	public void applicationPointcut() {
		// Point cut for application's package
	}
	
	@AfterThrowing(pointcut = "springPointCut() && applicationPointcut()", throwing = "e")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
		log.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(),
				joinPoint.getSignature().getName(), e.getCause() != null ? e.getCause() : "NULL");
	}

	@After("springPointCut() && applicationPointcut()")
	public void logAfterCalling(JoinPoint joinPoint) {
		log.info("Called Application for " + joinPoint.getSignature().getDeclaringTypeName() + "."
				+ joinPoint.getSignature().getName());
	}


}
