package com.taobao.cun.auge.validator;

import java.util.List;

/**
 * Bean校验结果
 * 
 * @author chengyu.zhoucy
 *
 */
public class ValidateResult {
	private String className;
	private List<String> errors;
	
	public ValidateResult(String className, List<String> errors){
		this.className = className;
		this.errors = errors;
	}
	
	public String getClassName() {
		return className;
	}

	public List<String> getErrors() {
		return errors;
	}

	public boolean hasError(){
		return errors != null && !errors.isEmpty();
	}
}
