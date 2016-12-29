package com.taobao.cun.auge.station.adapter.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.common.lang.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.masterdata.client.exception.MasterdataClientException;
import com.alibaba.masterdata.client.model.dataobj.Emp360Info;
import com.alibaba.masterdata.client.model.query.EmpQuery;
import com.alibaba.masterdata.client.model.result.ResultSupport;
import com.alibaba.masterdata.client.service.Employee360Service;
import com.taobao.cun.auge.station.adapter.Emp360Adapter;
import com.taobao.cun.auge.station.dto.EmpInfoDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.util.CollectionUtil;

@Component("emp360Adapter")
public class Emp360AdapterImpl implements Emp360Adapter {

	public static final Logger logger = LoggerFactory.getLogger(Emp360Adapter.class);

	@Resource
	private Employee360Service employee360Service;
	
	@Value("${hr.security.key}")
	private String securityId;

	@Override
	public Map<String, EmpInfoDto> getEmpInfoByWorkNos(List<String> workNos) throws AugeServiceException {
		Map<String, EmpInfoDto> empInfoMap = new HashMap<String, EmpInfoDto>();
		if (CollectionUtil.isEmpty(workNos)) {
			throw new AugeServiceException("workNos is null!");
		}
		Map<String, String> workMap = new HashMap<String, String>();
		for (String workNo : workNos) {
			workMap.put(workNo.replaceFirst("^0+", ""), workNo);
		}
		EmpQuery empQuery = new EmpQuery();
		empQuery.setClientId(Emp360Adapter.HR_HSF_APP_NAME);
		empQuery.setSecret(securityId);
		empQuery.setWorkNoList(workNos);
		try {
			ResultSupport<List<Emp360Info>> returnResult = employee360Service.getBatchEmpInfoList(empQuery);
			if (returnResult == null || !returnResult.isSuccess()) {
				throw new AugeServiceException("workNos is null!");
			}
			if (CollectionUtil.isEmpty(returnResult.getResult())) {
				throw new AugeServiceException("result is null!");
			}
			for (Emp360Info emp360Info : returnResult.getResult()) {
				if (emp360Info != null && StringUtil.isNotBlank(emp360Info.getWorkNo())) {
					EmpInfoDto EmpInfoDto = new EmpInfoDto();
					EmpInfoDto.setWorkNo(emp360Info.getWorkNo().replaceFirst("^0+", ""));
					EmpInfoDto.setLoginAccount(emp360Info.getLoginAccount());
					EmpInfoDto.setName(emp360Info.getName());
					EmpInfoDto.setNickName(emp360Info.getNickName());
					EmpInfoDto.setBuMail(emp360Info.getBuMail());
					EmpInfoDto.setMobile(emp360Info.getMobile());
					empInfoMap.put(workMap.get(emp360Info.getWorkNo().replaceFirst("^0+", "")), EmpInfoDto);
				}
			}
		} catch (MasterdataClientException e) {
			logger.error("根据工号没有找到员工信息");
			throw new AugeServiceException("根据工号没有找到员工信息!");
		}
		return empInfoMap;
	}

	public String getName(String workNo) throws AugeServiceException {
		String name = "";
		if (StringUtils.isNotEmpty(workNo)) {
			List<String> workNoList = new ArrayList<String>();
			workNoList.add(workNo);
			Map<String, EmpInfoDto> EmpInfoDtoMap = getEmpInfoByWorkNos(workNoList);
			if (EmpInfoDtoMap != null && EmpInfoDtoMap.size() > 0) {
				EmpInfoDto EmpInfoDto = EmpInfoDtoMap.get(workNo);
				if (EmpInfoDto != null) {
					name = EmpInfoDto.getName();
				}
			}
		}
		return name;
	}

	public String getSecurityId() {
		return securityId;
	}

	public void setSecurityId(String securityId) {
		this.securityId = securityId;
	}
}