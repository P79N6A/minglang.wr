package com.taobao.cun.auge.dal.mapper;

import com.github.pagehelper.Page;
import com.taobao.cun.auge.dal.domain.DwiCtStationTpaIncomeM;
import com.taobao.cun.auge.dal.example.DwiCtStationTpaIncomeMExmple;

public interface DwiCtStationTpaIncomeMExtMapper {
	
	Page<DwiCtStationTpaIncomeM> selectStationsByExample(DwiCtStationTpaIncomeMExmple example);
}
