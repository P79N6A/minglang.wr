package com.taobao.cun.auge.station.adapter.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ali.com.google.common.collect.Maps;
import com.alibaba.fastjson.JSON;
import com.alibaba.pm.sc.api.Result;
import com.alibaba.pm.sc.api.quali.SellerQualiService;
import com.alibaba.pm.sc.api.quali.constants.QualiStatus;
import com.alibaba.pm.sc.api.quali.dto.EntityQuali;
import com.alibaba.pm.sc.api.quali.dto.ListHidByEidAndEidTypeResponse;
import com.alibaba.pm.sc.api.quali.dto.UserQualiRecord;
import com.alibaba.pm.sc.portal.api.quali.QualiAccessService;
import com.alibaba.pm.sc.portal.api.quali.dto.QualiAddRequest;
import com.taobao.cun.auge.dal.domain.CuntaoQualification;
import com.taobao.cun.auge.qualification.service.QualificationStatus;
import com.taobao.cun.auge.station.adapter.SellerQualiServiceAdapter;
import com.taobao.cun.auge.station.adapter.UicReadAdapter;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.vipserver.client.utils.CollectionUtils;

@Component("sellerQualiServiceAdapter")
public class SellerQualiServiceAdapterImpl implements SellerQualiServiceAdapter{

	private static final Logger logger = LoggerFactory.getLogger(SellerQualiServiceAdapterImpl.class);

	@Autowired
	private SellerQualiService sellerQualiService;
	
	@Value("${qualiInfoId}")
	private Long qualiInfoId;
	
	@Autowired
	private QualiAccessService qualiAccessService;
	@Autowired
	private UicReadAdapter uicReadAdapter;
	/**
	 * 是否有有效淘宝资质
	 * @param taobaoUserId
	 * @return
	 */
	@Override
	public boolean hasValidQuali(Long taobaoUserId){
		Optional<EntityQuali>  quali = this.queryQuali(taobaoUserId);
		return quali.isPresent();
	}
	
	public void insertQualiRecord(CuntaoQualification qualification){
		QualiAddRequest request = new QualiAddRequest();
		request.setQualiInfoName("bizLicense");
		request.setSource("cuntao");
		request.setNick(uicReadAdapter.getTaobaoNickByTaobaoUserId(qualification.getTaobaoUserId()));
		request.setUserId(qualification.getTaobaoUserId());
		Map<String, String> content = Maps.newHashMap();
		content.put("companyName", qualification.getCompanyName());
		content.put("regNo", qualification.getQualiNo());
		String ossPics = qualification.getQualiOss();
		if (StringUtils.isNotEmpty(ossPics)) {
			String[] ossPic = ossPics.split(",");
			List<Map<String, String>> ossList = new ArrayList<Map<String, String>>();
			for (String ossPicName : ossPic) {
				Map<String, String> ossPicMap = new HashMap<String, String>();
				ossPicMap.put("imgURL", ossPicName);
				ossList.add(ossPicMap);
			}
			content.put("qualiImage", JSON.toJSONString(ossList));
		}
		request.setContentWithName(content);
		Result<Void> result = qualiAccessService.insertQualiRecord(request);
		if(!result.isSuccessful()||!result.isExecuteSuccessful()){
			qualification.setErrorCode(result.getCode()+"");
			qualification.setErrorMessage(result.getMessage());
			qualification.setStatus(QualificationStatus.SUBMIT_FAIL);
		}else{
			qualification.setStatus(QualificationStatus.SUBMIT_SUCESS);
			qualification.setErrorMessage("");
			qualification.setErrorCode("");
		}
	}
	
	
	@Override
	public Optional<EntityQuali> queryQuali(Long taobaoUserId){
		Result<List<EntityQuali>> result = sellerQualiService.listEntityQualiByHid(taobaoUserId,qualiInfoId,QualiStatus.VALID,QualiStatus.INVALID);
		if(!result.isSuccessful()){
			logger.error("listEntityQualiByHid error["+taobaoUserId+"]:"+result.toString());
			throw new AugeBusinessException("查询企业资质异常");
		}
		if(!CollectionUtils.isEmpty(result.getData())){
			return Optional.ofNullable(result.getData().iterator().next());
		}
		return Optional.ofNullable(null);
	}
	/**
	 * 
	 * @param taobaoUserId
	 * @return
	 */
	@Override
	public UserQualiRecord lastAuditQualiStatus(Long taobaoUserId){
		List<UserQualiRecord> auditRecords = getUserQuailRecords(taobaoUserId).get();
		if(CollectionUtils.isEmpty(auditRecords)){
			UserQualiRecord record = new UserQualiRecord();
			record.setStatus(NO_AUDIT_RECORD);
			//没有提交记录
			return record;
		}
		UserQualiRecord lastRecord = auditRecords.stream().findFirst().get();
	    return lastRecord;
	}
	
	@Override
	public Optional<List<UserQualiRecord>> getUserQuailRecords(Long taobaoUserId) {
		Result<List<UserQualiRecord>> auditRecord	= sellerQualiService.listUserQualiRecord(taobaoUserId,qualiInfoId);
		if(!auditRecord.isSuccessful()){
			logger.error("listUserQualiRecord  auditRecord error["+taobaoUserId+"]:"+auditRecord.toString());
			throw new AugeBusinessException("查询企业资质异常");
		}
		return Optional.ofNullable(auditRecord.getData());
	}


	@Override
	public Optional<EntityQuali> queryQualiById(Long qualiId,int eidType) {
		Result<EntityQuali> result = sellerQualiService.getEntityQualiByQid(qualiId,eidType);
		if(!result.isSuccessful()){
			logger.error("getEntityQualiByQid error qualiId["+qualiId+"]:"+result.toString());
			throw new AugeBusinessException("查询企业资质异常");
		}
		return Optional.ofNullable(result.getData());
	}
	
	public Optional<ListHidByEidAndEidTypeResponse> queryHavanaIdByQuali(String eid, int eidType){
		Result<ListHidByEidAndEidTypeResponse> result = sellerQualiService.listHidByEidAndEidType(eid, eidType);
		if(!result.isSuccessful()){
			logger.error("listHidByEidAndEidType error eid["+eid+"] eidType["+eidType+"]:"+result.toString());
			throw new AugeBusinessException("查询havana信息异常");
		}
		return Optional.ofNullable(result.getData());
	}
}
