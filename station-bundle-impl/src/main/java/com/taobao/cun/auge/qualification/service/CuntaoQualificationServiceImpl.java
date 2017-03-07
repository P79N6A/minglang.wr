package com.taobao.cun.auge.qualification.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
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
import com.taobao.cun.auge.station.bo.PartnerProtocolRelBO;
import com.taobao.cun.auge.station.condition.CuntaoQualificationPageCondition;
import com.taobao.cun.auge.station.dto.PartnerProtocolRelDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerProtocolRelTargetTypeEnum;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("cuntaoQualificationService")
@RefreshScope
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
	
	@Value("${c2bSettleProcotolId}")
	//B类用户新的入住协议ID
	private Long c2bSettleProcotolId;
	
	@Autowired
	private PartnerProtocolRelBO partnerProtocolRelBO;
	
	@Override
	public void syncCuntaoQulificationFromMetaq(Long taobaoUserId, Long qualiId, int eidType) {
		try {
			Qualification qualification = queryHavanaC2BQualificationByQualiId(taobaoUserId, qualiId, eidType);
			if(qualification == null) {
				return ;
			}
			CuntaoQualification cuntaoQualification = cuntaoQualificationBO.getCuntaoQualificationByTaobaoUserId(taobaoUserId);
			if(cuntaoQualification != null){
				cuntaoQualificationCopier.copy(qualification, cuntaoQualification, null);
				DomainUtils.beforeUpdate(cuntaoQualification, "system");
				cuntaoQualificationBO.updateQualification(cuntaoQualification);
			}else{
				 cuntaoQualification = new CuntaoQualification();
				 cuntaoQualificationCopier.copy(qualification, cuntaoQualification, null);
				 DomainUtils.beforeInsert(cuntaoQualification, "system");
				 cuntaoQualificationBO.saveQualification(cuntaoQualification);
			}
		} catch (Exception e) {
			logger.error("syncCuntaoQulificationFromMetaq error taobaoUserId["+taobaoUserId+"] qualiId["+qualiId+"] eidType["+eidType+"] !",e);
		}
	}
	
	public void syncCuntaoQulification(Long taobaoUserId) {
		try {
			Qualification qualification = this.queryHavanaC2BQualification(taobaoUserId);
			if(qualification == null) {
				return ;
			}
			CuntaoQualification cuntaoQualification = cuntaoQualificationBO.getCuntaoQualificationByTaobaoUserId(taobaoUserId);
			if(cuntaoQualification != null){
				cuntaoQualificationCopier.copy(qualification, cuntaoQualification, null);
				DomainUtils.beforeUpdate(cuntaoQualification, "system");
				cuntaoQualificationBO.updateQualification(cuntaoQualification);
			}else{
				 cuntaoQualification = new CuntaoQualification();
				 cuntaoQualificationCopier.copy(qualification, cuntaoQualification, null);
				 DomainUtils.beforeInsert(cuntaoQualification, "system");
				 cuntaoQualificationBO.saveQualification(cuntaoQualification);
			}
		} catch (Exception e) {
			logger.error("syncCuntaoQulificationFromMetaq error taobaoUserId["+taobaoUserId+"]!",e);
		}
	}
	
	
	
	public Qualification queryHavanaC2BQualificationByQualiId(Long taobaoUserId,Long qualiId,int eidType){
		try {
			Optional<EntityQuali> entityQuail = sellerQualiServiceAdapter.queryQualiById(qualiId, eidType);
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
			Optional<EntityQuali> entityQuail = sellerQualiServiceAdapter.queryQuali(taobaoUserId);
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
			if(cuntaoQulification == null){
				cuntaoQulification  = new CuntaoQualification();
			}
			cuntaoQualificationCopier.copy(qualification, cuntaoQulification, null);
			cuntaoQualificationBO.submitLocalQualification(cuntaoQulification);
	}

	@Override
	public void submitHavanaQualification(Long taobaoUserId) {
		cuntaoQualificationBO.submitHavanaQualification(taobaoUserId);
	}


	@Override
	public C2BSettleInfo queryC2BSettleInfo(Long taobaoUserId) {
		C2BSettleInfo c2bSettleInfo = new C2BSettleInfo();
		PartnerStationRel partnerInstance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
		if(partnerInstance!=null){
			c2bSettleInfo.setPartnerInstanceStatus(partnerInstance.getState());
		}else{
			c2bSettleInfo.setPartnerInstanceStatus(PartnerInstanceStateEnum.QUIT.getCode());
		}
		PartnerProtocolRelDto partnerProtocolDto = partnerProtocolRelBO.getPartnerProtocolRelDtoByTaobaoUserId(taobaoUserId, PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE,c2bSettleProcotolId);
		if(partnerProtocolDto != null){
			c2bSettleInfo.setSignC2BTime(partnerProtocolDto.getConfirmTime());
		}
		syncCuntaoQulification(taobaoUserId);
		CuntaoQualification cuntaoQualification = cuntaoQualificationBO.getCuntaoQualificationByTaobaoUserId(taobaoUserId);
		if(null != cuntaoQualification && (cuntaoQualification.getStatus()==QualificationStatus.IN_VALID||cuntaoQualification.getStatus()==QualificationStatus.VALID)){
			c2bSettleInfo.setQualiAuditPassTime(cuntaoQualification.getAuditTime());
			c2bSettleInfo.setInvalidTime(cuntaoQualification.getInvalidTime());
			c2bSettleInfo.setSettleIdentity(cuntaoQualification.getEnterpriceType());
		}else{
			c2bSettleInfo.setSettleIdentity(QualificationBuilder.PERSONAL_BUSINESS);
		}
		return c2bSettleInfo;
	}


}
