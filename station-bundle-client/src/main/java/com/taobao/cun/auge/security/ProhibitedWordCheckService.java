package com.taobao.cun.auge.security;

import java.util.Optional;

/**
 * 违禁词检查服务
 * @author chengyu.zhoucy
 *
 */
public interface ProhibitedWordCheckService {
	/**
	 * 检查文本是否含有违禁词
	 * @param word
	 * @return
	 */
	boolean isProhibitedWord(String word);
	
	/**
	 * 获取一段文本中包含的一个违禁词
	 * @param text
	 * @return
	 */
	Optional<String> getProhibitedWord(String text);
}
