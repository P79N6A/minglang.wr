package com.taobao.cun.auge.contactrecord.service;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.contactrecord.dto.CuntaoGovContactRecordDetailDto;
import com.taobao.cun.auge.contactrecord.dto.CuntaoGovContactRecordSummaryDto;

import java.util.List;

/**
 * 政府拜访记录查询
 *
 * @author chengyu.zhoucy
 *
 */
public interface CuntaoGovContactRecordQueryService {
    /**
     * 获取详情
     * @param id
     * @return
     */
    CuntaoGovContactRecordDetailDto getCuntaoGovContactDetail(Long id);

    /**
     * 分页获取县服务中心的拜访记录列表
     *
     * @param countyId
     * @param pageNum
     * @return
     */
    PageDto<CuntaoGovContactRecordSummaryDto> queryByCountyId(Long countyId, int pageNum);

    /**
     * 获取县服务中心最新记录摘要
     * @param countyIds
     * @return
     */
    List<CuntaoGovContactRecordSummaryDto> queryLatestRecords(List<Long> countyIds);

    /**
     * 获取县服务中心最新记录详情
     * @param countyId
     * @return
     */
    CuntaoGovContactRecordDetailDto queryLatestRecord(Long countyId);
}
