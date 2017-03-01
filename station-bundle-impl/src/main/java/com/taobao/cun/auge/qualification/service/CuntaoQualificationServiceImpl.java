package com.taobao.cun.auge.qualification.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.pm.sc.api.quali.constants.UserQualiRecordStatus;
import com.alibaba.pm.sc.api.quali.dto.EntityQuali;
import com.alibaba.pm.sc.api.quali.dto.UserQualiRecord;
import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.dal.domain.CuntaoQualification;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;
import com.taobao.cun.auge.station.adapter.SellerQualiServiceAdapter;
import com.taobao.cun.auge.station.bo.CuntaoQualificationBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.condition.CuntaoQualificationPageCondition;
import com.taobao.cun.auge.testuser.TestUserProperties;
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
	
	@Autowired
	private TestUserProperties testUserProperties;
	
	private BeanCopier cuntaoQualificationCopier = BeanCopier.create(Qualification.class, CuntaoQualification.class, false);
	
	private BeanCopier cuntaoQualificationReverseCopier = BeanCopier.create(CuntaoQualification.class, Qualification.class, false);
	
	@Autowired
	private CuntaoOrgServiceClient cuntaoOrgServiceClient;
	
	
	@Override
	public Qualification syncCuntaoQulification(Long taobaoUserId) {
		try {
			Qualification qualification = queryHavanaC2BQualification(taobaoUserId);
			if(qualification == null) {
				return null;
			}
			CuntaoQualification cuntaoQualification = cuntaoQualificationBO.getCuntaoQualificationByTaobaoUserId(taobaoUserId);
			if(cuntaoQualification != null){
				cuntaoQualificationCopier.copy(qualification, cuntaoQualification, null);
				DomainUtils.beforeUpdate(cuntaoQualification, "system");
				cuntaoQualificationBO.updateQualification(cuntaoQualification);
				return qualification;
			}else{
			    cuntaoQualification = new CuntaoQualification();
				cuntaoQualificationCopier.copy(qualification, cuntaoQualification, null);
				DomainUtils.beforeInsert(cuntaoQualification, "system");
				cuntaoQualificationBO.saveQualification(cuntaoQualification);
				return qualification;
			}
		} catch (Exception e) {
			logger.error("syncCuntaoQulification["+taobaoUserId+"] error!",e);
		}
		return null;
	}

	public Qualification queryHavanaC2BQualification(Long taobaoUserId){
		try {
			PartnerStationRel parnterInstance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
			Optional<EntityQuali> entityQuail = sellerQualiServiceAdapter.queryValidQuali(taobaoUserId);
			if(entityQuail.isPresent() && sellerQualiServiceAdapter.checkQualiBizScope(entityQuail.get(), taobaoUserId)){
				Optional<List<UserQualiRecord>> auditRecords = sellerQualiServiceAdapter.getUserQuailRecords(taobaoUserId);
				if(auditRecords.isPresent()){
					Optional<UserQualiRecord> userQualiRecord = auditRecords.get().stream().filter(record -> entityQuail.get().getQuali().getId().equals(record.getQid()) && record.getStatus() == UserQualiRecordStatus.AUDIT_PASS).findFirst();
					Qualification qulification = qualificationBuilder.build(parnterInstance,entityQuail,userQualiRecord);
					return qulification;
				}else{
					Qualification qulification = qualificationBuilder.build(parnterInstance,entityQuail,Optional.empty());
					return qulification;
				}
			}
		} catch (Exception e) {
			logger.error("queryHavanaC2BQualification["+taobaoUserId+"] error!",e);
		}
		
		return null;
	}
	
	
	@Override
	public Qualification queryC2BQualification(Long taobaoUserId,boolean isSyncHavana) {
		try {
			if(isSyncHavana){
				syncCuntaoQulification(taobaoUserId);
			}
			PartnerStationRel parnterInstance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
			CuntaoQualification cuntaoQualification = cuntaoQualificationBO.getCuntaoQualificationByTaobaoUserId(taobaoUserId);
			if(cuntaoQualification != null){
				Qualification qualification = new Qualification();
				cuntaoQualificationReverseCopier.copy(cuntaoQualification, qualification, null);
				qualification.setPartnerInstanceState(parnterInstance.getState());
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

	C2BTestUser buildTestUser(Long taobaoUserId){
		C2BTestUser c2bTestUser= new C2BTestUser();
		c2bTestUser.setTaobaoUserId(taobaoUserId);
		return c2bTestUser;
	}
	
	@Override
	public PageDto<C2BTestUser> querC2BTestUsers(CuntaoQualificationPageCondition condition){
		List<Long> orgIds =Stream.of(StringUtils.commaDelimitedListToStringArray(testUserProperties.getTestUserProperty("c2b","testOrgIds"))).map(Long::parseLong).collect(Collectors.toList());
		List<String> testUserTypes =Stream.of(StringUtils.commaDelimitedListToStringArray(testUserProperties.getTestUserProperty("c2b","testUserType"))).collect(Collectors.toList());
		List<String> orgIdPaths = orgIds.stream().map(orgId -> cuntaoOrgServiceClient.getCuntaoOrg(orgId).getFullIdPath()).collect(Collectors.toList());
		condition.setOrgIdPaths(orgIdPaths);
		condition.setUserTypes(testUserTypes);
		condition.setInvalidPartnerInstanceStatus(Lists.newArrayList("SETTLE_FAIL","QUIT","QUITING","TO_AUDIT"));
		Page<Long> testTaobaoUserIds  = cuntaoQualificationBO.selectC2BTestUsers(condition);
		List<C2BTestUser> testUsers = testTaobaoUserIds.getResult().stream().map(taobaoUserId -> buildTestUser(taobaoUserId)).collect(Collectors.toList());
		return PageDtoUtil.success(testTaobaoUserIds, testUsers);
	}



}
