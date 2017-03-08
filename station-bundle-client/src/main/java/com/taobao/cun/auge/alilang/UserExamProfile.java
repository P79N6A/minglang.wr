package com.taobao.cun.auge.alilang;

import java.io.Serializable;
import java.util.List;

public class UserExamProfile implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer unFinishExamCount;
	
	private List<String> unFinishExamNames;

	public Integer getUnFinishExamCount() {
		return unFinishExamCount;
	}

	public void setUnFinishExamCount(Integer unFinishExamCount) {
		this.unFinishExamCount = unFinishExamCount;
	}

	public List<String> getUnFinishExamNames() {
		return unFinishExamNames;
	}

	public void setUnFinishExamNames(List<String> unFinishExamNames) {
		this.unFinishExamNames = unFinishExamNames;
	}
	
	
}
