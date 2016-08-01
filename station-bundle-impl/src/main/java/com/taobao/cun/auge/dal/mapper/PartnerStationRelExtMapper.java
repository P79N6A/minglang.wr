package com.taobao.cun.auge.dal.mapper;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.taobao.cun.auge.dal.domain.PartnerInstance;
import com.taobao.cun.auge.dal.example.PartnerInstanceExample;

public interface PartnerStationRelExtMapper {
	Page<PartnerInstance> selectPartnerInstancesByExample(PartnerInstanceExample example);
	
	/**
	 * 查询待冻结保证金的数据
	 */
	List<Long> getWaitThawMoney(Map param);
}
