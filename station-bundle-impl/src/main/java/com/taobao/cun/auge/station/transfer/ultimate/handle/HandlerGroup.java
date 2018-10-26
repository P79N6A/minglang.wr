package com.taobao.cun.auge.station.transfer.ultimate.handle;

/**
 * 处理器分组，自动交接、提前交接处理流程不一样
 * 
 * @author chengyu.zhoucy
 *
 */
public interface HandlerGroup {
	/**
	 * 自动交接
	 */
	String AUTO = "auto";
	
	/**
	 * 提前交接
	 */
	String ADVANCE = "advance";
}
