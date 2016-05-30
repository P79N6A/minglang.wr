package com.taobao.cun.auge.dal.mapper;

import com.github.pagehelper.Page;
import com.taobao.cun.auge.dal.domain.PartnerInstance;
import com.taobao.cun.auge.dal.example.PartnerInstanceExample;

public interface PartnerStationRelExtMapper {
	Page<PartnerInstance> selectPartnerInstancesByExample(PartnerInstanceExample example);
}
