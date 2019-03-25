package com.taobao.cun.auge.cuncounty.bo;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyDto;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyStateEnum;
import com.taobao.cun.auge.cuncounty.dto.edit.CuntaoCountyUpdateDto;
import com.taobao.cun.auge.cuncounty.exception.IllegalCountyStateException;

/**
 * 更新，根据状态确定能否更新，更新后是否需要审批
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class CuntaoCountyUpdateFacade {
	@Resource
	private CuntaoCountyWriteBo cuntaoCountyWriteBo;
	@Resource
	private CuntaoCountyQueryBo cuntaoCountyQueryBo;
	@Resource
	private CuntaoCountyWaitOpenProcessBo cuntaoCountyWaitOpenProcessBo;
	
	@Transactional(rollbackFor=Throwable.class)
	public void update(CuntaoCountyUpdateDto cuntaoCountyUpdateDto) {
		CuntaoCountyDto cuntaoCountyDto = cuntaoCountyQueryBo.getCuntaoCounty(cuntaoCountyUpdateDto.getCountyId());
		if(cuntaoCountyDto.getState().getCode().equals(CuntaoCountyStateEnum.WAIT_OPEN_AUDIT.getCode())) {
			throw new IllegalCountyStateException("当前状态:" + cuntaoCountyDto.getState().getDesc() + ",不允许修改");
		}
		//修改信息
		cuntaoCountyWriteBo.updateCuntaoCounty(cuntaoCountyUpdateDto);
		//如果需要审批
		if(cuntaoCountyWaitOpenProcessBo.isNeedAuditState(cuntaoCountyDto)) {
			cuntaoCountyWaitOpenProcessBo.start(cuntaoCountyUpdateDto.getCountyId(), cuntaoCountyUpdateDto.getOperator());
		}
		
	}
}
