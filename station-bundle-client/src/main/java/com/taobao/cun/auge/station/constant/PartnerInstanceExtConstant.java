package com.taobao.cun.auge.station.constant;

public final class PartnerInstanceExtConstant {

	// 根据最近2个月绩效，奖励合伙人淘帮手名额
	public static final Integer LAST_MONTHS_4_REWARD_PARENT_NUM = 2;

	// 排名前20%
	public static final Double SCALE = 0.2;

	// 最大配额
	public static final Integer MAX_CHILD_NUM = 10;

	// 每次新增名额
	public static final Integer ADD_NUM_PER = 2;

	// 默认初始化配额
	public final static Integer DEFAULT_MAX_CHILD_NUM = 3;

	// 淘帮手进入服务中，奖励父合伙人2个名额
	public final static Integer REWARD_PARENT_NUM_4_TPA_SERVICE = 2;

	// 淘帮手业绩不达标，减少父合伙人1个名额
	public final static Integer REDUCE_PARENT_NUM_4_AUTO_CLOSE = 1;

	// 根据最近2个月绩效，淘帮手自动停业
	public final static Integer LAST_MONTHS_4_AUTO_CLOSE = 2;

	// 淘帮手自动停业GMV限制
	public static final Double GMV_LIMIT_4_AUTO_CLOSE = 1000d;

	// 淘帮手自动停业订单限制
	public static final Long ORDER_LIMIT_4_AUTO_CLOSE = 10l;

	private PartnerInstanceExtConstant() {

	}

}
