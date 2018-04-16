package com.taobao.cun.auge.inspection;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.dal.domain.InspectionStatusSummary;
import com.taobao.cun.auge.dal.domain.InspectionStation;
import com.taobao.cun.auge.dal.example.InspectionStationExample;
import com.taobao.cun.auge.dal.mapper.InspectionStationMapper;
import com.taobao.cun.auge.inspection.condition.InspectionPagedCondition;
import com.taobao.cun.auge.inspection.condition.InspectionStationTypes;
import com.taobao.cun.auge.inspection.condition.InspectionStatus;
import com.taobao.cun.auge.inspection.dto.InspectionStationDto;
import com.taobao.cun.auge.inspection.dto.InspectionStatusSummaryDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.user.dto.CuntaoUserOrgVO;
import com.taobao.cun.auge.user.dto.UserRoleEnum;
import com.taobao.cun.auge.user.service.CuntaoUserOrgService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("inspectionStationQueryService")
@HSFProvider(serviceInterface = InspectionStationQueryService.class)
public class InspectionStationQueryServiceImpl implements InspectionStationQueryService {

	@Autowired
	private InspectionStationMapper partnerInstanceInspectionMapper;
	
	@Autowired
	private CuntaoUserOrgService cuntaoUserOrgService;
	private static final BeanCopier partnerInstanceInspectionCopier = BeanCopier.create(InspectionStation.class, InspectionStationDto.class, false);

	@Override
	public PageDto<InspectionStationDto> queryByPage(InspectionPagedCondition condition) {
		InspectionStationExample example = new InspectionStationExample();
		example.setOrgIdPath(condition.getOrgIdPath());
		example.setState(condition.getState());
		example.setStationName(condition.getStationName());
		example.setStoreCategory(condition.getStoreCategory());
		example.setMode(getVersion(condition.getType()));
		example.setType(getType(condition.getType()));
		example.setLevel(condition.getLevel());
		example.setServiceBeginDate(condition.getServiceBeginDate());
		example.setInspectionState(condition.getInspectionState());
		PageHelper.startPage(condition.getPageNum(), condition.getPageSize());
		Page<InspectionStation> inspections = partnerInstanceInspectionMapper.selectInspectionStationByExample(example);
		PageDto<InspectionStationDto> success = PageDtoUtil.success(inspections, convert(inspections));
		return success;
	}

	
	private List<InspectionStationDto> convert(List<InspectionStation> inspections){
		List<InspectionStationDto> results = Lists.newArrayList();
		for(InspectionStation inspection : inspections){
			InspectionStationDto dto = new InspectionStationDto();
			partnerInstanceInspectionCopier.copy(inspection, dto, null);
			dto.setStateDesc(PartnerInstanceStateEnum.valueof(dto.getState()).getDesc());
			/*List<CuntaoUserOrgVO> userOrg = cuntaoUserOrgService.getCuntaoOrgUsers( Lists.newArrayList(dto.getApplyOrg()),  Lists.newArrayList(UserRoleEnum.COUNTY_LEADER.getCode()));
			if(userOrg != null){
				List<String> leaders = userOrg.stream().map(user -> user.getUserName()).collect(Collectors.toList());
				dto.setLeaders(leaders);
			}*/
			results.add(dto);
		}
		return results;
	}


	@Override
	public InspectionStatusSummaryDto countInspectionStatusSummary(
			InspectionPagedCondition condition) {
		InspectionStationExample example = new InspectionStationExample();
		example.setOrgIdPath(condition.getOrgIdPath());
		example.setState(condition.getState());
		example.setStationName(condition.getStationName());
		example.setStoreCategory(condition.getStoreCategory());
		example.setMode(getVersion(condition.getType()));
		example.setType(getType(condition.getType()));
		example.setLevel(condition.getLevel());
		example.setInspectionState(condition.getInspectionState());
		List<InspectionStatusSummary> summary = partnerInstanceInspectionMapper.countInspectionSummaryByExample(example);
		InspectionStatusSummaryDto result = new InspectionStatusSummaryDto();
		Integer hasInspection = getInspetionStatusCount(InspectionStatus.HAS_INSPECTION,summary);
		Integer planInspection = getInspetionStatusCount(InspectionStatus.PLAN_INSPECTION,summary);
		Integer unInspection = getInspetionStatusCount(InspectionStatus.UNINSPECTION,summary);
		PageDto<InspectionStationDto> page = queryByPage(condition);
		result.setHasInspectionNum(hasInspection);
		result.setPlanInspectionNum(planInspection);
		result.setUnInspectionNum(unInspection);
		result.setTotalInspectionNum(Integer.parseInt(page.getTotal()+""));
		return result;
	}
	
	private Integer getInspetionStatusCount(String status,List<InspectionStatusSummary> summarys){
		for(InspectionStatusSummary summary : summarys){
			if(status.equals(summary.getStatus())){
				return summary.getCount();
			}
		}
		return 0;
	}
	
	
	public static String getVersion(String type){
		if(InspectionStationTypes.TP_V4.equals(type)){
			return "v4";
		}
		return null;
	}
	
	public static String getType(String type){
		if(InspectionStationTypes.TP_V4.equals(type)){
			return "TP";
		}
		return type;
	}
}
