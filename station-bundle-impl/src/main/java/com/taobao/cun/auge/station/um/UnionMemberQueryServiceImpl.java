package com.taobao.cun.auge.station.um;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.station.condition.PartnerInstancePageCondition;
import com.taobao.cun.auge.station.condition.UnionMemberPageCondition;
import com.taobao.cun.auge.station.convert.UnionMemberConverter;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
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

    /**
     * 由于优盟的状态名称和村小二不同，所以需要转化一下
     *
     * @param stationId
     * @param operatorDto
     * @return
     */
    @Override
    public UnionMemberDto getUnionMember(Long stationId, OperatorDto operatorDto) {
        PartnerInstanceDto instanceDto = partnerInstanceQueryService.queryInfo(stationId, operatorDto);
        return UnionMemberConverter.convert(instanceDto);
    }

    @Override
    public PageDto<UnionMemberDto> queryByPage(UnionMemberPageCondition pageCondition) {
        PartnerInstancePageCondition instancePageCondition = UnionMemberConverter.convert(pageCondition);
        PageDto<PartnerInstanceDto> instanceDtoPageDto = partnerInstanceQueryService.queryByPage(instancePageCondition);
        return UnionMemberConverter.convert(instanceDtoPageDto);
    }
}
