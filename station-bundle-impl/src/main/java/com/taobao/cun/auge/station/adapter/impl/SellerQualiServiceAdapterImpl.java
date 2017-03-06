package com.taobao.cun.auge.station.adapter.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import com.alibaba.pm.sc.api.Result;
import com.alibaba.pm.sc.api.quali.SellerQualiService;
import com.alibaba.pm.sc.api.quali.constants.QualiStatus;
import com.alibaba.pm.sc.api.quali.dto.EntityQuali;
import com.alibaba.pm.sc.api.quali.dto.ListHidByEidAndEidTypeResponse;
import com.alibaba.pm.sc.api.quali.dto.UserQualiRecord;
import com.alibaba.pm.sc.portal.api.constants.ResultCode;
import com.alibaba.pm.sc.portal.api.quali.spi.FormValidator;
import com.alibaba.pm.sc.portal.api.quali.spi.dto.FormValidateRequest;
import com.taobao.cun.auge.station.adapter.SellerQualiServiceAdapter;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.vipserver.client.utils.CollectionUtils;

@Component("sellerQualiServiceAdapter")
@RefreshScope
public class SellerQualiServiceAdapterImpl implements SellerQualiServiceAdapter{

	private static final Logger logger = LoggerFactory.getLogger(SellerQualiServiceAdapterImpl.class);

	@Autowired
	private SellerQualiService sellerQualiService;
	
	@Value("${qualiInfoId}")
	private Long qualiInfoId;
	
	@Autowired
	private FormValidator cuntaoQualificationFormValidator;

	/**
	 * 是否有有效淘宝资质
	 * @param taobaoUserId
	 * @return
	 */
	@Override
	public boolean hasValidQuali(Long taobaoUserId){
		Optional<EntityQuali>  quali = this.queryQuali(taobaoUserId);
		return quali.isPresent() && checkQualiBizScope(quali.get(),taobaoUserId);
	}
	
	/**
	 * 验证资质经营范围
	 * @param quali
	 * @param taobaoUserId
	 * @return
	 */
	public boolean checkQualiBizScope(EntityQuali quali,Long taobaoUserId){
		if(quali == null) return false;
		FormValidateRequest request = new FormValidateRequest();
		request.setQualiInfoId(quali.getQuali().getQualiInfoId());
		request.setHid(taobaoUserId);
		request.setContent(quali.getQuali().getContent());
		return cuntaoQualificationFormValidator.validate(request).getCode() == ResultCode.SUCCESS.getCode();
	}
	
	
	@Override
	public Optional<EntityQuali> queryQuali(Long taobaoUserId){
		Result<List<EntityQuali>> result = sellerQualiService.listEntityQualiByHid(taobaoUserId,qualiInfoId);
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

	public Long getQualiInfoId() {
		return qualiInfoId;
	}

	public void setQualiInfoId(Long qualiInfoId) {
		this.qualiInfoId = qualiInfoId;
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
