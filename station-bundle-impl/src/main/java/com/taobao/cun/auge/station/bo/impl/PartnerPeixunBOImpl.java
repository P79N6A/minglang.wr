package com.taobao.cun.auge.station.bo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.support.Assert;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.ivy.common.AppAuthDTO;
import com.alibaba.ivy.common.PageDTO;
import com.alibaba.ivy.common.ResultDTO;
import com.alibaba.ivy.service.course.CourseServiceFacade;
import com.alibaba.ivy.service.user.TrainingRecordServiceFacade;
import com.alibaba.ivy.service.user.TrainingTicketServiceFacade;
import com.alibaba.ivy.service.user.dto.TrainingRecordDTO;
import com.alibaba.ivy.service.user.dto.TrainingTicketDTO;
import com.alibaba.ivy.service.user.query.TrainingRecordQueryDTO;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.dal.domain.PartnerCourseRecord;
import com.taobao.cun.auge.dal.domain.PartnerCourseRecordExample;
import com.taobao.cun.auge.dal.domain.PartnerCourseRecordExample.Criteria;
import com.taobao.cun.auge.dal.mapper.PartnerCourseRecordMapper;
import com.taobao.cun.auge.station.bo.AppResourceBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerPeixunBO;
import com.taobao.cun.auge.station.dto.PartnerPeixunDto;
import com.taobao.cun.auge.station.enums.NotifyContents;
import com.taobao.cun.auge.station.enums.PartnerPeixunCourseTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerPeixunStatusEnum;
import com.taobao.cun.crius.common.resultmodel.ResultModel;
import com.taobao.cun.crius.exam.dto.ExamDispatchDto;
import com.taobao.cun.crius.exam.service.ExamInstanceService;
import com.taobao.cun.crius.exam.service.ExamUserDispatchService;
import com.taobao.notify.message.StringMessage;
@Component("partnerPeixunBO")
public class PartnerPeixunBOImpl implements PartnerPeixunBO{
	
	private static final Logger logger = LoggerFactory.getLogger(PartnerPeixunBO.class);

	@Autowired
	PartnerCourseRecordMapper partnerCourseRecordMapper;
	@Autowired
	PartnerInstanceBO  partnerInstanceBO;
	@Autowired
	TrainingRecordServiceFacade trainingRecordServiceFacade;
	@Autowired
	CourseServiceFacade courseServiceFacade;
	@Autowired
	TrainingTicketServiceFacade trainingTicketServiceFacade;
	@Autowired
	ExamUserDispatchService examUserDispatchService;
	@Autowired
	ExamInstanceService examInstanceService;
	@Autowired
	AppResourceBO appResourceBO;
	
	@Value("${partner.apply.in.peixun.code}")
	private String peixunCode;
	
	@Value("${partner.peixun.client.code}")
	private String peixunClientCode;
	
