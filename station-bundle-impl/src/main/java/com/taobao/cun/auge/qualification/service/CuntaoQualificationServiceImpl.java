package com.taobao.cun.auge.qualification.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;

import com.alibaba.pm.sc.api.quali.constants.UserQualiRecordStatus;
import com.alibaba.pm.sc.api.quali.dto.EntityQuali;
import com.alibaba.pm.sc.api.quali.dto.UserQualiRecord;
import com.github.pagehelper.Page;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.dal.domain.CuntaoQualification;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.station.adapter.SellerQualiServiceAdapter;
import com.taobao.cun.auge.station.bo.CuntaoQualificationBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.condition.CuntaoQualificationPageCondition;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("cuntaoQualificationService")
@HSFProvider(serviceInterface= CuntaoQualificationService.class)
public class CuntaoQualificationServiceImpl implements CuntaoQualificationService {

	private static final Logger logger = LoggerFactory.getLogger(CuntaoQualificationServiceImpl.class);

	@Autowired
	private CuntaoQualificationBO cuntaoQualificationBO;
	
	@Autowired
	private SellerQualiServiceAdapter sellerQualiServiceAdapter;
	
	@Autowired
	private QualificationBuilder qualificationBuilder;
	
	@Autowired
	private PartnerInstanceBO partnerInstanceBO;
	
	private BeanCopier cuntaoQualificationCopier = BeanCopier.create(Qualification.class, CuntaoQualification.class, false);
	
	private BeanCopier cuntaoQualificationReverseCopier = BeanCopier.create(CuntaoQualification.class, Qualification.class, false);
	
	
	
	@Override
	public Qualification syncCuntaoQulificationFromMetaq(Long taobaoUserId, Long qualiId, int eidType) {
		try {
			PartnerStationRel partnerInstance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
			//如果为空说明合伙人已经退出了，
			if(partnerInstance == null){
				CuntaoQualification cuntaoQualification = cuntaoQualificationBO.getCuntaoQualificationByTaobaoUserId(taobaoUserId);
				if(cuntaoQualification !=null){
					cuntaoQualificationBO.deletedQualificationById(cuntaoQualification.getId());
				}
				return null;
			}
			CuntaoQualification cuntaoQualification = cuntaoQualificationBO.getCuntaoQualificationByTaobaoUserId(taobaoUserId);
			if(cuntaoQualification != null){
				Qualification qualification = queryHavanaC2BQualificationByQualiId(taobaoUserId, qualiId, eidType);
				if(qualification == null) {
					return null;
				}
				cuntaoQualificationCopier.copy(qualification, cuntaoQualification, null);
				DomainUtils.beforeUpdate(cuntaoQualification, "system");
				cuntaoQualificationBO.updateQualification(cuntaoQualification);
				return qualification;
			}else{
				Qualification qualification = queryHavanaC2BQualificationByQualiId(taobaoUserId, qualiId, eidType);
				if(qualification == null) {
					return null;
				}
				 cuntaoQualification = new CuntaoQualification();
				 cuntaoQualificationCopier.copy(qualification, cuntaoQualification, null);
				 DomainUtils.beforeInsert(cuntaoQualification, "system");
				 cuntaoQualificationBO.saveQualification(cuntaoQualification);
				 return qualification;
			}
		} catch (Exception e) {
			logger.error("syncCuntaoQulificationFromMetaq error taobaoUserId["+taobaoUserId+"] qualiId["+qualiId+"] eidType["+eidType+"] !",e);
		}
		return null;
	}
	

