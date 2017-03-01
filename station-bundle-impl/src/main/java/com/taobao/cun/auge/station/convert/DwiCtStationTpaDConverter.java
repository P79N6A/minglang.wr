package com.taobao.cun.auge.station.convert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.cglib.beans.BeanCopier;

import com.taobao.cun.auge.dal.domain.DwiCtStationTpaD;
import com.taobao.cun.auge.station.dto.DwiCtStationTpaDDto;

public final class DwiCtStationTpaDConverter {
	
	private static BeanCopier domain2DtoBeanCopier = BeanCopier.create(DwiCtStationTpaD.class, DwiCtStationTpaDDto.class,
			false);

	private DwiCtStationTpaDConverter(){
		
	}
	
	public static List<DwiCtStationTpaDDto> convert(List<DwiCtStationTpaD> tpaIncomes) {
		if (CollectionUtils.isEmpty(tpaIncomes)) {
			return Collections.<DwiCtStationTpaDDto> emptyList();
		}
		List<DwiCtStationTpaDDto> incomeDtos = new ArrayList<DwiCtStationTpaDDto>(tpaIncomes.size());
		for (DwiCtStationTpaD income : tpaIncomes) {
			if (null == income) {
				continue;
			}
			incomeDtos.add(convert(income));
		}
		return incomeDtos;
	}
	

	private static DwiCtStationTpaDDto convert(DwiCtStationTpaD income) {
		if (null == income) {
			return null;
		}
		
		DwiCtStationTpaDDto incomeDto = new DwiCtStationTpaDDto();
		domain2DtoBeanCopier.copy(income, incomeDto, null);
		return incomeDto;
	}
}
