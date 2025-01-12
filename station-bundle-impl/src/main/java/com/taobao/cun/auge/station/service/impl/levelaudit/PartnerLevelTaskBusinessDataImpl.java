package com.taobao.cun.auge.station.service.impl.levelaudit;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.dal.domain.PartnerLevelTaskBusinessData;
import com.taobao.cun.auge.dal.domain.PartnerLevelTaskBusinessDataExample;
import com.taobao.cun.auge.dal.mapper.PartnerLevelTaskBusinessDataMapper;
import com.taobao.cun.auge.evaluate.dto.PartnerLevelTaskBusinessDataDTO;
import com.taobao.cun.auge.evaluate.enums.LevelTaskDataTypeEnum;
import com.taobao.cun.auge.evaluate.service.PartnerLevelTaskBusinessDataService;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

/**
 * Created by xujianhui on 16/12/13.
 */

@Service("partnerLevelTaskBusinessDataService")
@HSFProvider(serviceInterface = PartnerLevelTaskBusinessDataService.class)
public class PartnerLevelTaskBusinessDataImpl implements PartnerLevelTaskBusinessDataService {

    @Autowired
    private PartnerLevelTaskBusinessDataMapper partnerLevelTaskBusinessDataMapper;

    @Override
    public Boolean saveTaskBusinessData(String processInstanceId, Long taskId, PartnerLevelTaskBusinessDataDTO businessDataDTO) {
        BeanValidator.validateWithThrowable(businessDataDTO);
        Assert.notNull(processInstanceId);
        PartnerLevelTaskBusinessDataExample example = getPartnerLevelTaskBusinessDataExample(processInstanceId, taskId, businessDataDTO);
        List<PartnerLevelTaskBusinessData> dataList = partnerLevelTaskBusinessDataMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(dataList)){
            PartnerLevelTaskBusinessData data = getPartnerLevelTaskBusinessData(true, processInstanceId, taskId, businessDataDTO);
            int count = partnerLevelTaskBusinessDataMapper.insert(data);
            return count > 0 ;
        }else{
            PartnerLevelTaskBusinessData data = getPartnerLevelTaskBusinessData(false, processInstanceId, taskId, businessDataDTO);
            int count = partnerLevelTaskBusinessDataMapper.updateByExampleSelective(data, example);
            return count > 0;
        }
    }

    private PartnerLevelTaskBusinessDataExample getPartnerLevelTaskBusinessDataExample(String processInstanceId, Long taskId, PartnerLevelTaskBusinessDataDTO businessDataDTO) {
        PartnerLevelTaskBusinessDataExample example = new PartnerLevelTaskBusinessDataExample();
        PartnerLevelTaskBusinessDataExample.Criteria criteria = example.createCriteria();
        criteria.andProcessInstanceIdEqualTo(processInstanceId).andInfoTypeEqualTo(businessDataDTO.getInfoType().name());
        if(taskId!=null){
            criteria.andTaskNodeEqualTo(taskId);
        }
        if(businessDataDTO.getId()!=null) {
            criteria.andIdEqualTo(businessDataDTO.getId());
        }
        return example;
    }

    @Override
    public List<PartnerLevelTaskBusinessDataDTO> queryByAuditedPersonId(Long auditedPersonId, LevelTaskDataTypeEnum dataType) {
        Assert.notNull(auditedPersonId);
        Assert.notNull(dataType);
        PartnerLevelTaskBusinessDataExample example = new PartnerLevelTaskBusinessDataExample();
        example.createCriteria().andAuditedPersonIdEqualTo(auditedPersonId).andInfoTypeEqualTo(dataType.name());
        List<PartnerLevelTaskBusinessData> dataList = partnerLevelTaskBusinessDataMapper.selectByExample(example);
        return toDtoList(dataList);
    }

    @Override
    public List<PartnerLevelTaskBusinessDataDTO> queryByProcessInstanceAndTaskId(String processInstanceId, Long taskId, LevelTaskDataTypeEnum dataType) {
        Assert.notNull(processInstanceId);
        Assert.notNull(dataType);
        PartnerLevelTaskBusinessDataExample example = new PartnerLevelTaskBusinessDataExample();
        if(taskId!=null) {
            example.createCriteria().andProcessInstanceIdEqualTo(processInstanceId).andInfoTypeEqualTo(dataType.name()).andTaskNodeEqualTo(taskId);
        }else {
            example.createCriteria().andProcessInstanceIdEqualTo(processInstanceId).andInfoTypeEqualTo(dataType.name());
        }
        List<PartnerLevelTaskBusinessData> dataList = partnerLevelTaskBusinessDataMapper.selectByExample(example);
        return toDtoList(dataList);
    }


    private static List<PartnerLevelTaskBusinessDataDTO> toDtoList(List<PartnerLevelTaskBusinessData> dataList) {
        if(CollectionUtils.isEmpty(dataList)) {
            return Collections.emptyList();
        }

        List<PartnerLevelTaskBusinessDataDTO> dtoList = Lists.newArrayList();
        for(PartnerLevelTaskBusinessData data:dataList) {
            PartnerLevelTaskBusinessDataDTO dto = new PartnerLevelTaskBusinessDataDTO();
            dto.setExtendsInfo(data.getExtendsInfo());
            dto.setTaskBusinessInfo(data.getTaskBusinessInfo());
            dto.setParticipants(data.getParticipants());
            dto.setAuditedPersonId(data.getAuditedPersonId());
            if(StringUtils.isNotBlank(data.getInfoType())) {
                dto.setInfoType(LevelTaskDataTypeEnum.valueOf(data.getInfoType()));
            }
            dto.setAuditStatus(data.getAuditStatus());
            dto.setAuditPersonName(data.getAuditPersonName());
            dto.setProcessInstanceId(data.getProcessInstanceId());
            if(data.getTaskNode()!=null) {
                dto.setTaskId(Long.valueOf(data.getTaskNode()));
            }
            dtoList.add(dto);
        }
        return dtoList;
    }

    private PartnerLevelTaskBusinessData getPartnerLevelTaskBusinessData(boolean isCreate, String processInstanceId, Long taskId, PartnerLevelTaskBusinessDataDTO businessDataDTO) {
        PartnerLevelTaskBusinessData data = new PartnerLevelTaskBusinessData();
        data.setAuditedPersonId(businessDataDTO.getAuditedPersonId());
        data.setAuditPersonName(businessDataDTO.getAuditPersonName());
        Date now = new Date();
        if(isCreate) {
            data.setGmtCreate(now);
            data.setGmtModified(now);
        }else {
            data.setGmtModified(now);
        }
        data.setInfoType(businessDataDTO.getInfoType().name());
        data.setParticipants(businessDataDTO.getParticipants());
        data.setProcessInstanceId(processInstanceId);
        data.setTaskNode(taskId);
        data.setAuditStatus(businessDataDTO.getAuditStatus());
        data.setTaskBusinessInfo(businessDataDTO.getTaskBusinessInfo());
        data.setExtendsInfo(businessDataDTO.getExtendsInfo());
        return data;
    }
}
