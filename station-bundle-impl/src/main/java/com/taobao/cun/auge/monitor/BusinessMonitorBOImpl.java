package com.taobao.cun.auge.monitor;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.BusinessMonitor;
import com.taobao.cun.auge.dal.domain.BusinessMonitorExample;
import com.taobao.cun.auge.dal.mapper.BusinessMonitorMapper;

@Component
public class BusinessMonitorBOImpl implements BusinessMonitorBO{

	@Autowired
	BusinessMonitorMapper businessMonitorMapper;
	@Override
	public void addBusinessMonitor(String businessCode, Long businessKey,String errorCode,String errorMessage) {
		BusinessMonitorExample example = new BusinessMonitorExample();
		example.createCriteria().andBusinessCodeEqualTo(businessCode).andBusinessKeyEqualTo(businessKey).andIsDeletedEqualTo("n");
		List<BusinessMonitor> monitors = businessMonitorMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(monitors)){
			BusinessMonitor monitor = new BusinessMonitor();
			monitor.setIsDeleted("n");
			monitor.setCreator("system");
			monitor.setModifier("system");
			monitor.setGmtCreate(new Date());
			monitor.setGmtModified(new Date());
			monitor.setBusinessCode(businessCode);
			monitor.setBusinessKey(businessKey);
			monitor.setIsFixed("n");
			monitor.setErrorCode(errorCode);
			monitor.setErrorMessage(errorMessage);
			businessMonitorMapper.insertSelective(monitor);
		}else{
			BusinessMonitor monitor = new BusinessMonitor();
			monitor.setModifier("system");
			monitor.setGmtModified(new Date());
			monitor.setBusinessCode(businessCode);
			monitor.setBusinessKey(businessKey);
			businessMonitorMapper.updateByExampleSelective(monitor, example);
		}
	}

	@Override
	public void fixBusinessMonitor(String businessCode, Long businessKey) {
		BusinessMonitor monitor = new BusinessMonitor();
		monitor.setIsFixed("y");
		BusinessMonitorExample example = new BusinessMonitorExample();
		example.createCriteria().andBusinessCodeEqualTo(businessCode).andBusinessKeyEqualTo(businessKey).andIsDeletedEqualTo("n");
		List<BusinessMonitor> monitors = businessMonitorMapper.selectByExample(example);
		if(CollectionUtils.isNotEmpty(monitors)){
			businessMonitorMapper.updateByExampleSelective(monitor, example);
		}
	}

}
