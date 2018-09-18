package com.taobao.cun.auge.station.convert;

import java.util.List;
import java.util.stream.Collectors;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.station.condition.PartnerInstancePageCondition;
import com.taobao.cun.auge.station.condition.UnionMemberPageCondition;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
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

        StationDto stationDto = instanceDto.getStationDto();
        PartnerDto partnerDto = instanceDto.getPartnerDto();
        UnionMemberDto unionMemberDto = new UnionMemberDto();
        if (null != stationDto) {
            unionMemberDto.setStationId(stationDto.getId());
            unionMemberDto.setStationNum(stationDto.getStationNum());
            unionMemberDto.setStationName(stationDto.getName());
            unionMemberDto.setAddress(stationDto.getAddress());
            unionMemberDto.setFormat(stationDto.getFormat());
            unionMemberDto.setCovered(stationDto.getCovered());
            unionMemberDto.setDescription(stationDto.getDescription());
            unionMemberDto.setOrgId(stationDto.getApplyOrg());
        }

        if (null != partnerDto) {
            unionMemberDto.setAlipayAccount(partnerDto.getAlipayAccount());
            unionMemberDto.setTaobaoNick(partnerDto.getTaobaoNick());
            unionMemberDto.setTaobaoUserId(partnerDto.getTaobaoUserId());
            unionMemberDto.setName(partnerDto.getName());
            unionMemberDto.setIdenNum(partnerDto.getIdenNum());
            unionMemberDto.setMobile(partnerDto.getMobile());
        }
        unionMemberDto.setParentStationId(instanceDto.getParentStationId());
        //unionMemberDto.setParentStationDto();

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

        instancePageCondition.copyOperatorDto(pageCondition);
        return instancePageCondition;
    }
}
