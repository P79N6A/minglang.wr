package com.taobao.cun.auge.contactrecord.service;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.contactrecord.bo.CuntaoGovContactRecordQueryBo;
import com.taobao.cun.auge.contactrecord.dto.CuntaoGovContactRecordDetailDto;
import com.taobao.cun.auge.contactrecord.dto.CuntaoGovContactRecordSummaryDto;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

import javax.annotation.Resource;
import java.util.List;

@HSFProvider(serviceInterface = CuntaoGovContactRecordQueryService.class)
public class CuntaoGovContactRecordQueryServiceImpl implements CuntaoGovContactRecordQueryService{
    @Resource
    private CuntaoGovContactRecordQueryBo cuntaoGovContactRecordQueryBo;

    @Override
    public CuntaoGovContactRecordDetailDto getCuntaoGovContactDetail(Long id){
        return cuntaoGovContactRecordQueryBo.getCuntaoGovContactDetail(id);
    }

    @Override
    public PageDto<CuntaoGovContactRecordSummaryDto> queryByCountyId(Long countyId, int pageNum){
        return cuntaoGovContactRecordQueryBo.queryByCountyId(countyId, pageNum);
    }

    @Override
    public List<CuntaoGovContactRecordSummaryDto> queryLatestRecords(List<Long> countyIds) {
        return cuntaoGovContactRecordQueryBo.queryLatestRecords(countyIds);
    }

    @Override
    public CuntaoGovContactRecordDetailDto queryLatestRecord(Long countyId) {
        return cuntaoGovContactRecordQueryBo.queryLatestRecord(countyId);
    }
}
