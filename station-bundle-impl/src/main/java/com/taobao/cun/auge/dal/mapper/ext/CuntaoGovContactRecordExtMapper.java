package com.taobao.cun.auge.dal.mapper.ext;

import com.taobao.cun.auge.dal.domain.CuntaoGovContactRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CuntaoGovContactRecordExtMapper {
    List<CuntaoGovContactRecord> queryLatestRecords(@Param("countyIds") List<Long> countyIds, @Param("contactWay") String contactWay);
}
