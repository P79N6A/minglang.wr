package com.taobao.cun.auge.opensearch;

import com.taobao.cun.auge.station.dto.StationOpenSearchDto;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class StationBuyerQueryListDtoConverter {
	private static final String TPName = "村小二";
	private static final String TPAName = "淘帮手";
	private static final String TPVName = "村拍档";

    public static List<StationOpenSearchDto> convertStationBuyerQueryListVoList(List items) {
        if (CollectionUtils.isEmpty(items)) return null;
        List<StationBuyerQueryListVo> voList = (List<StationBuyerQueryListVo>)items;
        List<StationOpenSearchDto> list = new ArrayList<StationOpenSearchDto>();
        for (StationBuyerQueryListVo vo:voList){
            list.add(convertStationBuyerQueryListVo(vo));
        }
        return list;
    }

    private static StationOpenSearchDto convertStationBuyerQueryListVo(StationBuyerQueryListVo vo) {

    	StationOpenSearchDto stationDto = new StationOpenSearchDto();
        if (vo.getVariableValue()!=null && !CollectionUtils.isEmpty(vo.getVariableValue().getExtInfo()) &&vo.getVariableValue().getExtInfo().size()>=1) {
            stationDto.setDistance(vo.getVariableValue().getExtInfo().get(0));
        }
        stationDto.setId(vo.getStationId());
        stationDto.setName(vo.getStationName());
        stationDto.setUserId(vo.getTbUserId());
        stationDto.setUserName(vo.getApplierName());
		stationDto.setAddress(vo.getProvinceDetail() + " " + vo.getCityDetail()
				+ (StringUtils.isBlank(vo.getCountyDetail()) ? "" : (" " + vo.getCountyDetail()))
        + (StringUtils.isBlank(vo.getTownDetail()) ? "" :(" " + vo.getTownDetail())));

        if (null == vo.getOperatorType()) {
            stationDto.setUserType(1);
            stationDto.setUserTypeDesc(TPName);
            return stationDto;
        }

        if (vo.getOperatorType().equals("TPA")) {
            stationDto.setUserType(2);
            stationDto.setUserTypeDesc(TPAName);
        } else if (vo.getOperatorType().equals("TPV")){
            stationDto.setUserType(3);
            stationDto.setUserTypeDesc(TPVName);
        } else {
            stationDto.setUserType(1);
            stationDto.setUserTypeDesc(TPName);
        }

        return stationDto;
    }
}
