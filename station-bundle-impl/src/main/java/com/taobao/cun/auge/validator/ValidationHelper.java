package com.taobao.cun.auge.validator;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

import com.google.common.collect.Maps;
import com.taobao.cun.auge.station.exception.AugeBusinessException;


public class ValidationHelper {
	
    private DataBinder dataBinder;
	
	private  static MessageSource messageSource;
	
	public static final String APPLICATION_CONTEXT = "APPLICATION_CONTEXT";
	
	private final Log logger = LogFactory.getLog(getClass());
	
	private List<String> ingoreErrorCodes;
	
	public static ValidationHelper getInstance(Object target){
		return new ValidationHelper(target);
	}
	
	public ValidationHelper setIngoreErrorCodes(List<String> ingoreErrorCodes){
		this.ingoreErrorCodes = ingoreErrorCodes;
		return this;
	}
	
	private ValidationHelper(Object target){
		this.dataBinder = new DataBinder(target);
	}
	
	public ValidationHelper bindMessageSource(MessageSource messageSource){
		ValidationHelper.messageSource = messageSource;
		return this;
	}
	
	public ValidationHelper setValidator(Validator validator){
		if(dataBinder != null){
			dataBinder.setValidator(validator);
		}
		return this;
	}
	
	public ValidationHelper addValidators(Validator ... validators){
		if(dataBinder != null){
			dataBinder.addValidators(validators);
		}
		return this;
	}
	
	public void exceptionIfInvalid(boolean exceptionIfInvalid){
		dataBinder.validate();
		BindingResult errors = dataBinder.getBindingResult();
		if (errors.hasErrors()) {
			for (ObjectError error : errors.getAllErrors()) {
					handleError(error,exceptionIfInvalid);
			}
		}
	}
	
	private void handleError(ObjectError error,boolean exceptionIfInvalid) {
		if(messageSource == null){
			ResourceBundleMessageSource	messageSource = new ResourceBundleMessageSource();
	    	messageSource.setDefaultEncoding("UTF-8");
	    	messageSource.setBasename("exception");
	    	bindMessageSource(messageSource);
		}
		String errorMessage = messageSource.getMessage(error, Locale.getDefault());
		if(ingoreErrorCodes!= null && !ingoreErrorCodes.contains(error.getCode())){
			this.logger.error(error.getCode()+":"+errorMessage);
		}
		
		if (exceptionIfInvalid) {
			AugeBusinessException exception =  new AugeBusinessException(error.getCode(), errorMessage);
			throw exception;
		}
	}
	
	public ValidationHelper reject(String errorCode,String spel,boolean exceptionIfInvalid){
		return reject(errorCode, spel,null,null,exceptionIfInvalid,null);
	}
	
	public ValidationHelper reject(String errorCode,String spel,String defaultMessage,boolean exceptionIfInvalid){
		return reject(errorCode, defaultMessage,null,null,exceptionIfInvalid,null);
	}
	
	public ValidationHelper reject(String errorCode,String spel){
		return reject(errorCode, spel,null,null,true,null);
	}
	
	public ValidationHelper reject(String errorCode,String spel,String defaultMessage){
		return reject(errorCode, spel,defaultMessage,null,true,null);
	}
	
	
	public ValidationHelper reject(String errorCode,String spel,String defaultMessage,Object[] args,boolean exceptionIfInvalid,Consumer<ValidationContext> consumer){
		ValidationContext validationContext = new ValidationContext(errorCode,defaultMessage,args);
		if(consumer != null){
			consumer.accept(validationContext);
		}
		Validator validator = new SpringElValidator(spel,validationContext);
		this.setValidator(validator);
		this.exceptionIfInvalid(exceptionIfInvalid);
		return this;
	}
	
	public ValidationHelper reject(String errorCode,String defaultMessage,boolean exceptionIfInvalid,Predicate<Object> predicate){
		return reject(errorCode,defaultMessage,null,exceptionIfInvalid,predicate);
	}
	
