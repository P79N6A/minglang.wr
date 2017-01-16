package com.taobao.cun.auge.dal.mapper;

import com.github.pagehelper.Page;
import com.taobao.cun.auge.dal.domain.DwiCtStationTpaIncomeM;
import com.taobao.cun.auge.dal.example.DwiCtStationTpaIncomeMExtExmple;

public interface DwiCtStationTpaIncomeMExtMapper {
	
	Page<DwiCtStationTpaIncomeM> selectStationsByExample(DwiCtStationTpaIncomeMExtExmple example);
}
