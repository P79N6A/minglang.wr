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
import com.alibaba.pm.sc.api.quali.dto.UserQualiRecord;
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
	


	/**
	 * 是否有有效淘宝资质
	 * @param taobaoUserId
	 * @return
	 */
	@Override
	public boolean hasValidQuali(Long taobaoUserId){
		return this.queryValidQuali(taobaoUserId).isPresent();
	}
	
	@Override
	public Optional<EntityQuali> queryValidQuali(Long taobaoUserId){
		Result<List<EntityQuali>> result = sellerQualiService.listEntityQualiByHid(taobaoUserId,qualiInfoId,QualiStatus.VALID);
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
	
	
}
