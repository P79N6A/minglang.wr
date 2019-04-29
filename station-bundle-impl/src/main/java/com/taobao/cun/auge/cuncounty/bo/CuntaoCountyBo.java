package com.taobao.cun.auge.cuncounty.bo;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyDto;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyStateEnum;
import com.taobao.cun.auge.cuncounty.utils.BeanConvertUtils;
import com.taobao.cun.auge.dal.domain.CuntaoCounty;
import com.taobao.cun.auge.dal.mapper.CuntaoCountyMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.exception.AugeBusinessException;

/**
 * 县服务中心
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class CuntaoCountyBo {
	@Resource
	private CuntaoCountyMapper cuntaoCountyMapper;
	@Resource
	private CainiaoCountyRemoteBo cainiaoCountyRemoteBo;
	@Resource
	private CountyActionLogBo countyActionLogBo;
	@Resource
	private StationBO stationBO;
	@Value("${county.apply.open.servicingstation.num}")
	private Integer servicingStateNum;
	
	public CuntaoCountyDto getCuntaoCounty(Long id) {
		CuntaoCounty cuntaoCounty = cuntaoCountyMapper.selectByPrimaryKey(id);
		if(cuntaoCounty == null) {
			return null;
		}
		CuntaoCountyDto cuntaoCountyDto = BeanConvertUtils.convert(CuntaoCountyDto.class, cuntaoCountyMapper.selectByPrimaryKey(id));
		cuntaoCountyDto.setState(cuntaoCounty.getState());
		return cuntaoCountyDto;
	}
	
	public void updateState(Long countyId, String state, String operator) {
		CuntaoCounty cuntaoCounty = cuntaoCountyMapper.selectByPrimaryKey(countyId);
		if(cuntaoCounty != null) {
			cuntaoCounty.setState(state);
			if(operator != null) {
				cuntaoCounty.setModifier(operator);
			}
			cuntaoCounty.setGmtModified(new Date());
			cuntaoCountyMapper.updateByPrimaryKey(cuntaoCounty);
			countyActionLogBo.addStateLog(countyId, state, cuntaoCounty.getModifier());
		}
	}
	
	public void startOperate(Long countyId) {
		CuntaoCounty cuntaoCounty = cuntaoCountyMapper.selectByPrimaryKey(countyId);
		cuntaoCounty.setGmtModified(new Date());
		cuntaoCounty.setState(CuntaoCountyStateEnum.WAIT_OPEN.getCode());
		cuntaoCounty.setOperateDate(DateUtils.truncate(new Date(), Calendar.DATE));
		cuntaoCountyMapper.updateByPrimaryKey(cuntaoCounty);
		countyActionLogBo.addStateLog(countyId, CuntaoCountyStateEnum.WAIT_OPEN.getCode(), cuntaoCounty.getModifier());
	}
	
	public void applyOpen(Long countyId, String operator) {
		CuntaoCountyDto cuntaoCountyDto = getCuntaoCounty(countyId);
		if(cuntaoCountyDto == null) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "县服务中心不存在");
		}
		if(cuntaoCountyDto.getState().getCode().equals(CuntaoCountyStateEnum.WAIT_OPEN.getCode())) {
			int servicingStationNum = stationBO.getServicingNumByOrgId(cuntaoCountyDto.getOrgId());
			if(servicingStationNum < servicingStateNum) {
				throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "服务中站点数:" + servicingStationNum + ",不满足:" + servicingStateNum + "个的条件");
			}
			if(!cainiaoCountyRemoteBo.isOperating(countyId)) {
				throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "菜鸟侧的县仓尚未开业，不具备县点开业条件");
			}
			updateState(countyId, CuntaoCountyStateEnum.OPENING.getCode(), operator);
		}else {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "当前状态不能申请开业");
		}
	}
}
