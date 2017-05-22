package com.taobao.cun.auge.station.bo.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.taobao.cun.auge.common.exception.AugeServiceException;
import com.taobao.cun.auge.dal.domain.PartnerApply;
import com.taobao.cun.auge.dal.domain.PartnerApplyExample;
import com.taobao.cun.auge.dal.domain.PartnerApplyExample.Criteria;
import com.taobao.cun.auge.dal.mapper.PartnerApplyMapper;
import com.taobao.cun.auge.station.bo.PartnerExamBO;
import com.taobao.cun.auge.station.enums.NotifyContents;
import com.taobao.cun.crius.common.resultmodel.ResultModel;
import com.taobao.cun.crius.exam.dto.UserDispatchDto;
import com.taobao.cun.crius.exam.service.ExamUserDispatchService;
import com.taobao.notify.message.StringMessage;
@Component("partnerExamBO")
public class PartnerExamBOImpl implements PartnerExamBO{

	@Autowired
	ExamUserDispatchService examUserDispatchService;
	@Autowired
	PartnerApplyMapper partnerApplyMapper;
	
	private static final Logger logger = LoggerFactory.getLogger(PartnerExamBOImpl.class);

	@Override
	public void handleExamFinish(StringMessage strMessage, JSONObject ob) {
		try{
		String messageType=strMessage.getMessageType();
		if(!NotifyContents.EXAM_FINISH_MST.equals(messageType)){
			//不需要处理的消息类型
            return;
		}
		Long userId=ob.getLong("userId");
		Long paperId =ob.getLong("paperId");
		String status=ob.getString("status");
		Integer point=ob.getInteger("point");
		Assert.notNull(userId);
		Assert.notNull(status);
		Assert.notNull(point);
		ResultModel<UserDispatchDto> disResult=examUserDispatchService.queryExamUserDispatch(paperId, userId);
		if(!disResult.isSuccess()){
			throw new AugeServiceException("query userExamDispatch error "+strMessage+","+disResult.getException());
		}
		UserDispatchDto dis=disResult.getResult();
		if(!dis.getExamPaper().getBizType().equals("partner_apply")){
			//非招募考试，暂不处理
			return;
		}
		handleExamResultToApply(userId,status,point);
		}catch(Exception e){
			logger.error("handleExamFinish error, "+strMessage,e);
		}
	}
	
	private void handleExamResultToApply(Long userId,String status,int point){
		//获取招募考试合伙人报名信息
				PartnerApplyExample example = new PartnerApplyExample();
				Criteria criteria = example.createCriteria();
				criteria.andTaobaoUserIdEqualTo(userId);
				criteria.andIsDeletedEqualTo("n");
				List<PartnerApply> partnerApplyList = partnerApplyMapper.selectByExample(example);
				if(partnerApplyList.size()==0){
					throw new AugeServiceException("query partnerApply no result ");
				}
				for(PartnerApply apply:partnerApplyList){
					 apply.setGmtModified(new Date());
					 apply.setModifier("applyExam");
					 apply.setApplyExamStatus(status);
					 apply.setApplyExamPoint(point);
					 partnerApplyMapper.updateByPrimaryKey(apply);
				}
	}

	@Override
	public void handleExamFinish(JSONObject ob) {
		try{
		Long userId=ob.getLong("userId");
		Long paperId =ob.getLong("paperId");
		String status=ob.getString("status");
		Integer point=ob.getInteger("point");
		Assert.notNull(userId);
		Assert.notNull(status);
		Assert.notNull(point);
		ResultModel<UserDispatchDto> disResult=examUserDispatchService.queryExamUserDispatch(paperId, userId);
		if(!disResult.isSuccess()){
			throw new AugeServiceException("query userExamDispatch error:"+disResult.getException().getMessage());
		}
		UserDispatchDto dis=disResult.getResult();
		if(!dis.getExamPaper().getBizType().equals("partner_apply")){
			//非招募考试，暂不处理
			return;
		}
		handleExamResultToApply(userId,status,point);
		}catch(Exception e){
			logger.error("handleExamFinish error",e);
		}
	
		
	}

}
