package com.taobao.cun.auge.security;

import java.util.Optional;

import javax.annotation.Resource;

import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface= ProhibitedWordCheckService.class, clientTimeout = 8000)
public class ProhibitedWordCheckServiceImpl implements ProhibitedWordCheckService{
	@Resource
	private ProhibitedWordChecker prohibitedWordChecker;
	
	@Override
	public boolean hasProhibitedWord(String word) {
		return prohibitedWordChecker.hasProhibitedWord(word);
	}

	@Override
	public Optional<String> getProhibitedWord(String text) {
		return prohibitedWordChecker.getProhibitedWord(text);
	}
}
