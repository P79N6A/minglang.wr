package com.taobao.cun.auge.station.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.example.DwiCtStationTpaIncomeMExmple;
import com.taobao.cun.auge.dal.mapper.DwiCtStationTpaIncomeMExtMapper;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceExtBO;
import com.taobao.cun.auge.station.constant.PartnerInstanceExtConstant;
import com.taobao.cun.auge.station.convert.DwiCtStationTpaIncomeMConverter;
import com.taobao.cun.auge.station.dto.DwiCtStationTpaIncomeMDto;
import com.taobao.cun.auge.station.dto.ForcedCloseDto;
import com.taobao.cun.auge.station.dto.PartnerChildMaxNumUpdateDto;
import com.taobao.cun.auge.station.enums.CloseStationApplyCloseReasonEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerMaxChildNumChangeReasonEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.service.PartnerInstanceExtService;
import com.taobao.cun.auge.station.service.PartnerInstanceService;
import com.taobao.cun.auge.station.service.TpaGmvScheduleService;
import com.taobao.cun.auge.station.util.DateTimeUtil;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("tpaGmvScheduleService")
@HSFProvider(serviceInterface = TpaGmvScheduleService.class)
public class TpaGmvScheduleServiceImpl implements TpaGmvScheduleService {

	private static final Logger logger = LoggerFactory.getLogger(TpaGmvScheduleService.class);

	

	@Autowired
	DwiCtStationTpaIncomeMExtMapper dwiCtStationTpaIncomeMExtMapper;

	@Autowired
	PartnerInstanceExtBO partnerInstanceExtBO;

	@Autowired
	PartnerInstanceBO partnerInstanceBO;
	
	@Autowired
	PartnerInstanceExtService partnerInstanceExtService;
	
	@Autowired
	PartnerInstanceService partnerInstanceService;

	@Override
	public PageDto<DwiCtStationTpaIncomeMDto> getWaitAddChildNumStationList(PageQuery pageQuery) throws AugeServiceException {
		try {
			// 参数校验
			BeanValidator.validateWithThrowable(pageQuery);

			DwiCtStationTpaIncomeMExmple example = new DwiCtStationTpaIncomeMExmple();

			example.setBizMonths(findLastNMonth(PartnerInstanceExtConstant.LAST_MONTHS_4_REWARD_PARENT_NUM));
			example.setLastMonthCount(PartnerInstanceExtConstant.LAST_MONTHS_4_REWARD_PARENT_NUM);
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
			String lastMonth = DateTimeUtil.MONTH_FORMAT.format(cal.getTime());

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

			example.setBizMonths(findLastNMonth(PartnerInstanceExtConstant.LAST_MONTHS_4_AUTO_CLOSE));
			example.setLastMonthCount(PartnerInstanceExtConstant.LAST_MONTHS_4_AUTO_CLOSE);
			example.setGmvLimit(PartnerInstanceExtConstant.GMV_LIMIT_4_AUTO_CLOSE);
			example.setOrderNumLimit(PartnerInstanceExtConstant.ORDER_LIMIT_4_AUTO_CLOSE);

			PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
			Page<DwiCtStationTpaIncomeM> page = dwiCtStationTpaIncomeMExtMapper.selectStationsByExample(example);
			return PageDtoUtil.success(page, DwiCtStationTpaIncomeMConverter.convert(page));
		} catch (Exception e) {
			logger.error("查询淘帮手连续n个月绩效失败", e);
			return PageDtoUtil.unSuccess(pageQuery.getPageNum(), pageQuery.getPageSize());
		}
	}

	@Override
	public Boolean autoCloseByGmv(DwiCtStationTpaIncomeMDto incomeDto) {
		logger.info("Start to auto close tpa station.DwiCtStationTpaIncomeMDto=" + JSON.toJSONString(incomeDto));

		PartnerStationRel tpaInstance = partnerInstanceBO.findPartnerInstanceByStationId(incomeDto.getStationId());
		// DA算的是淘帮手绩效月报表，出报表和定时钟开始运营之间的时间段，可能淘帮手处于停业申请中，已停业，退出申请中，已退出，或者退出，重新入驻为其他合伙人，不处理
		if (null == tpaInstance || !PartnerInstanceStateEnum.SERVICING.getCode().equals(tpaInstance.getState())) {
			logger.warn("Failed to auto close tpa station,instance state is not servicing.DwiCtStationTpaIncomeMDto="
					+ JSON.toJSONString(incomeDto));
			return Boolean.TRUE;
			// DA算的是淘帮手绩效月报表，出报表和定时钟开始运营之间的时间段，可能淘帮手升级了，tpa实例不存在了
		} else if (!PartnerInstanceTypeEnum.TPA.getCode().equals(tpaInstance.getType())) {
			logger.warn("Failed to auto close tpa station,instance type is not tpa.DwiCtStationTpaIncomeMDto="
					+ JSON.toJSONString(incomeDto));
			return Boolean.TRUE;
		}

		// 服务开始时间，是否在考核期间，入驻第一个月不在考核期间
		if (isInCheckPeriod(tpaInstance.getServiceBeginTime(), incomeDto.getBizMonth())) {
			ForcedCloseDto forcedCloseDto = new ForcedCloseDto();

			forcedCloseDto.setInstanceId(tpaInstance.getId());
			forcedCloseDto.setReason(CloseStationApplyCloseReasonEnum.ASSESS_FAIL);
			forcedCloseDto.copyOperatorDto(OperatorDto.defaultOperator());

			partnerInstanceService.applyCloseBySystem(forcedCloseDto);
		} else {
			logger.warn("Failed to auto close tpa station,tpa is not during the check period.DwiCtStationTpaIncomeMDto="
					+ JSON.toJSONString(incomeDto));
		}
		logger.info("End to auto close tpa station.DwiCtStationTpaIncomeMDto=" + JSON.toJSONString(incomeDto));
		return Boolean.TRUE;
	}
	
	/**
	 * 是否在考核期间：2017年1月开始服务，2017年4月才开始考核，考核2月，3月的绩效，第一个月不算考核
	 * @param serviceBeginTime 淘帮手村点服务开始时间
	 * @param bizMonth 连续两个月，绩效不达标时，最近一个月的月份。例如1月，2月绩效不达标，bizMonth=201702
	 * @return
	 */
	private boolean isInCheckPeriod(Date serviceBeginTime, String bizMonth) {
		return DateTimeUtil.getMonthOffset(serviceBeginTime, bizMonth) > 1;
	}

	public static void main(String[] args) {
		TpaGmvScheduleServiceImpl test = new TpaGmvScheduleServiceImpl();
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 2017);
		c.set(Calendar.MONTH, 11);
		test.isInCheckPeriod(c.getTime(), "201802");
	}
}
