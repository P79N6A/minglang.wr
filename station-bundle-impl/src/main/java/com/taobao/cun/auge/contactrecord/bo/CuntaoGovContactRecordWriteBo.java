package com.taobao.cun.auge.contactrecord.bo;

import com.alibaba.fastjson.JSONArray;
import com.google.common.base.Strings;
import com.taobao.cun.auge.common.utils.BeanCopy;
import com.taobao.cun.auge.contactrecord.dto.CuntaoGovContactRecordAddDto;
import com.taobao.cun.auge.dal.domain.CuntaoGovContactRecord;
import com.taobao.cun.auge.dal.domain.CuntaoGovContactRecordRisk;
import com.taobao.cun.auge.dal.domain.CuntaoGovContactRecordRiskExample;
import com.taobao.cun.auge.dal.mapper.CuntaoGovContactRecordMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoGovContactRecordRiskMapper;
import com.taobao.cun.auge.station.adapter.Emp360Adapter;
import com.taobao.cun.auge.validator.BeanValidator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Component
public class CuntaoGovContactRecordWriteBo {
    @Resource
    private CuntaoGovContactRecordMapper cuntaoGovContactRecordMapper;
    @Resource
    private CuntaoGovContactRecordRiskMapper cuntaoGovContactRecordRiskMapper;
    @Resource
    private Emp360Adapter emp360Adapter;
    /**
     * 添加政府联系小记
     * @param cuntaoGovContactRecordAddDto
     */
    @Transactional(rollbackFor = Throwable.class)
    public Long add(CuntaoGovContactRecordAddDto cuntaoGovContactRecordAddDto){
        BeanValidator.validateWithThrowable(cuntaoGovContactRecordAddDto);

        CuntaoGovContactRecord cuntaoGovContactRecord = BeanCopy.copy(CuntaoGovContactRecord.class, cuntaoGovContactRecordAddDto);
        cuntaoGovContactRecord.setCreator(cuntaoGovContactRecordAddDto.getOperator());
        cuntaoGovContactRecord.setModifier(cuntaoGovContactRecordAddDto.getOperator());
        cuntaoGovContactRecord.setVisitorWorkId(cuntaoGovContactRecordAddDto.getOperator());
        cuntaoGovContactRecord.setVisitorName(emp360Adapter.getName(cuntaoGovContactRecordAddDto.getOperator()));
        cuntaoGovContactRecord.setGmtCreate(new Date());
        cuntaoGovContactRecord.setGmtModified(new Date());
        cuntaoGovContactRecordMapper.insert(cuntaoGovContactRecord);

        if(!Strings.isNullOrEmpty(cuntaoGovContactRecordAddDto.getRiskInfos())){
            List<CuntaoGovContactRecordRisk> risks = JSONArray.parseArray(cuntaoGovContactRecordAddDto.getRiskInfos(), CuntaoGovContactRecordRisk.class);
            risks.forEach(r->{
                r.setCreator(cuntaoGovContactRecordAddDto.getOperator());
                r.setModifier(cuntaoGovContactRecordAddDto.getOperator());
                r.setGmtCreate(new Date());
                r.setGmtModified(new Date());
                r.setContactId(cuntaoGovContactRecord.getId());
                cuntaoGovContactRecordRiskMapper.insert(r);
            });
        }

        return cuntaoGovContactRecord.getId();
    }

    /**
     * 删除记录
     * @param id
     */
    @Transactional(rollbackFor = Throwable.class)
    public void delete(Long id, String operator){
        CuntaoGovContactRecord cuntaoGovContactRecord = cuntaoGovContactRecordMapper.selectByPrimaryKey(id);
        if(cuntaoGovContactRecord != null){
            cuntaoGovContactRecord.setGmtModified(new Date());
            cuntaoGovContactRecord.setIsDeleted("y");
            cuntaoGovContactRecord.setModifier(operator);
            cuntaoGovContactRecordMapper.updateByPrimaryKey(cuntaoGovContactRecord);

            CuntaoGovContactRecordRisk cuntaoGovContactRecordRisk = new CuntaoGovContactRecordRisk();
            CuntaoGovContactRecordRiskExample cuntaoGovContactRecordRiskExample = new CuntaoGovContactRecordRiskExample();
            cuntaoGovContactRecordRisk.setGmtModified(new Date());
            cuntaoGovContactRecordRisk.setIsDeleted("y");
            cuntaoGovContactRecordRisk.setModifier(operator);
            cuntaoGovContactRecordRiskExample.createCriteria().andContactIdEqualTo(id);
            cuntaoGovContactRecordRiskMapper.updateByExampleSelective(cuntaoGovContactRecordRisk, cuntaoGovContactRecordRiskExample);
        }
    }
}
