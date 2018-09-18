package com.taobao.cun.auge.station.convert;

import java.util.List;
import java.util.stream.Collectors;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.station.condition.PartnerInstancePageCondition;
import com.taobao.cun.auge.station.condition.UnionMemberPageCondition;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.um.dto.UnionMemberDto;
import com.taobao.cun.auge.station.um.enums.UnionMemberStateEnum;
import org.apache.commons.collections.CollectionUtils;

public final class UnionMemberConverter {

    private UnionMemberConverter() {

    }

    public static UnionMemberDto convert(PartnerInstanceDto instanceDto) {
        if (null == instanceDto) {
            return null;
        }

        UnionMemberDto unionMemberDto = new UnionMemberDto();

        unionMemberDto.setStationDto(instanceDto.getStationDto());
        unionMemberDto.setPartnerDto(instanceDto.getPartnerDto());

        unionMemberDto.setParentStationId(instanceDto.getParentStationId());
        unionMemberDto.setInstanceId(instanceDto.getId());
        unionMemberDto.setState(UnionMemberStateEnum.valueof(instanceDto.getState().getCode()));

        return unionMemberDto;
    }

    public static PageDto<UnionMemberDto> convert(PageDto<PartnerInstanceDto> instanceDtoPageDto) {

        PageDto<UnionMemberDto> pageDto = new PageDto<UnionMemberDto>();
        if (null == instanceDtoPageDto) {
            return pageDto;
        }
        if (CollectionUtils.isNotEmpty(instanceDtoPageDto.getItems())) {
            List<UnionMemberDto> unionMemberDtos = instanceDtoPageDto.getItems().stream().map(
                instanceDto -> convert(instanceDto)).collect(
                Collectors.toList());
            pageDto.setItems(unionMemberDtos);
        }
        pageDto.setPageNum(instanceDtoPageDto.getPageNum());
        pageDto.setPageSize(instanceDtoPageDto.getPageSize());
        pageDto.setTotal(instanceDtoPageDto.getTotal());

        pageDto.setCurPagesize(instanceDtoPageDto.getCurPagesize());
        pageDto.setPages(instanceDtoPageDto.getPages());
        pageDto.setPrePage(instanceDtoPageDto.getPrePage());
        pageDto.setNextPage(instanceDtoPageDto.getNextPage());

        pageDto.setFirstPage(instanceDtoPageDto.getFirstPage());

        pageDto.setLastPage(instanceDtoPageDto.getLastPage());

        return pageDto;
    }

    public static PartnerInstancePageCondition convert(UnionMemberPageCondition pageCondition) {
        PartnerInstancePageCondition instancePageCondition = new PartnerInstancePageCondition();
        instancePageCondition.setStationName(pageCondition.getStationName());

        if (null != pageCondition.getState()) {
            instancePageCondition.setInstanceState(
                PartnerInstanceStateEnum.valueof(pageCondition.getState().getCode()));
        }

        instancePageCondition.setParentStationId(pageCondition.getParentStationId());
        instancePageCondition.setStationNum(pageCondition.getStationNum());
        instancePageCondition.setOrgIdPath(pageCondition.getOrgIdPath());
        instancePageCondition.setTaobaoNick(pageCondition.getTaobaoNick());

        instancePageCondition.setPartnerType(PartnerInstanceTypeEnum.UM);
        instancePageCondition.setIsCurrent(Boolean.TRUE);
        instancePageCondition.copyOperatorDto(pageCondition);
        return instancePageCondition;
    }
}
