package org.example.expert.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class TransactionLoggingAspect {
	@Before("@annotation(org.springframework.transaction.annotation.Transactional)")
	public void beforeTransaction(JoinPoint joinPoint) {
		log.info("트랜잭션 시작: {}", joinPoint.getSignature());
	}

	@AfterReturning("@annotation(org.springframework.transaction.annotation.Transactional)")
	public void afterTransaction(JoinPoint joinPoint) {
		log.info("트랜잭션 커밋: {}", joinPoint.getSignature());
	}

	@AfterThrowing(value = "@annotation(org.springframework.transaction.annotation.Transactional)", throwing = "e")
	public void afterThrowing(JoinPoint joinPoint, Throwable e) {
		log.error("트랜잭션 롤백 (예외 발생): {} - {}", joinPoint.getSignature(), e.getMessage());
	}
}
