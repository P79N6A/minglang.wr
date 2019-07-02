package com.taobao.cun.auge.contactrecord.service;

import com.taobao.cun.auge.contactrecord.bo.CuntaoGovContactRecordWriteBo;
import com.taobao.cun.auge.contactrecord.dto.CuntaoGovContactRecordAddDto;
import com.taobao.cun.auge.cuncounty.service.CuntaoCountyGovContactService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

import javax.annotation.Resource;

@HSFProvider(serviceInterface = CuntaoCountyGovContactService.class)
public class CuntaoGovContactRecordWriteImpl implements CuntaoGovContactRecordWrite{
    @Resource
    private CuntaoGovContactRecordWriteBo cuntaoGovContactRecordWriteBo;

    @Override
    public Long add(CuntaoGovContactRecordAddDto cuntaoGovContactRecordAddDto){
        return cuntaoGovContactRecordWriteBo.add(cuntaoGovContactRecordAddDto);
    }

    @Override
    public void delete(Long id, String operator) {
        cuntaoGovContactRecordWriteBo.delete(id, operator);
    }
}
