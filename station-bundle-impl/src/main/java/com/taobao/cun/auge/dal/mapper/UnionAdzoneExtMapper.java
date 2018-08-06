package com.taobao.cun.auge.dal.mapper;

import com.taobao.cun.auge.station.dto.NewuserOrderStat;

import java.util.List;
import java.util.Map;

public interface UnionAdzoneExtMapper {
    List<NewuserOrderStat> getNewuserOrderStat(Map<String,Object> param );
}
