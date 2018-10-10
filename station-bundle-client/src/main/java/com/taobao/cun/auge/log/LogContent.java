package com.taobao.cun.auge.log;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.google.common.collect.Lists;

/**
 * 日志错误信息，参数等
 * 
 * @author chengyu.zhoucy
 *
 */
public class LogContent implements Serializable{
	private static final long serialVersionUID = -5154986933794220530L;

	private List<String> errors;
	
	private List<String> messages;
    
    private Object params;

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public List<String> getErrors() {
		return errors;
	}
	
	public void addError(String error) {
		if(errors == null) {
			errors = Lists.newArrayList();
		}
		errors.add(error);
	}
	
	public void addMessage(String message) {
		if(messages == null) {
			messages = Lists.newArrayList();
		}
		messages.add(message);
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	
	public void setException(Throwable t) {
		String[] errors = ExceptionUtils.getStackFrames(t);
		if(errors != null) {
			this.errors = Lists.newArrayList();
			for(String error : errors) {
				this.errors.add(error.replaceAll("\t", " - "));
			}
		}
	}

	public Object getParams() {
		return params;
	}

	public void setParams(Object params) {
		this.params = params;
	}
}
