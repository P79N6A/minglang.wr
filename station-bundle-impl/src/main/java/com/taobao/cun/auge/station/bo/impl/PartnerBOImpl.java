package com.taobao.cun.auge.station.bo.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerExample;
import com.taobao.cun.auge.dal.domain.PartnerExample.Criteria;
import com.taobao.cun.auge.dal.domain.PartnerFlowerNameApply;
import com.taobao.cun.auge.dal.domain.PartnerFlowerNameApplyExample;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.mapper.ExPartnerMapper;
import com.taobao.cun.auge.dal.mapper.PartnerFlowerNameApplyMapper;
import com.taobao.cun.auge.dal.mapper.PartnerMapper;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.convert.PartnerConverter;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerFlowerNameApplyDto;
import com.taobao.cun.auge.station.enums.PartnerFlowerNameApplyStatusEnum;
import com.taobao.cun.auge.station.enums.PartnerFlowerNameSourceEnum;
import com.taobao.cun.auge.station.enums.PartnerStateEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.crius.bpm.dto.CuntaoProcessInstance;
import com.taobao.cun.crius.bpm.enums.UserTypeEnum;
import com.taobao.cun.crius.bpm.service.CuntaoWorkFlowService;
import com.taobao.cun.crius.common.resultmodel.ResultModel;
import com.taobao.diamond.client.Diamond;

@Component("partnerBO")
public class PartnerBOImpl implements PartnerBO {
	
	@Autowired
	ExPartnerMapper exPartnerMapper;
	@Autowired
	PartnerFlowerNameApplyMapper partnerFlowerNameApplyMapper;
	@Autowired
    private CuntaoWorkFlowService  cuntaoWorkFlowService;
	@Autowired
	PartnerInstanceBO partnerInstanceBO;
	@Autowired
	StationBO stationBO;
	public static String FLOW_BUSINESS_CODE="partner_flower_name_apply";
	public static String DIAMOND_BLACK="com.taobao.cun:auge.flowerName.blackList";
	public static String DIAMOND_GROUP="DEFAULT_GROUP";

	@Override
	public Partner getNormalPartnerByTaobaoUserId(Long taobaoUserId)
			throws AugeServiceException {
		ValidateUtils.notNull(taobaoUserId);
		PartnerExample example = new PartnerExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andTaobaoUserIdEqualTo(taobaoUserId);
		criteria.andStateEqualTo(PartnerStateEnum.NORMAL.getCode());
		return ResultUtils.selectOne(exPartnerMapper.selectByExample(example)); 
	}

