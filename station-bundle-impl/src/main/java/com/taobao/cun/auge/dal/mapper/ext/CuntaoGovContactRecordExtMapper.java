package com.taobao.cun.auge.dal.mapper.ext;

import com.taobao.cun.auge.dal.domain.CuntaoGovContactRecord;

import java.util.List;

public interface CuntaoGovContactRecordExtMapper {
    List<CuntaoGovContactRecord> queryLatestRecords(List<Long> countyIds);
}
