package com.taobao.cun.auge.station.constant;

public final class PartnerInstanceExtConstant {
	
	// 奖励合伙人淘帮手名额最近两个月
	public static final Integer REWARD_PARENT_NUM_LAST_MONTH_COUNT = 2;

	// 排名前20%
	public static final Double SCALE = 0.2;

	// 最大配额
	public static final Integer MAX_CHILD_NUM = 10;

	// 每次新增名额
	public static final Integer ADD_NUM_PER = 2;

	// 默认初始化配额
	public final static Integer DEFAULT_MAX_CHILD_NUM = 3;
	
	// 淘帮手进入服务中，奖励父合伙人2个名额
	public final static Integer REWARD_PARENT_NUM_FRO_SERVICE = 2;
	
	// 淘帮手业绩不达标，减少父合伙人1个名额
	public final static Integer REDUCE_PARENT_NUM_FRO_CLOSE = 1;
	
	//淘帮手自动停业计算月数
	public final static Integer AUTO_CLOSE_LAST_MONTH_COUNT = 2;
	
	//淘帮手自动停业GMV限制
	public static final Double GMV_LIMIT = 1000D;
	
	//淘帮手自动停业订单限制
	public static final Long ORDER_LIMIT = 10l;
	
	private PartnerInstanceExtConstant(){
		
	}

}
