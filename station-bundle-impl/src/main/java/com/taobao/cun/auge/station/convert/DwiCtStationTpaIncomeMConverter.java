package com.taobao.cun.auge.station.convert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.taobao.cun.auge.dal.domain.DwiCtStationTpaIncomeM;
import com.taobao.cun.auge.station.dto.DwiCtStationTpaIncomeMDto;

public final class DwiCtStationTpaIncomeMConverter {

	private DwiCtStationTpaIncomeMConverter(){
		
	}
	
	
	public static List<DwiCtStationTpaIncomeMDto> convert(List<DwiCtStationTpaIncomeM> tpaIncomes) {
		if (CollectionUtils.isEmpty(tpaIncomes)) {
			return Collections.<DwiCtStationTpaIncomeMDto> emptyList();
		}
		List<DwiCtStationTpaIncomeMDto> incomeDtos = new ArrayList<DwiCtStationTpaIncomeMDto>(tpaIncomes.size());
		for (DwiCtStationTpaIncomeM income : tpaIncomes) {
			if (null == income) {
				continue;
			}
			incomeDtos.add(convert(income));
		}
		return incomeDtos;
	}
	

	private static DwiCtStationTpaIncomeMDto convert(DwiCtStationTpaIncomeM income) {
		if (null == income) {
			return null;
		}
		
		DwiCtStationTpaIncomeMDto incomeDto = new DwiCtStationTpaIncomeMDto();
		
		incomeDto.setId(income.getId()) ;
		
		incomeDto.setStatDate(income.getStatDate());
		incomeDto.setBizMonth(income.getBizMonth());
		incomeDto.setCountyOrgId(income.getCountyOrgId());
		incomeDto.setCountyOrgName(income.getCountyOrgName());
		incomeDto.setStationId(income.getStationId());
		incomeDto.setStationName(income.getStationName());
		
		incomeDto.setTpaSrvcCnt1d(income.getTpaSrvcCnt1d());
		incomeDto.setCtSrvcTpaIncomeEstFeeAmtMtd(income.getCtSrvcTpaIncomeEstFeeAmtMtd());
		incomeDto.setCtSrvcTpaIncomeEstFeeAmtAvgMtd(income.getCtSrvcTpaIncomeEstFeeAmtAvgMtd());
		incomeDto.setStationSrvcCnt(income.getStationSrvcCnt());
		incomeDto.setCtSrvcTpaIncomeEstFeeAmtAvgRankMtd(income.getCtSrvcTpaIncomeEstFeeAmtAvgRankMtd());
		
		return incomeDto;
	}
}
