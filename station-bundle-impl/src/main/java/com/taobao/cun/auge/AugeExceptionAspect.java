package com.taobao.cun.auge;

import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.station.exception.AugeServiceException;

//@Aspect
//@Order
//@Component
public class AugeExceptionAspect {

	
	@AfterThrowing(
			pointcut="execution(* com.taobao.cun.auge..*.*(..))",
	        throwing="ex")
	public void handleAugeException(Exception ex) throws Exception{
		if(ex instanceof ConstraintViolationException){
			ConstraintViolationException constraintViolationException = (ConstraintViolationException)ex;
			Set<ConstraintViolation<?>> result = constraintViolationException.getConstraintViolations();
			if (!result.isEmpty()) {
				for (Iterator<ConstraintViolation<?>> it = result.iterator(); it.hasNext();) {
					ConstraintViolation<?> violation = it.next();
					throw new AugeServiceException(violation.getPropertyPath().toString(),violation.getMessage());
				}
			}
		}
		if(ex instanceof AugeServiceException){
			throw (AugeServiceException)ex;
		}
	}
}
