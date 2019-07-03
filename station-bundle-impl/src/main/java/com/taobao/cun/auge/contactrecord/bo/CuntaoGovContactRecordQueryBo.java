package com.taobao.cun.auge.contactrecord.bo;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.BeanCopy;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.contactrecord.dto.CuntaoGovContactRecordDetailDto;
import com.taobao.cun.auge.contactrecord.dto.CuntaoGovContactRecordRiskDto;
import com.taobao.cun.auge.contactrecord.dto.CuntaoGovContactRecordSummaryDto;
import com.taobao.cun.auge.contactrecord.enums.CuntaoGovContactRiskStateEnum;
import com.taobao.cun.auge.contactrecord.enums.CuntaoGovContactRiskLevelEnum;
import com.taobao.cun.auge.contactrecord.enums.CuntaoGovContactRiskTypeEnum;
import com.taobao.cun.auge.contactrecord.enums.CuntaoGovContactRecordWayEnum;
import com.taobao.cun.auge.dal.domain.CuntaoGovContactRecord;
import com.taobao.cun.auge.dal.domain.CuntaoGovContactRecordExample;
import com.taobao.cun.auge.dal.domain.CuntaoGovContactRecordRisk;
import com.taobao.cun.auge.dal.domain.CuntaoGovContactRecordRiskExample;
import com.taobao.cun.auge.dal.mapper.CuntaoGovContactRecordMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoGovContactRecordRiskMapper;
import com.taobao.cun.auge.dal.mapper.ext.CuntaoGovContactRecordExtMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 政府拜访记录查询
 *
 * @author chengyu.zhoucy
 *
 */
@Component
public class CuntaoGovContactRecordQueryBo {
    @Resource
    private CuntaoGovContactRecordMapper cuntaoGovContactRecordMapper;
    @Resource
    private CuntaoGovContactRecordExtMapper cuntaoGovContactRecordExtMapper;
    @Resource
    private CuntaoGovContactRecordRiskMapper cuntaoGovContactRecordRiskMapper;

    /**
     * 获取详情
     * @param id
     * @return
     */
    public CuntaoGovContactRecordDetailDto getCuntaoGovContactDetail(Long id){
        return toCuntaoGovContactRecordDetailDto(cuntaoGovContactRecordMapper.selectByPrimaryKey(id));
    }

    private List<CuntaoGovContactRecordRiskDto> getCuntaoGovContactRecordRisks(Long id){
        CuntaoGovContactRecordRiskExample cuntaoGovContactRecordRiskExample = new CuntaoGovContactRecordRiskExample();
        cuntaoGovContactRecordRiskExample.createCriteria().andContactIdEqualTo(id).andIsDeletedEqualTo("n");
        return toCuntaoGovContactRecordRiskDtos(cuntaoGovContactRecordRiskMapper.selectByExample(cuntaoGovContactRecordRiskExample));
    }

    private CuntaoGovContactRecordDetailDto toCuntaoGovContactRecordDetailDto(CuntaoGovContactRecord cuntaoGovContactRecord){
        if(cuntaoGovContactRecord == null){
            return null;
        }
        CuntaoGovContactRecordDetailDto detail = BeanCopy.copy(CuntaoGovContactRecordDetailDto.class, cuntaoGovContactRecord);
        detail.setContactWayEnum(CuntaoGovContactRecordWayEnum.valueof(cuntaoGovContactRecord.getContactWay()));
        detail.setRisks(getCuntaoGovContactRecordRisks(detail.getId()));

        return detail;
    }

    private List<CuntaoGovContactRecordRiskDto> toCuntaoGovContactRecordRiskDtos(List<CuntaoGovContactRecordRisk> risks){
        if(CollectionUtils.isNotEmpty(risks)){
            return risks.stream().map(r->{
                CuntaoGovContactRecordRiskDto dto = new CuntaoGovContactRecordRiskDto();
                dto.setContactId(r.getContactId());
                dto.setRiskDesc(r.getRiskDesc());
                dto.setRiskLevel(CuntaoGovContactRiskLevelEnum.valueof(r.getRiskLevel()));
                dto.setRiskType(CuntaoGovContactRiskTypeEnum.valueof(r.getRiskType()));
                dto.setState(CuntaoGovContactRiskStateEnum.valueof(r.getState()));
                return dto;
            }).collect(Collectors.toList());
        }else{
            return Lists.newArrayList();
        }
    }

    /**
     * 查询一个县的拜访记录
     * @param countyId
     * @return
     */
    public PageDto<CuntaoGovContactRecordSummaryDto> queryByCountyId(Long countyId, int pageNum){
        CuntaoGovContactRecordExample example = new CuntaoGovContactRecordExample();
        example.createCriteria().andCountyIdEqualTo(countyId).andIsDeletedEqualTo("n");
        example.setOrderByClause(" id desc ");

        PageHelper.startPage(pageNum, 20);
        Page<CuntaoGovContactRecord> result = (Page<CuntaoGovContactRecord>)cuntaoGovContactRecordMapper.selectByExample(example);
        return PageDtoUtil.success(result, BeanCopy.copyList(CuntaoGovContactRecordSummaryDto.class, result.getResult()));
    }

    public List<CuntaoGovContactRecordSummaryDto> queryLatestRecords(List<Long> countyIds) {
        return BeanCopy.copyList(CuntaoGovContactRecordSummaryDto.class,cuntaoGovContactRecordExtMapper.queryLatestRecords(countyIds));
    }

    public CuntaoGovContactRecordDetailDto queryLatestRecord(Long countyId) {
        List<CuntaoGovContactRecord> cuntaoGovContactRecords = cuntaoGovContactRecordExtMapper.queryLatestRecords(Lists.newArrayList(countyId));
        if(CollectionUtils.isNotEmpty(cuntaoGovContactRecords)){
            return toCuntaoGovContactRecordDetailDto(cuntaoGovContactRecords.get(0));
        }
        return null;
    }
}
