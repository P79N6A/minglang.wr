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
import com.taobao.cun.auge.dal.domain.PartnerInstanceInspection;
import com.taobao.cun.auge.dal.example.PartnerInstanceInspectionExample;
import com.taobao.cun.auge.dal.mapper.PartnerInstanceInspectionMapper;
import com.taobao.cun.auge.inspection.condition.PartnerInstanceInspectionPagedCondition;
import com.taobao.cun.auge.inspection.dto.PartnerInstanceInspectionDto;
import com.taobao.cun.auge.user.dto.CuntaoUserOrgVO;
import com.taobao.cun.auge.user.dto.UserRoleEnum;
import com.taobao.cun.auge.user.service.CuntaoUserOrgService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("partnerInstanceInspectionQueryService")
@HSFProvider(serviceInterface = PartnerInstanceInspectionQueryService.class)
public class PartnerInstanceInspectionQueryServiceImpl implements PartnerInstanceInspectionQueryService {

	@Autowired
	private PartnerInstanceInspectionMapper partnerInstanceInspectionMapper;
	
	@Autowired
	private CuntaoUserOrgService cuntaoUserOrgService;
	private static final BeanCopier partnerInstanceInspectionCopier = BeanCopier.create(PartnerInstanceInspection.class, PartnerInstanceInspectionDto.class, false);

	@Override
	public PageDto<PartnerInstanceInspectionDto> queryByPage(PartnerInstanceInspectionPagedCondition condition) {
		PartnerInstanceInspectionExample example = new PartnerInstanceInspectionExample();
		example.setOrgIdPath(condition.getOrgIdPath());
		example.setState(condition.getState());
		example.setStationName(condition.getStationName());
		example.setStoreCategory(condition.getStoreCategory());
		example.setMode(condition.getMode());
		example.setType(condition.getType());
		PageHelper.startPage(condition.getPageNum(), condition.getPageSize());
		Page<PartnerInstanceInspection> inspections = partnerInstanceInspectionMapper.selectPartnerInstanceInspectionByExample(example);
		PageDto<PartnerInstanceInspectionDto> success = PageDtoUtil.success(inspections, convert(inspections));
		return success;
	}

	
	private List<PartnerInstanceInspectionDto> convert(List<PartnerInstanceInspection> inspections){
		List<PartnerInstanceInspectionDto> results = Lists.newArrayList();
		for(PartnerInstanceInspection inspection : inspections){
			PartnerInstanceInspectionDto dto = new PartnerInstanceInspectionDto();
			partnerInstanceInspectionCopier.copy(inspection, dto, null);
			List<CuntaoUserOrgVO> userOrg = cuntaoUserOrgService.getCuntaoOrgUsers( Lists.newArrayList(dto.getApplyOrg()),  Lists.newArrayList(UserRoleEnum.COUNTY_LEADER.getCode()));
			if(userOrg != null){
				List<String> leaders = userOrg.stream().map(user -> user.getUserName()).collect(Collectors.toList());
				dto.setLeaders(leaders);
			}
			results.add(dto);
		}
		return results;
	}
}
