package com.taobao.cun.auge.security;

import javax.annotation.Resource;

import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface= ProhibitedWordCheckService.class, clientTimeout = 8000)
public class ProhibitedWordCheckServiceImpl implements ProhibitedWordCheckService{
	@Resource
	private ProhibitedWordChecker prohibitedWordChecker;
	
	@Override
	public boolean isProhibitedWord(String word) {
		return prohibitedWordChecker.isProhibitedWord(word);
	}
}
