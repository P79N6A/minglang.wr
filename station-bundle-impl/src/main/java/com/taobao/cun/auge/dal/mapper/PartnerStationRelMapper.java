package com.taobao.cun.auge.dal.mapper;

import com.github.pagehelper.Page;
import com.taobao.cun.auge.dal.domain.PartnerInstance;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.example.PartnerInstanceExample;

import tk.mybatis.mapper.common.Mapper;

public interface PartnerStationRelMapper extends Mapper<PartnerStationRel> {

	Page<PartnerInstance> selectPartnerInstancesByExample(PartnerInstanceExample example);

}