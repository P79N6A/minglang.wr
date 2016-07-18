package com.taobao.cun.auge.station.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.dal.domain.DwiCtStationTpaIncomeM;
import com.taobao.cun.auge.dal.domain.DwiCtStationTpaIncomeMExample;
import com.taobao.cun.auge.dal.domain.DwiCtStationTpaIncomeMExample.Criteria;
import com.taobao.cun.auge.dal.mapper.DwiCtStationTpaIncomeMMapper;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.service.TpaGmvScheduleService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("tpaGmvScheduleService")
@HSFProvider(serviceInterface = TpaGmvScheduleService.class)
public class TpaGmvScheduleServiceImpl implements TpaGmvScheduleService {

	@Autowired
	DwiCtStationTpaIncomeMMapper DwiCtStationTpaIncomeMMapper;

	@Override
	public List<Long> getWaitAddChildNumStationList(int fetchNum) throws AugeServiceException {
		if (fetchNum < 0) {
			return Collections.<Long> emptyList();
		}

		DwiCtStationTpaIncomeMExample example = new DwiCtStationTpaIncomeMExample();

		Criteria criteria = example.createCriteria();
		criteria.andBizMonthIn(findLastTwoMonth());
		
		PageHelper.startPage(1, fetchNum);
		List<DwiCtStationTpaIncomeM> resList = DwiCtStationTpaIncomeMMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(resList)) {
			return Collections.<Long> emptyList();
		}
		List<Long> instanceIdList = new ArrayList<Long>(resList.size());
		for (DwiCtStationTpaIncomeM rel : resList) {
			instanceIdList.add(rel.getStationId());
		}
		return instanceIdList;
	}

	private List<String> findLastTwoMonth() {
		List<String> lastTwoMonths = new ArrayList<String>();

		for (int i = 1; i < 3; i++) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -i);
			SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
			String lastMonth = format.format(cal.getTime());

			lastTwoMonths.add(lastMonth);
		}

		return lastTwoMonths;
	}
}
