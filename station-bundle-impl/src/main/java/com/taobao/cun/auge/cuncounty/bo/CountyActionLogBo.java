package com.taobao.cun.auge.cuncounty.bo;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyStateEnum;
import com.taobao.cun.auge.log.BizActionEnum;
import com.taobao.cun.auge.log.BizActionLogDto;
import com.taobao.cun.auge.log.bo.BizActionLogBo;

/**
 * 县点动作记录，记录关键动作
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class CountyActionLogBo {
	@Resource
	private BizActionLogBo bizActionLogBo;
	
	/**
	 * 创建时记录
	 * @param countyId
	 * @param operator
	 */
	void addCreateLog(Long countyId, String operator) {
		BizActionLogDto bizActionLogAddDto = new BizActionLogDto();
		bizActionLogAddDto.setBizActionEnum(BizActionEnum.countystation_create);
		bizActionLogAddDto.setGmtCreate(new Date());
		bizActionLogAddDto.setObjectId(countyId);
		bizActionLogAddDto.setObjectType("county");
		bizActionLogAddDto.setOpWorkId(operator);
		bizActionLogBo.addLog(bizActionLogAddDto);
	}
	/**
	 * 状态发生变化时记录日志
	 * @param countyId
	 * @param state
	 * @param operator
	 */
	void addStateLog(Long countyId, String state, String operator) {
		//当前只记录待开业、开业两种状态
		if(state.equals(CuntaoCountyStateEnum.WAIT_OPEN.getCode()) || state.equals(CuntaoCountyStateEnum.OPENING.getCode())) {
			BizActionEnum bizActionEnum = null;
			if(state.equals(CuntaoCountyStateEnum.WAIT_OPEN.getCode())){
				bizActionEnum = BizActionEnum.countystation_wait_open;
			}else {
				bizActionEnum = BizActionEnum.countystation_opening;
			}
			BizActionLogDto bizActionLogAddDto = new BizActionLogDto();
			bizActionLogAddDto.setBizActionEnum(bizActionEnum);
			bizActionLogAddDto.setGmtCreate(new Date());
			bizActionLogAddDto.setObjectId(countyId);
			bizActionLogAddDto.setObjectType("county");
			bizActionLogAddDto.setOpWorkId(operator);
			bizActionLogBo.addLog(bizActionLogAddDto);
		}
	}
}