	public Qualification queryHavanaC2BQualificationByQualiId(Long taobaoUserId,Long qualiId,int eidType){
		try {
			Optional<EntityQuali> entityQuail = sellerQualiServiceAdapter.queryValidQualiById(qualiId, eidType);
			if(entityQuail.isPresent()){
				Optional<List<UserQualiRecord>> auditRecords = sellerQualiServiceAdapter.getUserQuailRecords(taobaoUserId);
				if(auditRecords.isPresent()){
					Optional<UserQualiRecord> userQualiRecord = auditRecords.get().stream().filter(record -> entityQuail.get().getQuali().getId().equals(record.getQid()) && record.getStatus() == UserQualiRecordStatus.AUDIT_PASS).findFirst();
					Qualification qulification = qualificationBuilder.build(taobaoUserId,entityQuail,userQualiRecord);
					return qulification;
				}else{
					Qualification qulification = qualificationBuilder.build(taobaoUserId,entityQuail,Optional.empty());
					return qulification;
				}
			}
		} catch (Exception e) {
			logger.error("queryHavanaC2BQualification["+taobaoUserId+"] error!",e);
		}
		
		return null;
	}
	
	
	public Qualification queryHavanaC2BQualification(Long taobaoUserId){
		try {
			Optional<EntityQuali> entityQuail = sellerQualiServiceAdapter.queryValidQuali(taobaoUserId);
			if(entityQuail.isPresent()){
				Optional<List<UserQualiRecord>> auditRecords = sellerQualiServiceAdapter.getUserQuailRecords(taobaoUserId);
				if(auditRecords.isPresent()){
					Optional<UserQualiRecord> userQualiRecord = auditRecords.get().stream().filter(record -> entityQuail.get().getQuali().getId().equals(record.getQid()) && record.getStatus() == UserQualiRecordStatus.AUDIT_PASS).findFirst();
					Qualification qulification = qualificationBuilder.build(taobaoUserId,entityQuail,userQualiRecord);
					return qulification;
				}else{
					Qualification qulification = qualificationBuilder.build(taobaoUserId,entityQuail,Optional.empty());
					return qulification;
				}
			}
		} catch (Exception e) {
			logger.error("queryHavanaC2BQualification["+taobaoUserId+"] error!",e);
		}
		
		return null;
	}
	
	
	@Override
	public Qualification queryC2BQualification(Long taobaoUserId) {
		try {
			CuntaoQualification cuntaoQualification = cuntaoQualificationBO.getCuntaoQualificationByTaobaoUserId(taobaoUserId);
			if(cuntaoQualification != null){
				Qualification qualification = new Qualification();
				cuntaoQualificationReverseCopier.copy(cuntaoQualification, qualification, null);
				PartnerStationRel parnterInstance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
				if(parnterInstance!=null){
					qualification.setPartnerInstanceState(parnterInstance.getState());
				}else{
					qualification.setPartnerInstanceState(PartnerInstanceStateEnum.QUIT.getCode());
				}
				return qualification;
			}
		} catch (Exception e) {
			logger.error("queryC2BQualification["+taobaoUserId+"] error!",e);
		}
		return null;
	}


	@Override
	public PageDto<Qualification> queryQualificationsByCondition(CuntaoQualificationPageCondition condition) {
		Page<CuntaoQualification> cuntaoQualifications = cuntaoQualificationBO.queryQualificationsByCondition(condition);
		List<Qualification>  qualis = cuntaoQualifications.getResult().stream().map(q -> buildQulification(q)).collect(Collectors.toList());
		PageDto<Qualification> records = PageDtoUtil.success(cuntaoQualifications, qualis);
        return records;
	}

	Qualification buildQulification(CuntaoQualification qualification){
		Qualification quali = new Qualification();
		cuntaoQualificationReverseCopier.copy(qualification,quali, null);
		return quali;
	}

	

	@Override
	public void submitLocalQualification(Qualification qualification) {
			CuntaoQualification cuntaoQulification = this.cuntaoQualificationBO.getCuntaoQualificationByTaobaoUserId(qualification.getTaobaoUserId());
			if(cuntaoQulification != null){
				return;
			}
			cuntaoQulification  = new CuntaoQualification();
			cuntaoQualificationCopier.copy(qualification, cuntaoQulification, null);
			cuntaoQualificationBO.saveQualification(cuntaoQulification);
	}

	@Override
	public void submitHavanaQualification(Long cuntaoQualificationId) {
		cuntaoQualificationBO.submitHavanaQualification(cuntaoQualificationId);
	}


	@Override
	public C2BSettleInfo queryC2BSettleInfo(Long taobaoUserId) {
		// TODO Auto-generated method stub
		return null;
	}


}
