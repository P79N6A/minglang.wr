package com.taobao.cun.auge.station.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.dal.domain.DwiCtStationTpaIncomeM;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.example.DwiCtStationTpaIncomeMExmple;
import com.taobao.cun.auge.dal.mapper.DwiCtStationTpaIncomeMExtMapper;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceExtBO;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.StationExceptionEnum;
import com.taobao.cun.auge.station.service.TpaGmvScheduleService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("tpaGmvScheduleService")
@HSFProvider(serviceInterface = TpaGmvScheduleService.class)
public class TpaGmvScheduleServiceImpl implements TpaGmvScheduleService {

	private static final Logger logger = LoggerFactory.getLogger(TpaGmvScheduleService.class);

	// 最近两个月
	private static final Integer lastMonthCount = 2;

	// 排名前20%
	private static final Double scale = 0.2;

	// 最大配额
	private static final Integer MAX_CHILD_NUM = 10;

	// 每次新增名额
	private static final Integer ADD_NUM_PER = 2;

	@Autowired
	DwiCtStationTpaIncomeMExtMapper dwiCtStationTpaIncomeMExtMapper;

	@Autowired
	PartnerInstanceExtBO partnerInstanceExtBO;

	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	@Override
	public List<Long> getWaitAddChildNumStationList(int fetchNum) throws AugeServiceException {
		if (fetchNum < 0) {
			return Collections.<Long> emptyList();
		}

		DwiCtStationTpaIncomeMExmple example = new DwiCtStationTpaIncomeMExmple();

		example.setBizMonths(findLastNMonth());
		example.setLastMonthCount(lastMonthCount);
		example.setScale(scale);

		try {
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
		} catch (Exception e) {
			logger.error("查询合伙人淘帮手连续n个月绩效失败", e);
			return Collections.<Long> emptyList();
		}
	}

	@Override
	public Boolean addChildNumByGmv(Long stationId) {
		// 根据stationId,查询实例id
		PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceByStationId(stationId);
		if (null == rel) {
			logger.error("partner instance is not exist.stationId " + stationId);
			throw new AugeServiceException(StationExceptionEnum.PARTNER_INSTANCE_NOT_EXIST);
		}
		Long instanceId = rel.getId();

		// 查询当前最大配额
		Integer curMaxChildNum = partnerInstanceExtBO.findPartnerMaxChildNum(instanceId);

		// 已经达到最大配额
		if (curMaxChildNum >= MAX_CHILD_NUM) {
			return Boolean.TRUE;
		}
		Integer childNum = curMaxChildNum + ADD_NUM_PER;

		// 最大配额校验
		childNum = childNum >= MAX_CHILD_NUM ? MAX_CHILD_NUM : childNum;

		String operator = OperatorDto.defaultOperator().getOperator();
		partnerInstanceExtBO.updatePartnerMaxChildNum(instanceId, childNum, operator);

		return Boolean.TRUE;
	}

	private String[] findLastNMonth() {
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
