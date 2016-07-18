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
import com.taobao.cun.auge.station.convert.DwiCtStationTpaIncomeMConverter;
import com.taobao.cun.auge.station.dto.DwiCtStationTpaIncomeMDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceExtDto;
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
	
	//默认初始化配额
	private final static Integer DEFAULT_MAX_CHILD_NUM = 3;

	@Autowired
	DwiCtStationTpaIncomeMExtMapper dwiCtStationTpaIncomeMExtMapper;

	@Autowired
	PartnerInstanceExtBO partnerInstanceExtBO;

	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	@Override
	public List<DwiCtStationTpaIncomeMDto> getWaitAddChildNumStationList(int fetchNum) throws AugeServiceException {
		if (fetchNum < 0) {
			return Collections.<DwiCtStationTpaIncomeMDto> emptyList();
		}

		DwiCtStationTpaIncomeMExmple example = new DwiCtStationTpaIncomeMExmple();

		example.setBizMonths(findLastNMonth());
		example.setLastMonthCount(lastMonthCount);
		example.setScale(scale);

		try {
			PageHelper.startPage(1, fetchNum);
			List<DwiCtStationTpaIncomeM> resList = dwiCtStationTpaIncomeMExtMapper.selectStationsByExample(example);
			if (CollectionUtils.isEmpty(resList)) {
				return Collections.<DwiCtStationTpaIncomeMDto> emptyList();
			}
			return DwiCtStationTpaIncomeMConverter.convert(resList);
		} catch (Exception e) {
			logger.error("查询合伙人淘帮手连续n个月绩效失败", e);
			return Collections.<DwiCtStationTpaIncomeMDto> emptyList();
		}
	}

	@Override
	public Boolean addChildNumByGmv(DwiCtStationTpaIncomeMDto incomeDto) {
		String operator = OperatorDto.defaultOperator().getOperator();
		
		Long stationId = incomeDto.getStationId();
		// 根据stationId,查询实例id
		PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceByStationId(stationId);
		if (null == rel) {
			logger.error("partner instance is not exist.stationId " + stationId);
			throw new AugeServiceException(StationExceptionEnum.PARTNER_INSTANCE_NOT_EXIST);
		}
		Long instanceId = rel.getId();

		// 查询当前最大配额
		Integer curMaxChildNum = partnerInstanceExtBO.findPartnerCurMaxChildNum(instanceId);
		
		//没有查询到，则插入默认值
		if(null == curMaxChildNum){
			PartnerInstanceExtDto instanceExtDto = new PartnerInstanceExtDto();
			
			instanceExtDto.setInstanceId(instanceId);
			instanceExtDto.setMaxChildNum(DEFAULT_MAX_CHILD_NUM);
			instanceExtDto.setOperator(operator);
			
			partnerInstanceExtBO.addPartnerInstanceExt(instanceExtDto);
			
			return Boolean.TRUE;
		}

		// 已经达到最大配额
		if (curMaxChildNum >= MAX_CHILD_NUM) {
			return Boolean.TRUE;
		}
		Integer childNum = curMaxChildNum + ADD_NUM_PER;

		// 最大配额校验
		childNum = childNum >= MAX_CHILD_NUM ? MAX_CHILD_NUM : childNum;
		
		
		PartnerInstanceExtDto instanceExtDto = new PartnerInstanceExtDto();
		
		instanceExtDto.setInstanceId(instanceId);
		instanceExtDto.setMaxChildNum(childNum);
		instanceExtDto.setOperator(operator);

		partnerInstanceExtBO.updatePartnerInstanceExt(instanceExtDto);

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
