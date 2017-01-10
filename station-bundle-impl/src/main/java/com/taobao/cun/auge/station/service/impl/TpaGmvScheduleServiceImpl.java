package com.taobao.cun.auge.station.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.PageQuery;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.dal.domain.DwiCtStationTpaIncomeM;
import com.taobao.cun.auge.dal.domain.PartnerInstanceExt;
import com.taobao.cun.auge.dal.example.DwiCtStationTpaIncomeMExmple;
import com.taobao.cun.auge.dal.mapper.DwiCtStationTpaIncomeMExtMapper;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceExtBO;
import com.taobao.cun.auge.station.constant.PartnerInstanceExtConstant;
import com.taobao.cun.auge.station.convert.DwiCtStationTpaIncomeMConverter;
import com.taobao.cun.auge.station.dto.DwiCtStationTpaIncomeMDto;
import com.taobao.cun.auge.station.dto.PartnerChildMaxNumUpdateDto;
import com.taobao.cun.auge.station.enums.PartnerMaxChildNumChangeReasonEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.service.PartnerInstanceExtService;
import com.taobao.cun.auge.station.service.TpaGmvScheduleService;
import com.taobao.cun.auge.validator.BeanValidator;
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
	
	@Autowired
	PartnerInstanceExtService partnerInstanceExtService;

	@Override
	public PageDto<DwiCtStationTpaIncomeMDto> getWaitAddChildNumStationList(PageQuery pageQuery) throws AugeServiceException {
		try {
			// 参数校验
			BeanValidator.validateWithThrowable(pageQuery);

			DwiCtStationTpaIncomeMExmple example = new DwiCtStationTpaIncomeMExmple();

			example.setBizMonths(findLastNMonth(PartnerInstanceExtConstant.REWARD_PARENT_NUM_LAST_MONTH_COUNT));
			example.setLastMonthCount(PartnerInstanceExtConstant.REWARD_PARENT_NUM_LAST_MONTH_COUNT);
			example.setScale(PartnerInstanceExtConstant.SCALE);

			PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
			Page<DwiCtStationTpaIncomeM> page = dwiCtStationTpaIncomeMExtMapper.selectStationsByExample(example);
			return PageDtoUtil.success(page, DwiCtStationTpaIncomeMConverter.convert(page));
		} catch (Exception e) {
			logger.error("查询合伙人淘帮手连续n个月绩效失败", e);
			return PageDtoUtil.unSuccess(pageQuery.getPageNum(), pageQuery.getPageSize());
		}
	}

	@Override
	public Boolean addChildNumByGmv(DwiCtStationTpaIncomeMDto incomeDto) {
		logger.info("Start to add child max num.DwiCtStationTpaIncomeMDto=" + JSON.toJSONString(incomeDto));

		Long stationId = incomeDto.getStationId();
		// 根据stationId,查询实例id
		Long instanceId = partnerInstanceBO.findPartnerInstanceIdByStationId(stationId);
		// 查询合伙人扩展
		PartnerInstanceExt instanceExt = partnerInstanceExtBO.findPartnerInstanceExt(instanceId);

		// 没有查询到，则插入默认值
		String bizMonth = incomeDto.getBizMonth();
		if (null == instanceExt) {
			partnerInstanceExtService.initPartnerMaxChildNum(instanceId, PartnerInstanceExtConstant.DEFAULT_MAX_CHILD_NUM, OperatorDto.defaultOperator());
			return Boolean.TRUE;
		}

		// 上次变更时间
		String lastChangeTime = instanceExt.getChildNumChangDate();

		// 如果上个月的数据已经处理过，则不再变更
		if (null != lastChangeTime && lastChangeTime.equals(bizMonth)) {
			logger.info("data has handled.instanceId=" + instanceId + " bizMonth= " + bizMonth);
			return Boolean.TRUE;
		}

		// 当前最大配额
		Integer curMaxChildNum = instanceExt.getMaxChildNum();

		// 当前子成员数量
		Integer curChildNum = partnerInstanceExtBO.findPartnerChildrenNum(instanceId);

		// 名额未用完，则不增加
		if (curChildNum < curMaxChildNum) {
			logger.info("child num not run out.instanceId=" + instanceId + " curChildNum= " + curChildNum
					+ " curMaxChildNum=" + curMaxChildNum);
			return Boolean.TRUE;
		}

		// 已经达到最大配额
		if (curMaxChildNum >= PartnerInstanceExtConstant.MAX_CHILD_NUM) {
			logger.info("child num reaches a maximun.instanceId=" + instanceId + " curMaxChildNum=" + curMaxChildNum);
			return Boolean.TRUE;
		}
		Integer childNum = curMaxChildNum + PartnerInstanceExtConstant.ADD_NUM_PER;

		// 最大配额校验
		childNum = childNum >= PartnerInstanceExtConstant.MAX_CHILD_NUM ? PartnerInstanceExtConstant.MAX_CHILD_NUM
				: childNum;

		//更新配额
		updatePartnerChildMaxNum(instanceId, bizMonth, childNum);
		
		logger.info(
				"change child maxNum.instanceId=" + instanceId + " childNum=" + childNum + " bizMonth= " + bizMonth);
		return Boolean.TRUE;
	}

	private void updatePartnerChildMaxNum(Long instanceId, String bizMonth, Integer childNum) {
		PartnerChildMaxNumUpdateDto updateDto = new PartnerChildMaxNumUpdateDto();
		updateDto.setInstanceId(instanceId);
		updateDto.setMaxChildNum(childNum);
		updateDto.setChildNumChangDate(bizMonth);
		updateDto.copyOperatorDto(OperatorDto.defaultOperator());
		updateDto.setReason(PartnerMaxChildNumChangeReasonEnum.TPA_PERFORMANCE_REWARD);

		partnerInstanceExtService.updatePartnerMaxChildNum(updateDto);
	}

	private String[] findLastNMonth(Integer lastMonthCount) {
		List<String> lastTwoMonths = new ArrayList<String>(lastMonthCount);

		for (int i = lastMonthCount; i > 0; i--) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -i);
			String lastMonth = format.format(cal.getTime());

			lastTwoMonths.add(lastMonth);
		}

		return lastTwoMonths.toArray(new String[lastTwoMonths.size()]);
	}

	@Override
	public PageDto<DwiCtStationTpaIncomeMDto> getWaitClosingTpaList(PageQuery pageQuery) throws AugeServiceException {
		try {
			// 参数校验
			BeanValidator.validateWithThrowable(pageQuery);

			DwiCtStationTpaIncomeMExmple example = new DwiCtStationTpaIncomeMExmple();

			example.setBizMonths(findLastNMonth(PartnerInstanceExtConstant.AUTO_CLOSE_LAST_MONTH_COUNT));
			example.setLastMonthCount(PartnerInstanceExtConstant.AUTO_CLOSE_LAST_MONTH_COUNT);
			example.setGmvLimit(PartnerInstanceExtConstant.GMV_LIMIT);
			example.setOrderNumLimit(PartnerInstanceExtConstant.ORDER_LIMIT);

			PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
			Page<DwiCtStationTpaIncomeM> page = dwiCtStationTpaIncomeMExtMapper.selectStationsByExample(example);
			return PageDtoUtil.success(page, DwiCtStationTpaIncomeMConverter.convert(page));
		} catch (Exception e) {
			logger.error("查询淘帮手连续n个月绩效失败", e);
			return PageDtoUtil.unSuccess(pageQuery.getPageNum(), pageQuery.getPageSize());
		}
	}
}
