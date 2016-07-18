package com.taobao.cun.auge.station.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.dal.domain.DwiCtStationTpaIncomeM;
import com.taobao.cun.auge.dal.example.DwiCtStationTpaIncomeMExmple;
import com.taobao.cun.auge.dal.mapper.DwiCtStationTpaIncomeMExtMapper;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.service.TpaGmvScheduleService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("tpaGmvScheduleService")
@HSFProvider(serviceInterface = TpaGmvScheduleService.class)
public class TpaGmvScheduleServiceImpl implements TpaGmvScheduleService {

	// 最近两个月
	private static final Integer lastMonthCount = 2;

	//排名前20%
	private static final Double scale = 0.2;

	@Autowired
	DwiCtStationTpaIncomeMExtMapper dwiCtStationTpaIncomeMExtMapper;

	@Override
	public List<Long> getWaitAddChildNumStationList(int fetchNum) throws AugeServiceException {
		if (fetchNum < 0) {
			return Collections.<Long> emptyList();
		}

		DwiCtStationTpaIncomeMExmple example = new DwiCtStationTpaIncomeMExmple();

		example.setBizMonths(findLastTwoMonth());
		example.setLastMonthCount(lastMonthCount);
		example.setScale(scale);

		PageHelper.startPage(1, fetchNum);
		List<DwiCtStationTpaIncomeM> resList = dwiCtStationTpaIncomeMExtMapper.selectStationsByExample(example);
		if (CollectionUtils.isEmpty(resList)) {
			return Collections.<Long> emptyList();
		}
		List<Long> instanceIdList = new ArrayList<Long>(resList.size());
		for (DwiCtStationTpaIncomeM rel : resList) {
			instanceIdList.add(rel.getStationId());
		}
		return instanceIdList;
	}

	private String[] findLastTwoMonth() {
		List<String> lastTwoMonths = new ArrayList<String>();

		for (int i = lastMonthCount; i > 0; i--) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -i);
			SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
			String lastMonth = format.format(cal.getTime());

			lastTwoMonths.add(lastMonth);
		}

		return lastTwoMonths.toArray(new String[lastTwoMonths.size()]);
	}
}
