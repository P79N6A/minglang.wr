package com.taobao.cun.auge.inspection;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.taobao.cun.auge.sop.inspection.enums.InspectionStateEnum;
import com.taobao.cun.auge.sop.inspection.enums.InspectionTypeEnum;
import com.taobao.cun.recruit.contact.dto.CuntaoContactRecordDto;
import com.taobao.cun.recruit.contact.enums.VisitTypeEnum;
import com.taobao.cun.recruit.contact.service.CuntaoContactRecordService;
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
import com.taobao.cun.auge.inspection.dto.InspectionStationDto;
import com.taobao.cun.auge.inspection.dto.InspectionStatusSummaryDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.user.service.CuntaoUserOrgService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

import javax.annotation.Resource;

@Service("inspectionStationQueryService")
@HSFProvider(serviceInterface = InspectionStationQueryService.class)
public class InspectionStationQueryServiceImpl implements InspectionStationQueryService {

	@Autowired
	private InspectionStationMapper partnerInstanceInspectionMapper;
	@Resource
	private CuntaoContactRecordService cuntaoContactRecordService;
	
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
		example.setLevel(condition.getLevel());
		example.setStates(condition.getStates());
		example.setType(condition.getType());
		example.setServiceBeginDate(condition.getServiceBeginDate());
		example.setInspectionState(condition.getInspectionState());
		example.setInspectionType(getInspectionType(condition.getInspectionState()));
		PageHelper.startPage(condition.getPageNum(), condition.getPageSize(),true,false);
		Page<InspectionStation> inspections = partnerInstanceInspectionMapper.selectInspectionStationByExample(example);
		PageDto<InspectionStationDto> success = PageDtoUtil.success(inspections, initUpgradeRecord(convert(inspections)));
		return success;
	}

	private String getInspectionType(String inspectionState) {
		//待审批 未自检 属于自检的状态
		if (InspectionStateEnum.TO_AUDIT.getCode().equalsIgnoreCase(inspectionState) || InspectionStateEnum.UN_PARTER_INSPECTION.getCode().equalsIgnoreCase(inspectionState)) {
			return InspectionTypeEnum.PARTNER_INSPECTION.getCode();
		} else if (InspectionStateEnum.UNINSPECTION.getCode().equalsIgnoreCase(inspectionState)) {
			return InspectionTypeEnum.INSPECTION.getCode();
		}
		return null;
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

	private List<InspectionStationDto> initUpgradeRecord(List<InspectionStationDto> inspectionStationDtos){
        List<Long> stationIds = inspectionStationDtos.stream().map(i->i.getStationId()).distinct().collect(Collectors.toList());
        if(stationIds == null || stationIds.isEmpty()){
        	return inspectionStationDtos;
		}
        List<CuntaoContactRecordDto> cuntaoContactRecordDtos = cuntaoContactRecordService.queryLatestRecords(VisitTypeEnum.UPGRADE.getCode(), stationIds);
        if(cuntaoContactRecordDtos != null){
			Map<Long,CuntaoContactRecordDto> map = cuntaoContactRecordDtos.stream().collect(Collectors.toMap(r->r.getStationId(), r->r));
			return inspectionStationDtos.stream().peek(i->{
				CuntaoContactRecordDto record = map.get(i.getStationId());
				if(record != null){
					i.setUpgradeRiskGrade(record.getRiskGrade());
					i.setUpgradePlan(record.getUpgradePlan());
				}
			}).collect(Collectors.toList());
		}

        return inspectionStationDtos;
    }


	@Override
	public InspectionStatusSummaryDto countInspectionStatusSummary(
			InspectionPagedCondition condition) {
		InspectionStationExample example = new InspectionStationExample();
		example.setOrgIdPath(condition.getOrgIdPath());
		example.setState(condition.getState());
		example.setStates(condition.getStates());
		example.setStationName(condition.getStationName());
		example.setStoreCategory(condition.getStoreCategory());
		example.setType(condition.getType());
		example.setLevel(condition.getLevel());
		example.setInspectionState(condition.getInspectionState());
		List<InspectionStatusSummary> summary = partnerInstanceInspectionMapper.countInspectionSummaryByExample(example);
		InspectionStatusSummaryDto result = new InspectionStatusSummaryDto();
		//未巡检
		Integer unInspection = getInspetionStatusCount(InspectionStateEnum.UNINSPECTION.getCode(), summary);
		//待审批
		Integer toAudit = getInspetionStatusCount(InspectionStateEnum.TO_AUDIT.getCode(), summary);
		//未自检
		Integer unPartnerInspection = getInspetionStatusCount(InspectionStateEnum.UN_PARTER_INSPECTION.getCode(), summary);
		PageDto<InspectionStationDto> page = queryByPage(condition);
		result.setUnInspectionNum(unInspection);
		result.setToAuditInspectionNum(toAudit);
		result.setUnPartnerInspectionNum(unPartnerInspection);
		result.setTotalInspectionNum(Integer.parseInt(page.getTotal() + ""));
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
}
