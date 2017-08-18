package com.taobao.cun.auge.lifecycle.validator;

import com.taobao.cun.auge.lifecycle.LifeCyclePhaseContext;
import com.taobao.cun.auge.lifecycle.extension.LifeCyclePhaseExtension;

/**
 *  生命周期验证接口
 * @author zhenhuan.zhangzh
 *
 */
public interface LifeCyclePhaseValidator extends LifeCyclePhaseExtension{

	public static final String ALIPAY_ACCOUNT_VALIDATION = "ALIPAY_ACCOUNT_VALIDATION";
	
	void validate(LifeCyclePhaseContext context);
	
}