	@Override
	public Long getNormalPartnerIdByTaobaoUserId(Long taobaoUserId)
			throws AugeServiceException {
		ValidateUtils.notNull(taobaoUserId);
		Partner partner = getNormalPartnerByTaobaoUserId(taobaoUserId);
		if (partner != null) {
			return partner.getId();
		}
		return null;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public Long addPartner(PartnerDto partnerDto)
			throws AugeServiceException {
		ValidateUtils.notNull(partnerDto);
		Partner record = PartnerConverter.toParnter(partnerDto,false);
		DomainUtils.beforeInsert(record, partnerDto.getOperator());
		exPartnerMapper.insert(record);
		return record.getId();
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void updatePartner(PartnerDto partnerDto)
			throws AugeServiceException {
		ValidateUtils.notNull(partnerDto);
		ValidateUtils.notNull(partnerDto.getId());
		Partner record = PartnerConverter.toParnter(partnerDto,true);
		DomainUtils.beforeUpdate(record, partnerDto.getOperator());
		exPartnerMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public Partner getPartnerById(Long partnerId) throws AugeServiceException {
		ValidateUtils.notNull(partnerId);
		return exPartnerMapper.selectByPrimaryKey(partnerId);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void deletePartner(Long partnerId, String operator)
			throws AugeServiceException {
		ValidateUtils.notNull(partnerId);
		Partner rel = new Partner();
		rel.setId(partnerId);
		DomainUtils.beforeDelete(rel, operator);
		exPartnerMapper.updateByPrimaryKeySelective(rel);
	}

	@Override
	public Partner getPartnerByAliLangUserId(String aliLangUserId) throws AugeServiceException {
		ValidateUtils.notNull(aliLangUserId);
		return exPartnerMapper.selectByAlilangUserId(aliLangUserId); 
	}

	@Override
	public List<Partner> getPartnerByMobile(String mobile) throws AugeServiceException {
		ValidateUtils.notNull(mobile);
		PartnerExample example = new PartnerExample();
		example.createCriteria()
				.andMobileEqualTo(mobile)
				.andStateEqualTo(PartnerStateEnum.NORMAL.getCode())
				.andIsDeletedEqualTo("n");
		return exPartnerMapper.selectByExample(example); 
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void applyFlowName(PartnerFlowerNameApplyDto dto) {
		Assert.notNull(dto);
		Assert.notNull(dto.getTaobaoUserId());
		Assert.notNull(dto.getFlowerName());
		Assert.notNull(dto.getNameMeaning());
		Assert.notNull(dto.getNameSource());
		Long id=null;
		//验证花名是否黑名单
		try {
			String flowerNameBlackString = Diamond.getConfig(DIAMOND_BLACK,
					DIAMOND_GROUP, 3000);
			if (flowerNameBlackString.contains(dto.getFlowerName())) {
				throw new AugeServiceException("此花名不允许使用，请更换再试。");
			}
		} catch (Exception e) {
			throw new AugeServiceException(e.getMessage());
		}
		if(dto.getId()!=null){
			id=dto.getId();
			//判断是否是审核没通过
			PartnerFlowerNameApply pf=partnerFlowerNameApplyMapper.selectByPrimaryKey(dto.getId());
			if(pf==null){
				throw new AugeServiceException("修改失败，查找不到申请记录");
			}
			if(!PartnerFlowerNameApplyStatusEnum.AUDIT_NOT_PASS.getCode().equals(pf.getStatus())){
				throw new AugeServiceException("当前状态不允许修改");
			}
			validateFlowerNameExist(dto);
			pf.setNameMeaning(dto.getNameMeaning());
			pf.setNameSource(dto.getNameSource());
			pf.setFlowerName(dto.getFlowerName());
			pf.setFlowerNamePinyin(dto.getFlowerNamePinyin());
			pf.setStatus(PartnerFlowerNameApplyStatusEnum.WAIT_AUDIT.getCode());
			pf.setGmtModified(new Date());
			pf.setModifier(String.valueOf(dto.getTaobaoUserId()));
			partnerFlowerNameApplyMapper.updateByPrimaryKey(pf);
		}else{
			validateApply(dto);
			PartnerFlowerNameApply apply=new PartnerFlowerNameApply();
			BeanUtils.copyProperties(dto, apply);
			apply.setStatus(PartnerFlowerNameApplyStatusEnum.WAIT_AUDIT.getCode());
			apply.setGmtModified(new Date());
			apply.setModifier(String.valueOf(dto.getTaobaoUserId()));
			apply.setCreator(String.valueOf(dto.getTaobaoUserId()));
			apply.setGmtCreate(new Date());
			apply.setIsDeleted("n");
			partnerFlowerNameApplyMapper.insert(apply);
			id=apply.getId();
		}
		//插入流程
		createFlow(id,dto.getTaobaoUserId());
	}
	
	private void validateApply(PartnerFlowerNameApplyDto dto){
		//判断花名是否已经存在
		validateFlowerNameExist(dto);
		//判断是否已经申请过
		PartnerFlowerNameApplyExample example1 = new PartnerFlowerNameApplyExample();
		com.taobao.cun.auge.dal.domain.PartnerFlowerNameApplyExample.Criteria criteria1 = example1.createCriteria();
		criteria1.andIsDeletedEqualTo("n").andTaobaoUserIdEqualTo(dto.getTaobaoUserId()).andStatusNotEqualTo(PartnerFlowerNameApplyStatusEnum.AUDIT_NOT_PASS.getCode());
		List<PartnerFlowerNameApply> applys1=partnerFlowerNameApplyMapper.selectByExample(example1);
		if(applys1.size()>0){
			throw new AugeServiceException("花名已经申请过，请不要重复申请");
		}
	}
	private void createFlow(Long applyId, Long loginId) {
		//获取组织
		PartnerStationRel rel=partnerInstanceBO.getActivePartnerInstance(loginId);
		if(rel==null){
			throw new AugeServiceException("当前状态无法申请花名");
		}
		Station s=stationBO.getStationById(rel.getStationId());
		if(s==null){
			throw new AugeServiceException("村点状态无效");
		}
		Map<String, String> initData = new HashMap<String, String>();
		initData.put("orgId", String.valueOf(s.getApplyOrg()));
		try {
			ResultModel<CuntaoProcessInstance> rm = cuntaoWorkFlowService
					.startProcessInstance(FLOW_BUSINESS_CODE,
							String.valueOf(applyId), String.valueOf(loginId),UserTypeEnum.HAVANA, initData);
			if (!rm.isSuccess()) {
				throw new AugeServiceException(rm.getException());
			}
		} catch (Exception e) {
			throw new AugeServiceException("申请花名失败", e);
		}
	}

	private void validateFlowerNameExist(PartnerFlowerNameApplyDto dto){
		PartnerFlowerNameApplyExample example = new PartnerFlowerNameApplyExample();
		com.taobao.cun.auge.dal.domain.PartnerFlowerNameApplyExample.Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n").andFlowerNameEqualTo(dto.getFlowerName()).andTaobaoUserIdNotEqualTo(dto.getTaobaoUserId());
		List<PartnerFlowerNameApply> applys=partnerFlowerNameApplyMapper.selectByExample(example);
		if(applys.size()>0){
			throw new AugeServiceException("花名已经存在，请选择别的花名申请");
		}
	}

	@Override
	public PartnerFlowerNameApplyDto getFlowerNameApplyDetail(Long taobaoUserId) {
		Assert.notNull(taobaoUserId);
		PartnerFlowerNameApplyExample example = new PartnerFlowerNameApplyExample();
		com.taobao.cun.auge.dal.domain.PartnerFlowerNameApplyExample.Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n").andTaobaoUserIdEqualTo(taobaoUserId);
		List<PartnerFlowerNameApply> applys=partnerFlowerNameApplyMapper.selectByExample(example);
		if(applys.size()>0){
			PartnerFlowerNameApplyDto result=new PartnerFlowerNameApplyDto();
			BeanUtils.copyProperties(applys.get(0), result);
			addExternalForFlowerDto(result);
			return result;
		}
		return null;
	}

	@Override
	public void auditFlowerNameApply(Long id, boolean auditResult) {
		Assert.notNull(id);
		Assert.notNull(auditResult);
		PartnerFlowerNameApply apply=partnerFlowerNameApplyMapper.selectByPrimaryKey(id);
		if(!PartnerFlowerNameApplyStatusEnum.WAIT_AUDIT.getCode().equals(apply.getStatus())){
			//状态不对，不处理
			return;
		}
		if(auditResult){
			apply.setStatus(PartnerFlowerNameApplyStatusEnum.AUDIT_PASS.getCode());
			//更新至partner
			Partner partner=getNormalPartnerByTaobaoUserId(apply.getTaobaoUserId());
			partner.setFlowerName(apply.getFlowerName());
			exPartnerMapper.updateByPrimaryKeySelective(partner);
			
		}else{
			apply.setStatus(PartnerFlowerNameApplyStatusEnum.AUDIT_NOT_PASS.getCode());
		}
		apply.setGmtModified(new Date());
		partnerFlowerNameApplyMapper.updateByPrimaryKey(apply);
	}

	@Override
	public PartnerFlowerNameApplyDto getFlowerNameApplyDetailById(Long id) {
		Assert.notNull(id);
		PartnerFlowerNameApplyExample example = new PartnerFlowerNameApplyExample();
		com.taobao.cun.auge.dal.domain.PartnerFlowerNameApplyExample.Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n").andIdEqualTo(id);
		List<PartnerFlowerNameApply> applys=partnerFlowerNameApplyMapper.selectByExample(example);
		if(applys.size()>0){
			PartnerFlowerNameApplyDto result=new PartnerFlowerNameApplyDto();
			BeanUtils.copyProperties(applys.get(0), result);
			addExternalForFlowerDto(result);
			return result;
		}
		return null;
	}
	
	private void addExternalForFlowerDto(PartnerFlowerNameApplyDto dto){
		if(StringUtils.isNotEmpty(dto.getNameSource())&&PartnerFlowerNameSourceEnum.valueof(dto.getNameSource())!=null){
			dto.setNameSourceDesc(PartnerFlowerNameSourceEnum.valueof(dto.getNameSource()).getDesc());
		}
		dto.setStatusDesc(PartnerFlowerNameApplyStatusEnum.valueof(dto.getStatus()).getDesc());
		//村点名称
		PartnerStationRel rel=partnerInstanceBO.getActivePartnerInstance(dto.getTaobaoUserId());
		if(rel!=null){
			Station station=stationBO.getStationById(rel.getStationId());
			dto.setStationName(station.getName());
		}
		Partner partner=getNormalPartnerByTaobaoUserId(dto.getTaobaoUserId());
		dto.setName(partner.getName());
	}
}