	@Value("${partner.peixun.client.key}")
	private String peixunClientKey;
	
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void handlePeixunProcess(StringMessage strMessage, JSONObject ob) {
		//判断是否是村淘入驻培训订单
		String code=ob.getString("serviceCode");
		if(!peixunCode.equals(code)){
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
			DomainUtils.beforeUpdate(record, DomainUtils.DEFAULT_OPERATOR);
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
        record.setGmtDone(new Date());
        record.setOrderNum(orderNum);
        DomainUtils.beforeUpdate(record, DomainUtils.DEFAULT_OPERATOR);
        partnerCourseRecordMapper.updateByPrimaryKey(record);
        //更新lifecycle
        partnerInstanceBO.finishCourse(userId);
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
		record.setPartnerUserId(userId);
		record.setStatus(PartnerPeixunStatusEnum.NEW.getCode());
		record.setCourseCode(peixunCode);
		DomainUtils.beforeInsert(record, DomainUtils.DEFAULT_OPERATOR);
		partnerCourseRecordMapper.insert(record);
		return record;
	}
	
	@Override
	public PartnerCourseRecord initPeixunRecord(Long userId,
			PartnerPeixunCourseTypeEnum courseType, String courseCode) {
		Assert.notNull(userId);
		Assert.notNull(courseType);
		Assert.notNull(courseCode);
		PartnerCourseRecordExample example = new PartnerCourseRecordExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andPartnerUserIdEqualTo(userId);
		criteria.andCourseTypeEqualTo(courseType.getCode());
		criteria.andCourseCodeEqualTo(courseCode);
		List<PartnerCourseRecord> records=partnerCourseRecordMapper.selectByExample(example);
		if(records.size()>0){
			logger.warn("prixun record exists,"+userId.toString());
			return records.get(0);
		}
		PartnerCourseRecord record=new PartnerCourseRecord();
		record.setCourseType(courseType.getCode());
		record.setPartnerUserId(userId);
		record.setStatus(PartnerPeixunStatusEnum.NEW.getCode());
		record.setCourseCode(courseCode);
		DomainUtils.beforeInsert(record, DomainUtils.DEFAULT_OPERATOR);
		partnerCourseRecordMapper.insert(record);
		return record;
	}
	
	public String getPeixunTicket(Long userId,String courseCode,String orderNum){
		AppAuthDTO auth = new AppAuthDTO();
		auth.setAuthkey(peixunClientKey);
		auth.setCode(peixunClientCode);
		List<TrainingRecordDTO> trainRecords=getRecordFromPeixun(auth,courseCode,userId);
		for(TrainingRecordDTO dto:trainRecords){
			if(orderNum.equals(getOrderNoByOrderItem(dto.getOrderItemNum()))){
				ResultDTO<List<TrainingTicketDTO>> ticketDto=trainingTicketServiceFacade.getByTrainingRecordId(auth, dto.getId());
				if(ticketDto.isSuccess()){
					if(ticketDto.getData() != null && ticketDto.getData().size()>0){
						return ticketDto.getData().get(0).getTicketNo();
					}
				}else{
					logger.error("getByTrainingRecordId error param:"+dto.getId()+"message:"+JSONObject.toJSONString(ticketDto));
					throw new RuntimeException("getTicketError "+ticketDto.getMsg());
				}
			}
		}
		return null;
	}
	
	private List<TrainingRecordDTO> getRecordFromPeixun(AppAuthDTO auth,String code, Long userId) {
		TrainingRecordQueryDTO query = new TrainingRecordQueryDTO();
		query.addCourseCode(code);
		query.addTrainee(String.valueOf(userId));
		try {
			ResultDTO<PageDTO<TrainingRecordDTO>> result = trainingRecordServiceFacade
					.find(auth, query, 100, 1);
			if (result.isSuccess()) {
				return result.getData().getRows()==null?Lists.newArrayList():result.getData().getRows();
			} else {
				throw new RuntimeException("query record error,"
						+ result.getMsg());
			}
		} catch (Exception e) {
			logger.error("queryPeixunRecordList error", e);
			throw new RuntimeException(e);
		}
	}

	protected String getOrderNoByOrderItem(String orderItem) {
		if (StringUtils.isBlank(orderItem))
			return null;
		int lastIndex = orderItem.lastIndexOf("_");
		if (lastIndex <= 0)
			return null;
		return orderItem.substring(0, lastIndex);
	}

	@Override
	public List<PartnerPeixunDto> queryBatchPeixunRecord(List<Long> userIds) {
		List<PartnerPeixunDto> result=new ArrayList<PartnerPeixunDto>();
		if(userIds==null||userIds.size()==0){
			return result;
		}
		PartnerCourseRecordExample example = new PartnerCourseRecordExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andPartnerUserIdIn(userIds);
		criteria.andCourseTypeEqualTo(PartnerPeixunCourseTypeEnum.APPLY_IN
				.getCode());
		List<PartnerCourseRecord> records = partnerCourseRecordMapper
				.selectByExample(example);
		for(PartnerCourseRecord record:records){
			PartnerPeixunDto dto=new PartnerPeixunDto();
			dto.setUserId(record.getPartnerUserId());
			dto.setStatus(record.getStatus());
			dto.setStatusDesc(PartnerPeixunStatusEnum.valueof(record.getStatus()).getDesc());
			result.add(dto);
		}
		return result;
	}

	@Override
	public void dispatchApplyInExamPaper(Long userId, String taobaoNick,String paperId) {
		Assert.notNull(userId);
		ExamDispatchDto dto = new ExamDispatchDto();
		dto.setDispatcher("SYSTEM");
		dto.setPaperId(new Long(paperId));
		dto.setUserId(userId);
		dto.setTaobaoNick(taobaoNick);
		ResultModel<Boolean> result = examUserDispatchService.dispatchExam(dto);
		if (!result.isSuccess()) {
			throw new RuntimeException("dispatch examPaper fail:"
					+ result.getException());
		}
	}
	
	@Override
	public void invalidPeixunRecord(Long userId,PartnerPeixunCourseTypeEnum courseType,String courseCode) {
		PartnerCourseRecordExample example = new PartnerCourseRecordExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andPartnerUserIdEqualTo(userId);
		criteria.andCourseTypeEqualTo(courseType.getCode());
		criteria.andCourseCodeEqualTo(courseCode);
		List<PartnerCourseRecord> records=partnerCourseRecordMapper.selectByExample(example);
		if(records.size()>0){
			PartnerCourseRecord record=records.get(0);
			if(PartnerPeixunStatusEnum.NEW.getCode().equals(record.getStatus())){
				record.setIsDeleted("y");
				record.setGmtModified(new Date());
				partnerCourseRecordMapper.updateByPrimaryKey(record);
			}
		}
		
	}
	
	public PartnerPeixunDto queryOnlineCourseRecord(Long userId, String courseCode) {
		PartnerPeixunDto result = new PartnerPeixunDto();
		AppAuthDTO auth = new AppAuthDTO();
		auth.setAuthkey(peixunClientKey);
		auth.setCode(peixunClientCode);
		List<TrainingRecordDTO> trainRecords = getRecordFromPeixun(auth,courseCode,
				userId);
		if (trainRecords.size() == 0) {
			return null;
		} else {
			result.setUserId(userId);
			result.setCourseCode(courseCode);
			result.setStatus(PartnerPeixunStatusEnum.DONE.getCode());
			result.setStatusDesc(PartnerPeixunStatusEnum.DONE.getDesc());
			result.setGmtDone(trainRecords.get(0).getEndDate());
		}
		return result;
	}
	

	@Override
	public PartnerCourseRecord queryOfflinePeixunRecord(Long userId,
			PartnerPeixunCourseTypeEnum courseType, String courseCode) {
		Assert.notNull(userId);
		Assert.notNull(courseType);
		Assert.notNull(courseCode);
		PartnerCourseRecordExample example = new PartnerCourseRecordExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andPartnerUserIdEqualTo(userId);
		criteria.andCourseTypeEqualTo(courseType.getCode());
		criteria.andCourseCodeEqualTo(courseCode);
		List<PartnerCourseRecord> records = partnerCourseRecordMapper
				.selectByExample(example);
		if(records.size()>0){
			return records.get(0);
		}
		return null;
	}

}
