package com.taobao.cun.auge.station.um;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.station.condition.UnionMemberPageCondition;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import com.taobao.cun.auge.station.um.dto.UnionMemberDto;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.springframework.stereotype.Service;

/**
 * 优盟查询服务
 *
 * @author haihu.fhh
 */
@Service("unionMemberQueryService")
@HSFProvider(serviceInterface = UnionMemberQueryService.class, clientTimeout = 7000)
public class UnionMemberQueryServiceImpl implements UnionMemberQueryService {

    private PartnerInstanceQueryService partnerInstanceQueryService;

    @Override
    public UnionMemberDto getUnionMember(Long stationId, OperatorDto operatorDto) {
        return null;
    }

    @Override
    public PageDto<UnionMemberDto> queryByPage(UnionMemberPageCondition pageCondition) {
        return null;
    }
}
