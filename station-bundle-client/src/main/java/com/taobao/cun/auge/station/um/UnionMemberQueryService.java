package com.taobao.cun.auge.station.um;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.station.condition.UnionMemberPageCondition;
import com.taobao.cun.auge.station.dto.UnionMemberDto;

/**
 * 优盟查询服务
 *
 * @author haihu.fhh
 */
public interface UnionMemberQueryService {

    /**
     * 根据stationId，查询优盟信息
     *
     * @param stationId
     * @return
     */
    UnionMemberDto getUnionMember(Long stationId, OperatorDto operatorDto);

    /**
     * 分页查询优盟
     *
     * @param pageCondition
     * @return
     */
    PageDto<UnionMemberDto> queryByPage(UnionMemberPageCondition pageCondition);

}
