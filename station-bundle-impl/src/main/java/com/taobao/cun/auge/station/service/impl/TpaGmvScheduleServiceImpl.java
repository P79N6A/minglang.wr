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
import com.taobao.cun.auge.dal.domain.PartnerInstanceExt;
import com.taobao.cun.auge.dal.example.DwiCtStationTpaIncomeMExmple;
import com.taobao.cun.auge.dal.mapper.DwiCtStationTpaIncomeMExtMapper;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceExtBO;
import com.taobao.cun.auge.station.constant.PartnerInstanceExtConstant;
import com.taobao.cun.auge.station.convert.DwiCtStationTpaIncomeMConverter;
import com.taobao.cun.auge.station.dto.DwiCtStationTpaIncomeMDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceExtDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.service.TpaGmvScheduleService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("tpaGmvScheduleService")
@HSFProvider(serviceInterface = TpaGmvScheduleService.class)
public class TpaGmvScheduleServiceImpl implements TpaGmvScheduleService {

	private static final Logger logger = LoggerFactory.getLogger(TpaGmvScheduleService.class);

	private static final SimpleDateFormat format = new SimpleDateFormat("yyyyMM");

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
		example.setLastMonthCount(PartnerInstanceExtConstant.lastMonthCount);
		example.setScale(PartnerInstanceExtConstant.scale);

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
		Long instanceId = partnerInstanceBO.findPartnerInstanceIdByStationId(stationId);

		// 查询合伙人扩展
		PartnerInstanceExt instanceExt = partnerInstanceExtBO.findPartnerInstanceExt(instanceId);

		// 没有查询到，则插入默认值
		if (null == instanceExt) {
			PartnerInstanceExtDto instanceExtDto = new PartnerInstanceExtDto();

			instanceExtDto.setInstanceId(instanceId);
			instanceExtDto.setMaxChildNum(PartnerInstanceExtConstant.DEFAULT_MAX_CHILD_NUM);
			instanceExtDto.setChildNumChangDate(incomeDto.getBizMonth());
			instanceExtDto.setOperator(operator);

			partnerInstanceExtBO.addPartnerInstanceExt(instanceExtDto);

			return Boolean.TRUE;
		}

		// 上次变更时间
		String lastChangeTime = instanceExt.getChildNumChangDate();

		// 如果上个月的数据已经处理过，则不再变更
		if (lastChangeTime.equals(incomeDto.getBizMonth())) {
			return Boolean.TRUE;
		}

		// 当前最大配额
		Integer curMaxChildNum = instanceExt.getMaxChildNum();

		// 当前子成员数量
		Integer curChildNum = partnerInstanceExtBO.findPartnerChildrenNum(instanceId);

		// 名额未用完，则不增加
		if (curChildNum < curMaxChildNum) {
			return Boolean.TRUE;
		}

		// 已经达到最大配额
		if (curMaxChildNum >= PartnerInstanceExtConstant.MAX_CHILD_NUM) {
			return Boolean.TRUE;
		}
		Integer childNum = curMaxChildNum + PartnerInstanceExtConstant.ADD_NUM_PER;

		// 最大配额校验
		childNum = childNum >= PartnerInstanceExtConstant.MAX_CHILD_NUM ? PartnerInstanceExtConstant.MAX_CHILD_NUM : childNum;

		PartnerInstanceExtDto instanceExtDto = new PartnerInstanceExtDto();

		instanceExtDto.setInstanceId(instanceId);
		instanceExtDto.setMaxChildNum(childNum);
		instanceExtDto.setOperator(operator);
		instanceExtDto.setChildNumChangDate(incomeDto.getBizMonth());

		partnerInstanceExtBO.updatePartnerInstanceExt(instanceExtDto);

		return Boolean.TRUE;
	}

	private String[] findLastNMonth() {
		List<String> lastTwoMonths = new ArrayList<String>(PartnerInstanceExtConstant.lastMonthCount);

		for (int i = PartnerInstanceExtConstant.lastMonthCount; i > 0; i--) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -i);
			String lastMonth = format.format(cal.getTime());

			lastTwoMonths.add(lastMonth);
		}

		return lastTwoMonths.toArray(new String[lastTwoMonths.size()]);
	}
}
