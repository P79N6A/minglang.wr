package com.taobao.cun.auge.station.bo.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.support.Assert;

import com.alibaba.fastjson.JSONObject;
import com.taobao.cun.auge.dal.domain.PartnerCourseRecord;
import com.taobao.cun.auge.dal.domain.PartnerCourseRecordExample;
import com.taobao.cun.auge.dal.domain.PartnerCourseRecordExample.Criteria;
import com.taobao.cun.auge.dal.mapper.PartnerCourseRecordMapper;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.PartnerPeixunBO;
import com.taobao.cun.auge.station.enums.NotifyContents;
import com.taobao.cun.auge.station.enums.PartnerPeixunCourseTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerPeixunStatusEnum;
import com.taobao.notify.message.StringMessage;
@Component("partnerPeixunBO")
public class PartnerPeixunBOImpl implements PartnerPeixunBO{
	
	private static final Logger logger = LoggerFactory.getLogger(PartnerPeixunBO.class);

	@Autowired
	PartnerCourseRecordMapper partnerCourseRecordMapper;
	@Autowired
	PartnerLifecycleBO  partnerLifecycleBO;

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void handlePeixunProcess(StringMessage strMessage, JSONObject ob) {
		//判断是否是村淘入驻培训订单
		String code=ob.getString("serviceCode");
		if(!"111".equals(code)){
			return;
		}
		String messageType=strMessage.getMessageType();
		if(NotifyContents.PARTNER_PEIXUN_PAYMENT_SUCCESS.equals(messageType)){
			//处理付款成功消息
			handlePaymentSucess(ob);
		}else if(NotifyContents.PARTNER_PEIXUN_COMPLETE.equals(messageType)){
			//处理签到消息
			handleComplete(ob);
		}else{
			logger.warn("messageType not need handle,"+strMessage.toExtString());
		}
	}
	
	private void handlePaymentSucess(JSONObject ob){
		Long userId=ob.getLong("buyerAliId");
		String orderNum=ob.getString("orderNo");
		String code=ob.getString("serviceCode");
		Assert.notNull(userId);
		Assert.notNull(orderNum);
		Assert.notNull(code);
		PartnerCourseRecord record=null;
		PartnerCourseRecordExample example = new PartnerCourseRecordExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andPartnerUserIdEqualTo(userId);
		criteria.andCourseTypeEqualTo(PartnerPeixunCourseTypeEnum.APPLY_IN.getCode());
		List<PartnerCourseRecord> records=partnerCourseRecordMapper.selectByExample(example);
		if(records.size()==0){
			//初始化培训记录
			record=initPartnerApplyInRecord(userId);
		}else{
			record=records.get(0);
		}
		if(record.getStatus().equals(PartnerPeixunStatusEnum.NEW.getCode())){
			record.setCourseCode(code);
			record.setStatus(PartnerPeixunStatusEnum.PAY.getCode());
			record.setOrderNum(orderNum);
			record.setGmtModified(new Date());
			partnerCourseRecordMapper.updateByPrimaryKey(record);
		}else{
			logger.warn("peixunRecord status :"+record.getStatus());
		}
		
	}

	private void handleComplete(JSONObject ob){
		Long userId=ob.getLong("buyerAliId");
		String orderNum=ob.getString("orderNo");
		Assert.notNull(userId);
		Assert.notNull(orderNum);
		PartnerCourseRecordExample example = new PartnerCourseRecordExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andPartnerUserIdEqualTo(userId);
		criteria.andCourseTypeEqualTo(PartnerPeixunCourseTypeEnum.APPLY_IN.getCode());
		List<PartnerCourseRecord> records=partnerCourseRecordMapper.selectByExample(example);
		if(records.size()==0){
			throw new RuntimeException("not find peixunRecord "+userId.toString());
		}
		PartnerCourseRecord record=records.get(0);
        record.setStatus(PartnerPeixunStatusEnum.DONE.getCode());
        record.setGmtModified(new Date());
        record.setGmtDone(new Date());
        record.setOrderNum(orderNum);
        partnerCourseRecordMapper.updateByPrimaryKey(record);
        //更新lifecycle
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public PartnerCourseRecord initPartnerApplyInRecord(Long userId) {
		Assert.notNull(userId);
		PartnerCourseRecordExample example = new PartnerCourseRecordExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andPartnerUserIdEqualTo(userId);
		criteria.andCourseTypeEqualTo(PartnerPeixunCourseTypeEnum.APPLY_IN.getCode());
		List<PartnerCourseRecord> records=partnerCourseRecordMapper.selectByExample(example);
		if(records.size()>0){
			logger.warn("prixun record exists,"+userId.toString());
			return records.get(0);
		}
		PartnerCourseRecord record=new PartnerCourseRecord();
		record.setCourseType(PartnerPeixunCourseTypeEnum.APPLY_IN.getCode());
		record.setGmtCreate(new Date());
		record.setGmtModified(new Date());
		record.setCreator("SYSTEM");
		record.setModifier("SYSTEM");
		record.setIsDeleted("n");
		record.setPartnerUserId(userId);
		record.setStatus(PartnerPeixunStatusEnum.NEW.getCode());
		partnerCourseRecordMapper.insert(record);
		return record;
	}
	
}
