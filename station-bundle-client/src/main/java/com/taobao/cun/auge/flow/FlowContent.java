package com.taobao.cun.auge.flow;

/**
 * 流程内容
 * 
 * @author chengyu.zhoucy
 *
 */
public class FlowContent {
	private Object result;
	
	private FlowContent(Object result) {
		this.result = result;
	}
	
	public Object getResult() {
		return result;
	}

	public static FlowContent create(Object object) {
		return new FlowContent(BeanDeepCopyUtils.copy(object));
	}
}
