package com.taobao.cun.auge.validator;

import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Bean校验异常
 * @author chengyu.zhoucy
 *
 */
public class BeanValidateException extends RuntimeException {

	private static final long serialVersionUID = 836613322793627129L;

	private List<String> errors;
	
	public BeanValidateException(String className, List<String> errors){
		super(className + "[" + StringUtils.join(errors, ";") + "]");
		this.errors = errors;
	}

	public List<String> getErrors() {
		return errors;
	}
}
