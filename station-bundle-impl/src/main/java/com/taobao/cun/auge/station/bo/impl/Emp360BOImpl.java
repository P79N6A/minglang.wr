package com.taobao.cun.auge.station.bo.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.common.lang.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.masterdata.client.exception.MasterdataClientException;
import com.alibaba.masterdata.client.model.dataobj.Emp360Info;
import com.alibaba.masterdata.client.model.query.EmpQuery;
import com.alibaba.masterdata.client.model.result.ResultSupport;
import com.alibaba.masterdata.client.service.Employee360Service;
import com.taobao.cun.auge.station.bo.Emp360BO;
import com.taobao.cun.auge.station.dto.EmpInfoDto;
import com.taobao.cun.common.exception.BusinessException;
import com.taobao.cun.common.exception.ParamException;
import com.taobao.util.CollectionUtil;

public class Emp360BOImpl implements Emp360BO {
    public static final Logger logger = LoggerFactory.getLogger(Emp360BOImpl.class);

    @Resource
    private Employee360Service employee360Service;
    private String securityId;


    @Override
    public Map<String, EmpInfoDto> getEmpInfoByWorkNos(List<String> workNos) {
        Map<String, EmpInfoDto> empInfoMap = new HashMap<String, EmpInfoDto>();
        if (CollectionUtil.isEmpty(workNos)) {
            throw new ParamException("workNos is null!");
        }
        Map<String, String> workMap = new HashMap<String, String>();
        for (String workNo : workNos) {
            workMap.put(workNo.replaceFirst("^0+", ""), workNo);
        }
        EmpQuery empQuery = new EmpQuery();
        empQuery.setClientId(Emp360BO.HR_HSF_APP_NAME);
        empQuery.setSecret(securityId);
        empQuery.setWorkNoList(workNos);
        try {
            logger.info("employee360Service.getBatchEmpInfoList query param  : {}", JSON.toJSON(empQuery));
            ResultSupport<List<Emp360Info>> returnResult = employee360Service.getBatchEmpInfoList(empQuery);
            logger.info("employee360Service.getBatchEmpInfoList query result  : {}", JSON.toJSON(returnResult));
            if (returnResult == null || !returnResult.isSuccess()) {
                throw new BusinessException("workNos is null!");
            }
            if (CollectionUtil.isEmpty(returnResult.getResult())) {
                throw new BusinessException("result is null!");
            }
            for (Emp360Info emp360Info : returnResult.getResult()) {
                if (emp360Info != null && StringUtil.isNotBlank(emp360Info.getWorkNo())) {
                    EmpInfoDto EmpInfoDto = new EmpInfoDto();
                    EmpInfoDto.setWorkNo(emp360Info.getWorkNo().replaceFirst("^0+", ""));
                    EmpInfoDto.setLoginAccount(emp360Info.getLoginAccount());
                    EmpInfoDto.setName(emp360Info.getName());
                    EmpInfoDto.setNickName(emp360Info.getNickName());
                    EmpInfoDto.setBuMail(emp360Info.getBuMail());
                    empInfoMap.put(workMap.get(emp360Info.getWorkNo().replaceFirst("^0+", "")), EmpInfoDto);
                }
            }
        } catch (MasterdataClientException e) {
            logger.error("根据工号没有找到员工信息");
            throw new BusinessException("根据工号没有找到员工信息!");
        }
        return empInfoMap;
    }



    public String getName(String workNo) {
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