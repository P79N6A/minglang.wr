package com.taobao.cun.auge.security;

/**
 * 违禁词检查服务
 * @author chengyu.zhoucy
 *
 */
public interface ProhibitedWordCheckService {
	boolean isProhibitedWord(String word);
}
