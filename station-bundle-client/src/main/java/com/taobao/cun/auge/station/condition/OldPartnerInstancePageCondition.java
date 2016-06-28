package com.taobao.cun.auge.station.condition;

import javax.validation.constraints.NotNull;

import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.StationApplyStateEnum;

public class OldPartnerInstancePageCondition {

	// 村服务站所属村淘组织
	private String orgIdPath;

	// 村服务站编号
	private String stationNum;

	// 村服务站名称
	private String stationName;

	// 村服务站管理员
	private String managerId;

	// 村服务站地址
	private Address address;

	// 合伙人taobao昵称
	private String taobaoNick;

	// 合伙人姓名
	private String partnerName;

	// 合伙人状态
	private StationApplyStateEnum stationApplyState;

	// 合伙人类型
	@NotNull(message = "partnerType is null")
	private PartnerInstanceTypeEnum partnerType;

	// 所属TP商id
	private Long providerId;


}
