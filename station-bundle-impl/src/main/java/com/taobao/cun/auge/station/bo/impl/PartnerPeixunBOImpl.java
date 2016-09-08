package com.taobao.cun.auge.station.bo.impl;

import java.text.SimpleDateFormat;
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
import com.alibaba.intl.fileserver.commons.tool.url.FileserverURLTools;
import com.alibaba.intl.fileserver.commons.tool.url.SchemaEnum;
import com.alibaba.ivy.common.AppAuthDTO;
import com.alibaba.ivy.common.PageDTO;
import com.alibaba.ivy.common.ResultDTO;
import com.alibaba.ivy.service.course.CourseServiceFacade;
import com.alibaba.ivy.service.course.dto.CourseDTO;
import com.alibaba.ivy.service.course.query.CourseQueryDTO;
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
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerPeixunBO;
import com.taobao.cun.auge.station.dto.PartnerOnlinePeixunDto;
import com.taobao.cun.auge.station.dto.PartnerPeixunDto;
import com.taobao.cun.auge.station.enums.NotifyContents;
import com.taobao.cun.auge.station.enums.PartnerOnlinePeixunStatusEnum;
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
	
	@Value("${partner.apply.in.peixun.code}")
	private String peixunCode;
	
	@Value("${partner.peixun.client.code}")
	private String peixunClientCode;
	
	@Value("${partner.peixun.client.key}")
	private String peixunClientKey;
	
	@Value("${crm.peixun.course.url}")
	private String courseUrl;
	
	@Value("${crm.peixun.order.url}")
	private String orderUrl;
	
	@Value("${crm.peixun.online.couerse.url}")
	private String onlineCourseUrl;
	
	@Value("${crm.peixun.online.code}")
	private String onlineCourseCode;
	
	@Value("${crm.peixun.online.exam.url}")
	private String onlineExamUrl;
	
	@Value("${crm.peixun.online.exam.paperId}")
	private String paperId;
	
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
	public PartnerPeixunDto queryApplyInPeixunRecord(Long userId) {
		Assert.notNull(userId);
		PartnerPeixunDto result = new PartnerPeixunDto();
		result.setUserId(userId);
		PartnerCourseRecordExample example = new PartnerCourseRecordExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andPartnerUserIdEqualTo(userId);
		criteria.andCourseTypeEqualTo(PartnerPeixunCourseTypeEnum.APPLY_IN
				.getCode());
		List<PartnerCourseRecord> records = partnerCourseRecordMapper
				.selectByExample(example);
		if (records.size() > 0) {
			// 获取课程信息
			CourseDTO course=getCourseFromPeixun(peixunCode);
			result.setCourseName(course.getName());
			result.setCourseAmount(course.getPrice());
			result.setCourseCode(peixunCode);
			result.setLogo(FileserverURLTools.alibabaV2Builder()
					.filename(course.getLogo()).useSchema(SchemaEnum.EMPTY)
					.build());
			PartnerCourseRecord record = records.get(0);
			result.setStatus(record.getStatus());
			result.setStatusDesc(PartnerPeixunStatusEnum.valueof(
					record.getStatus()).getDesc());
			//获取培训记录
			List<TrainingRecordDTO> trainRecords=getRecordFromPeixun(peixunCode,userId);
			if (!PartnerPeixunStatusEnum.NEW.getCode().equals(record.getStatus())) {
					result.setGmtDone(record.getGmtDone());
					result.setOrderNum(record.getOrderNum());
					result.setGmtOrder(record.getGmtCreate());
					//获取签到码
					result.setTicketNo(getTicketNo(trainRecords,record.getOrderNum()));
			}else{
				//查询有没有未付款订单信息
				if(trainRecords.size()>0){
					result.setOrderNum(trainRecords.get(0).getOrderItemNum());
					result.setStatus(PartnerPeixunStatusEnum.WAIT_PAY.getCode());
					result.setStatusDesc(PartnerPeixunStatusEnum.WAIT_PAY.getDesc());
					result.setGmtOrder(record.getGmtCreate());
				}
			}
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			if(result.getGmtDone()!=null){
				result.setGmtDoneDesc(sdf.format(result.getGmtDone()));
			}
			if(result.getGmtOrder()!=null){
				result.setGmtOrderDesc(sdf.format(result.getGmtOrder()));
			}
			result.setMyOrderUrl(orderUrl);
			result.setCourseDetailUrl(courseUrl);
			return result;
		}
		return null;
	}
	
	private String getTicketNo(List<TrainingRecordDTO> trainRecords,String orderNum){
		AppAuthDTO auth = new AppAuthDTO();
		auth.setAuthkey(peixunClientKey);
		auth.setCode(peixunClientCode);
		for(TrainingRecordDTO dto:trainRecords){
			if(orderNum.equals(getOrderNoByOrderItem(dto.getOrderItemNum()))){
				ResultDTO<List<TrainingTicketDTO>> ticketDto=trainingTicketServiceFacade.getByTrainingRecordId(auth, dto.getId());
				if(ticketDto.isSuccess()){
					if(ticketDto.getData() != null && ticketDto.getData().size()>0){
						return ticketDto.getData().get(0).getTicketNo();
					}else {
						return "";
					}
				}else{
					logger.error("getByTrainingRecordId error param:"+dto.getId()+"message:"+JSONObject.toJSONString(ticketDto));
					throw new RuntimeException("getTicketError "+ticketDto.getMsg());
				}
			}
		}
		return null;
	}
	
	private CourseDTO getCourseFromPeixun(String code) {
		AppAuthDTO auth = new AppAuthDTO();
		auth.setAuthkey(peixunClientKey);
		auth.setCode(peixunClientCode);
		CourseQueryDTO courseQuery = new CourseQueryDTO();
		courseQuery.setCodes(Lists.newArrayList(peixunCode));
		try {
			ResultDTO<PageDTO<CourseDTO>> courseResult = courseServiceFacade
					.find(auth, courseQuery, 100, 1);
			if (courseResult.isSuccess()
					&& courseResult.getData().getRows().size() > 0) {
				return courseResult.getData().getRows().get(0);
			} else {
				throw new RuntimeException("query course error,"
						+ courseResult.getMsg());
			}
		} catch (Exception e) {
			logger.error("queryApplyInPeixunList error", e);
			throw new RuntimeException(e);
		}
	}
	
	private List<TrainingRecordDTO> getRecordFromPeixun(String code, Long userId) {
		AppAuthDTO auth = new AppAuthDTO();
		auth.setAuthkey(peixunClientKey);
		auth.setCode(peixunClientCode);
		TrainingRecordQueryDTO query = new TrainingRecordQueryDTO();
		query.addCourseCode(code);
		query.addTrainee(String.valueOf(userId));
//		query.addStatus(TrainStatus.NotEffect.value());
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
	public PartnerOnlinePeixunDto queryOnlinePeixunProcess(Long userId) {
		Assert.notNull(userId);
		PartnerOnlinePeixunDto result=new PartnerOnlinePeixunDto();
		result.setCourseUrl(onlineCourseUrl);
		result.setTaobaoUserId(userId);
		result.setCourseCode(onlineCourseCode);
		result.setExamUrl(onlineExamUrl);
		//查询在线培训记录
		List<TrainingRecordDTO> trainRecords=getRecordFromPeixun(onlineCourseCode, userId);
		if(trainRecords.size()==0){
			result.setStatus(PartnerOnlinePeixunStatusEnum.WAIT_PEIXUN);
			return result;
		}else{
		//查询考试成绩
			
		}
		return null;
	}

	@Override
	public void dispatchApplyInExamPaper(Long userId) {
		Assert.notNull(userId);
		ExamDispatchDto dto = new ExamDispatchDto();
		dto.setDispatcher("SYSTEM");
		dto.setPaperId(new Long(paperId));
		dto.setUserId(userId);
		ResultModel<Boolean> result = examUserDispatchService.dispatchExam(dto);
		if (!result.isSuccess()) {
			throw new RuntimeException("dispatch examPaper fail:"
					+ result.getException());
		}
	}

}