	public ValidationHelper reject(String errorCode,String defaultMessage,Predicate<Object> predicate){
		return reject(errorCode,defaultMessage,null,true,predicate);
	}
	
	public ValidationHelper reject(String errorCode,boolean exceptionIfInvalid,Predicate<Object> predicate){
		return reject(errorCode,null,exceptionIfInvalid,predicate);
	}
	
	public ValidationHelper reject(String errorCode,Predicate<Object> predicate){
		return reject(errorCode,null,true,predicate);
	}
	
	
	public ValidationHelper reject(String errorCode,String defaultMessage,Object [] args,boolean exceptionIfInvalid,Predicate<Object> predicate){
		ValidationContext validationContext = new ValidationContext(errorCode,defaultMessage,args);
		Validator validator = new PredicateValidator(predicate,validationContext);
		this.setValidator(validator);
		this.exceptionIfInvalid(exceptionIfInvalid);
		return this;
	}
	
	
	
	public  class SpringElValidator implements Validator{
		String spel;
		ExpressionParser parser = new SpelExpressionParser();  
		ValidationContext validationContext;
		
		public SpringElValidator(String spel,ValidationContext context){
			Assert.notNull(spel,"spel can not be null");
			Assert.notNull(context,"ValidationContext can not be null");
			this.spel    = spel;
			this.validationContext = context;
		}
		@Override
		public boolean supports(Class<?> clazz) {
			return true;
		}

		@Override
		public void validate(Object target, Errors errors) {
			
			StandardEvaluationContext context = new StandardEvaluationContext(target);
			Object springContext = validationContext.getContext().get(APPLICATION_CONTEXT);
			if(springContext != null && BeanFactory.class.isAssignableFrom(springContext.getClass())){
				BeanFactory beanFactory = (BeanFactory)springContext;
				context.setBeanResolver(new BeanFactoryResolver(beanFactory)); 
				context.setVariables(validationContext.getContext());
			}
			
			Boolean result = parser.parseExpression(spel).getValue(context, Boolean.class);  
			if(!result){
				errors.reject(validationContext.errorCode,validationContext.args,validationContext.defaultMessage);
			}
		}
	}
	
	public class PredicateValidator implements Validator{
		 Predicate<Object> predicate;
		 ValidationContext validationContext;
		 PredicateValidator(Predicate<Object> predicate,ValidationContext context){
				Assert.notNull(predicate,"predicate can not be null");
				Assert.notNull(context,"ValidationContext can not be null");
				this.predicate    = predicate;
				this.validationContext = context;
		}
	
		@Override
		public boolean supports(Class<?> clazz) {
			return true;
		}

		@Override
		public void validate(Object target, Errors errors) {
			if(!this.predicate.test(target)){
				errors.reject(validationContext.errorCode,validationContext.args,validationContext.defaultMessage);
			}
		}
	}

	public class ValidationContext {
		String errorCode;
		
		Object[] args;
		
		String defaultMessage;
		
		Map<String,Object> context = Maps.newConcurrentMap();
		
		ValidationContext(String errorCode,String defaultMessage,Object[] args){
			this.errorCode = errorCode;
			this.defaultMessage = defaultMessage;
			this.args = args;
		}
		
		
		ValidationContext(String errorCode){
			this(errorCode, null, null);
		}
		
		ValidationContext(String errorCode,String defaultMessage){
			this(errorCode, defaultMessage, null);
		}


		public String getErrorCode() {
			return errorCode;
		}


		public void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}


		public Object[] getArgs() {
			return args;
		}


		public void setArgs(Object[] args) {
			this.args = args;
		}


		public String getDefaultMessage() {
			return defaultMessage;
		}


		public void setDefaultMessage(String defaultMessage) {
			this.defaultMessage = defaultMessage;
		}


		public Map<String, Object> getContext() {
			return context;
		}


		public void setContext(Map<String, Object> context) {
			this.context = context;
		}
		
	}
	
}
