package com.taobao.cun.auge.dal.mapper;

import com.github.pagehelper.Page;
import com.taobao.cun.auge.dal.domain.DwiCtStationTpaD;
import com.taobao.cun.auge.dal.example.DwiCtStationTpaDExtExample;

public interface DwiCtStationTpaDExtMapper {
	Page<DwiCtStationTpaD> selectStationsByExample(DwiCtStationTpaDExtExample example);
}
