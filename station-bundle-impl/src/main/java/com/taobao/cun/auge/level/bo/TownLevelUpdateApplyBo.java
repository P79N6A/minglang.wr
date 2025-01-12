package com.taobao.cun.auge.level.bo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.taobao.cun.auge.common.utils.BeanCopy;
import com.taobao.cun.auge.dal.domain.TownLevelAlterApply;
import com.taobao.cun.auge.dal.domain.TownLevelAlterApplyExample;
import com.taobao.cun.auge.dal.mapper.TownLevelAlterApplyMapper;
import com.taobao.cun.auge.level.dto.TownLevelDto;
import com.taobao.cun.auge.level.dto.TownLevelUpdateApplyDto;
import com.taobao.cun.crius.bpm.dto.StartProcessInstanceDto;
import com.taobao.cun.crius.bpm.enums.UserTypeEnum;
import com.taobao.cun.crius.bpm.service.CuntaoWorkFlowService;
import com.taobao.cun.crius.common.resultmodel.ResultModel;

@Component
public class TownLevelUpdateApplyBo {
	private static final String TASK_CODE = "townLevelUpdateApply";
	@Resource
	private TownLevelAlterApplyMapper townLevelAlterApplyMapper;
	@Resource
	private CuntaoWorkFlowService cuntaoWorkFlowService;
	@Resource
	private TownLevelBo townLevelBo;
	
	public TownLevelUpdateApplyDto getTownLevelUpdateApplyDto(Long id) {
		return BeanCopy.copy(TownLevelUpdateApplyDto.class, townLevelAlterApplyMapper.selectByPrimaryKey(id));
	}
	
	
	public void apply(TownLevelUpdateApplyDto townLevelUpdateApplyDto) {
		//检查是否已经有在审批中的流程
		TownLevelAlterApplyExample example = new TownLevelAlterApplyExample();
		example.createCriteria().andTownLevelIdEqualTo(townLevelUpdateApplyDto.getTownLevelId()).andStateEqualTo("NEW");
		if(townLevelAlterApplyMapper.countByExample(example) > 0) {
			throw new RuntimeException("已经有变更流程未完成，请等待流程结束后再提交。");
		}
		TownLevelAlterApply townLevelAlterApply = BeanCopy.copy(TownLevelAlterApply.class, townLevelUpdateApplyDto);
		townLevelAlterApply.setFinishTime(0L);
		townLevelAlterApply.setGmtCreate(new Date());
		townLevelAlterApply.setGmtModified(new Date());
		townLevelAlterApply.setModifier(townLevelUpdateApplyDto.getCreator());
		townLevelAlterApply.setCreator(townLevelUpdateApplyDto.getCreator());
		townLevelAlterApply.setState("NEW");
		townLevelAlterApplyMapper.insert(townLevelAlterApply);
		
        StartProcessInstanceDto startDto = new StartProcessInstanceDto();
		startDto.setBusinessCode(TASK_CODE);
		startDto.setBusinessId(String.valueOf(townLevelAlterApply.getId()));
		startDto.setApplierId(townLevelUpdateApplyDto.getCreator());
		startDto.setApplierUserType(UserTypeEnum.BUC);
		startDto.setBusinessName(getTaskName(townLevelUpdateApplyDto.getTownLevelId()));
		
		Map<String, String> initData = Maps.newHashMap();
		initData.put("orgId", String.valueOf(townLevelUpdateApplyDto.getOrgId()));
		startDto.setInitData(initData);
		ResultModel<Boolean> result = cuntaoWorkFlowService.startProcessInstance(startDto);
		if(!result.isSuccess()) {
			throw new RuntimeException("创建流程失败：" + result.getException().getMessage());
		}
	}
	
	private String getTaskName(Long townLevelId) {
		TownLevelDto townLevelDto = townLevelBo.getTownLevel(townLevelId);
		List<String> namePaths = Lists.newArrayList();
		namePaths.add(townLevelDto.getProvinceName());
		if(townLevelDto.getCityName() != null && !"N".equals(townLevelDto.getCityName())) {
			namePaths.add(townLevelDto.getCityName());
		}
		if(townLevelDto.getCountyName() != null && !"N".equals(townLevelDto.getCountyName())) {
			namePaths.add(townLevelDto.getCountyName());
		}
		namePaths.add(townLevelDto.getTownName());
		return "【" + Joiner.on("-").join(namePaths) + "】市场分层数据变更";
	}
	
	@Transactional(rollbackFor=Throwable.class)
	public void agree(String applyId) {
		TownLevelAlterApply townLevelAlterApply = townLevelAlterApplyMapper.selectByPrimaryKey(Long.parseLong(applyId));
		townLevelBo.updatePopulation(townLevelAlterApply.getTownLevelId(), townLevelAlterApply.getPopulation());
		townLevelAlterApply.setGmtModified(new Date());
		townLevelAlterApply.setState("AGREE");
		townLevelAlterApply.setFinishTime(System.currentTimeMillis());
		townLevelAlterApplyMapper.updateByPrimaryKey(townLevelAlterApply);
	}

	public void disagree(String applyId) {
		TownLevelAlterApply townLevelAlterApply = townLevelAlterApplyMapper.selectByPrimaryKey(Long.parseLong(applyId));
		townLevelAlterApply.setGmtModified(new Date());
		townLevelAlterApply.setState("DISAGREE");
		townLevelAlterApply.setFinishTime(System.currentTimeMillis());
		townLevelAlterApplyMapper.updateByPrimaryKey(townLevelAlterApply);
	}
}
