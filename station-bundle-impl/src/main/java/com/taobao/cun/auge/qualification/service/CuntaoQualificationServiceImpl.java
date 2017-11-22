package com.taobao.cun.auge.qualification.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.pm.sc.api.Result;
import com.alibaba.pm.sc.api.quali.constants.UserQualiRecordStatus;
import com.alibaba.pm.sc.api.quali.dto.EntityQuali;
import com.alibaba.pm.sc.api.quali.dto.UserQualiRecord;
import com.alibaba.pm.sc.portal.api.ScPortalService;
import com.alibaba.pm.sc.portal.api.quali.QLCAccessService;
import com.alibaba.pm.sc.portal.api.quali.dto.lifecycle.QLCAPIConst;
import com.alibaba.pm.sc.portal.api.quali.dto.lifecycle.QLCAbnormalRequest;
import com.alibaba.pm.sc.portal.api.quali.dto.lifecycle.QLCAbnormalResult;
import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.DateUtil;
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
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;
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
	
	private static final BeanCopier cuntaoQualificationCopier = BeanCopier.create(Qualification.class, CuntaoQualification.class, false);
	
	private static final BeanCopier cuntaoQualificationReverseCopier = BeanCopier.create(CuntaoQualification.class, Qualification.class, false);
	
	@Autowired
	private PartnerProtocolRelBO partnerProtocolRelBO;
	
	@Autowired
	private C2BErrorMessageConverter c2BErrorMessageConverter;
	
	@Autowired
	private QLCAccessService qlcAccessService;
	@Autowired
	private ScPortalService  scPortalService;
	@Autowired
	private QualiAbnormalMessageProperties qualiAbnormalMessageProperties;
	@Override
	public void syncCuntaoQulificationFromMetaq(Long taobaoUserId, Long qualiId, int eidType) {
			logger.info("syncCuntaoQulificationFromMetaq taobaoUserId["+taobaoUserId+"] qualiId["+qualiId+"] eidType["+eidType+"]");
			CuntaoQualification cuntaoQualification = cuntaoQualificationBO.getCuntaoQualificationByTaobaoUserId(taobaoUserId);
			if(cuntaoQualification == null) {
				return;
			}
			Qualification qualification = queryHavanaC2BQualificationByQualiId(taobaoUserId, qualiId, eidType);
			if(qualification == null) {
				return ;
			}
			cuntaoQualificationCopier.copy(qualification, cuntaoQualification, null);
			DomainUtils.beforeUpdate(cuntaoQualification, "system");
			cuntaoQualificationBO.updateQualification(cuntaoQualification);
	}
	
	@Override
    public void syncCuntaoQulification(Long taobaoUserId) {
			CuntaoQualification cuntaoQualification = cuntaoQualificationBO.getCuntaoQualificationByTaobaoUserId(taobaoUserId);
			if(cuntaoQualification == null) {
				return;
			}
			Qualification qualification = this.queryHavanaC2BQualification(taobaoUserId);
			if(qualification == null) {
				return ;
			}
			//最后提交时间大于我们这里的最后修改时间的话就更新
			cuntaoQualificationCopier.copy(qualification, cuntaoQualification, null);
			DomainUtils.beforeUpdate(cuntaoQualification, "system"); 
			cuntaoQualificationBO.updateQualification(cuntaoQualification);
	}
	
	
	public void invalidQualification(Long taobaoUserId){
		CuntaoQualification cuntaoQualification = cuntaoQualificationBO.getCuntaoQualificationByTaobaoUserId(taobaoUserId);
		if(cuntaoQualification == null) {
			return;
		}
		 QLCAbnormalRequest request = new QLCAbnormalRequest();
		 request.setUserId(cuntaoQualification.getTaobaoUserId());
		 request.setBizType(QLCAbnormalRequest.BIZ_CERT);
		 Result<QLCAbnormalResult> result = qlcAccessService.getQLCAbnormal(request);
		 if(result.isSuccessful()){
			 Map<String,Integer> details = result.getData().getAbnormalDetails();
			 if(details !=null && !details.isEmpty()){
				 for(String key : details.keySet()){
					 Integer value = details.get(key);
					 if(QLCAPIConst.TRUE==value.intValue()){
						 CuntaoQualification qualification = new CuntaoQualification();
						 qualification.setId(cuntaoQualification.getId());
						 qualification.setUpdateFlag("1");
						 qualification.setUpdateReason(JSON.toJSONString(details));
						 qualification.setUpdateDesc(qualiAbnormalMessageProperties.getAbnormalMessage(key));
						 qualification.setUpdateDate(DateUtil.formatTime(new Date()));
						 cuntaoQualificationBO.updateQualification(qualification);
						 break;
					 }
				 }
			 }
		 }
	}
	
	public void recoverQualification(Long taobaoUserId){
		CuntaoQualification cuntaoQualification = cuntaoQualificationBO.getCuntaoQualificationByTaobaoUserId(taobaoUserId);
		if(cuntaoQualification == null) {
			return;
		}
		CuntaoQualification qualification = new CuntaoQualification();
		qualification.setId(cuntaoQualification.getId());
		qualification.setUpdateFlag("");
		qualification.setUpdateReason("");
		qualification.setUpdateDesc("");
		qualification.setUpdateDate("");
		cuntaoQualificationBO.updateQualification(qualification);
	}
	
	
	
	
	public Qualification queryHavanaC2BQualificationByQualiId(Long taobaoUserId,Long qualiId,int eidType){
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
		return null;
	}
	
	
	@Override
    public Qualification queryHavanaC2BQualification(Long taobaoUserId){
			Optional<EntityQuali> entityQuail = sellerQualiServiceAdapter.queryQuali(taobaoUserId);
			Optional<UserQualiRecord> auditRecords = Optional.ofNullable(sellerQualiServiceAdapter.lastAuditQualiStatus(taobaoUserId));
			Qualification qulification = qualificationBuilder.build(taobaoUserId,entityQuail,auditRecords);
			return qulification;
	}
	
	@Override
    public Qualification querEnterpriceC2BQualification(Long taobaoUserId){
			Optional<EntityQuali> entityQuail = sellerQualiServiceAdapter.queryEnterpriceQualiById(taobaoUserId);
			//Optional<UserQualiRecord> auditRecords = Optional.ofNullable(sellerQualiServiceAdapter.lastAuditQualiStatus(taobaoUserId));
			Qualification qulification = qualificationBuilder.build(taobaoUserId,entityQuail,Optional.empty());
			return qulification;
	}
	
	
	@Override
	public Qualification queryC2BQualification(Long taobaoUserId) {
			CuntaoQualification cuntaoQualification = cuntaoQualificationBO.getCuntaoQualificationByTaobaoUserId(taobaoUserId);
			if(cuntaoQualification != null){
				Qualification qualification = new Qualification();
				cuntaoQualificationReverseCopier.copy(cuntaoQualification, qualification, null);
				String errorMsg = c2BErrorMessageConverter.convertErrorMsg(cuntaoQualification.getErrorCode(), cuntaoQualification.getErrorMessage());
				qualification.setErrorMessage(errorMsg);
				if(StringUtils.isNotEmpty(qualification.getQualiOss()) && QualificationStatus.VALID==qualification.getStatus()){
					try {
						Result<String> result = scPortalService.getMaskScImage(qualification.getQualiOss());
						if(result.isSuccessful()){
							qualification.setQualiImageUrl(result.getData());
						}
					} catch (Exception e) {
						logger.error("query quali image error["+taobaoUserId+"]",e);
					}
				}
				return qualification;
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
	public boolean submitLocalQualification(Qualification qualification) {
		try {
			CuntaoQualification cuntaoQulification = this.cuntaoQualificationBO.getCuntaoQualificationByTaobaoUserId(qualification.getTaobaoUserId());
			if(cuntaoQulification == null){
				cuntaoQulification  = new CuntaoQualification();
			}
			cuntaoQualificationCopier.copy(qualification, cuntaoQulification, null);
			cuntaoQualificationBO.submitLocalQualification(cuntaoQulification);
		} catch (Exception e) {
			logger.error("submitLocalQualification error！tabaoUserId["+qualification.getTaobaoUserId()+"]",e);
			return false;
		}
			return true;
	}

	@Override
	public void submitHavanaQualification(Long taobaoUserId) {
		Qualification qualification = this.querEnterpriceC2BQualification(taobaoUserId);
		if(qualification.getStatus() == 1){
			CuntaoQualification cuntaoQualification = cuntaoQualificationBO.getCuntaoQualificationByTaobaoUserId(taobaoUserId);
			if(cuntaoQualification == null) {
				return;
			}
			//最后提交时间大于我们这里的最后修改时间的话就更新
			cuntaoQualificationCopier.copy(qualification, cuntaoQualification, null);
			DomainUtils.beforeUpdate(cuntaoQualification, "system"); 
			cuntaoQualificationBO.updateQualification(cuntaoQualification);
		}else{
			cuntaoQualificationBO.submitHavanaQualification(taobaoUserId);
		}
		
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
		PartnerProtocolRelDto settleProtocol = partnerProtocolRelBO.getLastPartnerProtocolRelDtoByTaobaoUserId(taobaoUserId,ProtocolTypeEnum.SETTLE_PRO,PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE);
		PartnerProtocolRelDto settleC2BProtocol = partnerProtocolRelBO.getLastPartnerProtocolRelDtoByTaobaoUserId(taobaoUserId,ProtocolTypeEnum.C2B_SETTLE_PRO,PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE);
		if(settleC2BProtocol!=null){
			c2bSettleInfo.setSignC2BTime(settleC2BProtocol.getConfirmTime());
		}
		c2bSettleInfo.setNewPartner(!Optional.ofNullable(settleProtocol).isPresent());
		CuntaoQualification cuntaoQualification = cuntaoQualificationBO.getCuntaoQualificationByTaobaoUserId(taobaoUserId);
		if(null != cuntaoQualification){
			c2bSettleInfo.setQualiStatus(cuntaoQualification.getStatus());
			c2bSettleInfo.setQualiAuditPassTime(cuntaoQualification.getAuditTime());
			c2bSettleInfo.setInvalidTime(cuntaoQualification.getInvalidTime());
			c2bSettleInfo.setSettleIdentity(cuntaoQualification.getEnterpriceType() == null?QualificationBuilder.PERSONAL_BUSINESS:cuntaoQualification.getEnterpriceType());
		}else{
			c2bSettleInfo.setSettleIdentity(QualificationBuilder.PERSONAL_BUSINESS);
		}
		return c2bSettleInfo;
	}


	@Override
	public List<String> querySubmitedQualifications() {
		CuntaoQualificationPageCondition condition = new CuntaoQualificationPageCondition();
		condition.setPageSize(10000);
		condition.setPageNum(1);
		condition.setStatusList(Lists.newArrayList(0));
		List<String> list = Lists.newArrayList();
		Page<CuntaoQualification> qualis = cuntaoQualificationBO.queryQualificationsByCondition(condition);
		for (Iterator iterator = qualis.iterator(); iterator.hasNext();) {
			CuntaoQualification cuntaoQualification = (CuntaoQualification) iterator.next();
			UserQualiRecord record = sellerQualiServiceAdapter.lastAuditQualiStatus(cuntaoQualification.getTaobaoUserId());
			if(UserQualiRecordStatus.TO_BE_AUDITED != record.getStatus()){
				list.add(cuntaoQualification.getTaobaoUserId()+":"+record.getStatus());
				logger.info("querySubmitedQualifications:"+cuntaoQualification.getTaobaoUserId()+":"+record.getStatus());
			}
		}
		return list;
	}


	 public void syncInvalidQuali(int pageSize){
		 CuntaoQualificationPageCondition condition = new CuntaoQualificationPageCondition();
		 condition.setPageSize(pageSize);
		 condition.setPageNum(1);
		 condition.setStatusList(Lists.newArrayList(QualificationStatus.VALID,QualificationStatus.IN_VALID));
		 Page<CuntaoQualification> qualis = cuntaoQualificationBO.queryQualificationsByCondition(condition);	
		 for(CuntaoQualification quali: qualis){
			 Qualification havanaQuali = this.queryHavanaC2BQualification(quali.getTaobaoUserId());
			 if(havanaQuali != null && !QualificationStatus.VALID.equals(havanaQuali.getStatus())){
				 if(havanaQuali.getStatus() == QualificationStatus.IN_VALID && !"1".equals(quali.getUpdateFlag())){
					 logger.info("invalid quali taobaoUserId["+quali.getTaobaoUserId()+"] havanaStatus["+havanaQuali.getStatus()+"]");
					 QLCAbnormalRequest request = new QLCAbnormalRequest();
					 request.setUserId(quali.getTaobaoUserId());
					 request.setBizType(QLCAbnormalRequest.BIZ_CERT);
					 Result<QLCAbnormalResult> result = qlcAccessService.getQLCAbnormal(request);
					 if(result.isSuccessful()){
						 this.invalidQualification(quali.getTaobaoUserId());
						// logger.info("QLCAbnormalResult:"+JSON.toJSONString(result.getData().getAbnormalDetails()));
					 }else{
						 logger.info("query qlcAccessService error:"+result.getMessage()+" "+result.getCode());
					 }
				 }else{
					 logger.info("invalidHavanaStatus["+quali.getTaobaoUserId()+"] status["+havanaQuali.getStatus()+"]");
				 }
				 
			 }
		 }
		 
	 }
}
