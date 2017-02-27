package com.taobao.cun.auge.settling;

/**
 * 新合伙人实例前置入住流程服务接口，由于需要做村小二的C转B功能，为了避免对老业务产生影响，新开发一个接口
 * 完成和icuntao的相关交互工作，并且把把部分入住的功能从cuntaoadmin中迁移出来（合作协议，冻结保证金），新增
 * 了企业认证查询服务，调用集团havana接口
 * @author zhenhuan.zhangzh
 *
 */
public interface C2BSettlingService{

	  public static final int SUBMIT_AUTH_METERAIL = 1;
	  
	  public static final int AUDIT_AUTH_DETAIL = 2;
	  
	  public static final int SIGN_PROTOCOL = 3;
	  
	  public static final int FRZONE_MONEY = 4;
	  
	  public static final int ALL_DONE = -1;


	  C2BSettlingResponse settlingStep(C2BSettlingRequest settlingFlowRequest);
	 
	  C2BSignSettleProtocolResponse signC2BSettleProtocol(C2BSignSettleProtocolRequest newSettleProtocolRequest);
	  
}
