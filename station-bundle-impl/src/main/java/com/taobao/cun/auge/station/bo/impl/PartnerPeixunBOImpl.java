package com.taobao.cun.auge.station.bo.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

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
import com.taobao.cun.appResource.dto.AppResourceDto;
import com.taobao.cun.appResource.service.AppResourceService;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.dal.domain.PartnerCourseRecord;
import com.taobao.cun.auge.dal.domain.PartnerCourseRecordExample;
import com.taobao.cun.auge.dal.domain.PartnerCourseRecordExample.Criteria;
import com.taobao.cun.auge.dal.mapper.PartnerCourseRecordMapper;
import com.taobao.cun.auge.fuwu.FuwuOrderService;
import com.taobao.cun.auge.notify.DefaultNotifyPublish;
import com.taobao.cun.auge.notify.NotifyFuwuOrderChangeVo;
import com.taobao.cun.auge.partner.service.PartnerQueryService;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerPeixunBO;
import com.taobao.cun.auge.station.condition.PartnerPeixunQueryCondition;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerPeixunDto;
import com.taobao.cun.auge.station.dto.PartnerPeixunListDetailDto;
import com.taobao.cun.auge.station.dto.PartnerPeixunStatusCountDto;
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
	AppResourceService appResourceService;
	@Autowired
	DefaultNotifyPublish defaultNotifyPublish;
	@Autowired
	PartnerQueryService partnerQueryService;
	@Autowired
	FuwuOrderService fuwuOrderService;
	
	@Value("${partner.peixun.client.code}")
	private String peixunClientCode;
	
	@Value("${partner.peixun.client.key}")
	private String peixunClientKey;
	
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void handlePeixunFinishSucess(StringMessage strMessage, JSONObject ob) {
		String messageType=strMessage.getMessageType();
		if(!NotifyContents.CUNXUEXI_PEIXUN_COMPLETE_MST.equals(messageType)){
			//不需要处理的消息类型
            return;
		}
		String courseType=getCourseTypeByCode(ob.getString("productCode"));
		if(StringUtils.isNotEmpty(courseType)){
			PartnerCourseRecord pc=handleComplete(ob,courseType);
			//通知售中关闭订单
			try{
				fuwuOrderService.closeOrder(pc.getOrderNum());
			}catch(Exception e){
				//通知售中失败 不影响自己处理逻辑
				logger.error("call martini closeOrder error ",e);
			}
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void handlePeixunPaymentProcess(StringMessage strMessage,
			JSONObject ob) {
		String messageType=strMessage.getMessageType();
		if(!NotifyContents.CRM_ORDER_PAYMENT_SUCESS_MESSAGETYPE.equals(messageType)){
			//不需要处理的消息类型
            return;
		}
		String courseType=getCourseTypeByCode(ob.getString("productCode"));
		if(StringUtils.isNotEmpty(courseType)){
			handlePaymentSucess(ob,courseType);
			//获取合伙人信息
			PartnerDto partner=partnerQueryService.queryPartnerByTaobaoUserId(new Long(ob.getString("benefitCustomerAliId")));
			//notify crm peixun
			NotifyFuwuOrderChangeVo vo =new NotifyFuwuOrderChangeVo();
			vo.setMessageType(NotifyContents.FUWU_ORDER_PAYMENT_MESSAGETYPE);
			vo.setTopic(NotifyContents.FUWU_ORDER_PAYMENT_TOPIC);
			vo.setPartnerName(partner.getName());
			vo.setPartnerPhone(partner.getMobile());
			vo.setOrderNum(ob.getString("itemNum"));
			vo.setProductCode(ob.getString("productCode"));
			vo.setUserId(new Long(ob.getString("benefitCustomerAliId")));
			defaultNotifyPublish.publish(vo);
		}
	}
	
	private String getCourseTypeByCode(String code){
		List<AppResourceDto> apps=appResourceService.queryAppResourceList("PARTNER_PEIXUN_CODE");
		for(AppResourceDto app:apps){
			if(app.getValue().equals(code)){
				return app.getName();
			}
		}
		logger.info("handle code not match:"+code);
		return null;
	}
	
	
	private void handlePaymentSucess(JSONObject ob,String courseType){
		Long userId=ob.getLong("benefitCustomerAliId");
		String orderNum=ob.getString("itemNum");
		String code=ob.getString("productCode");
		Assert.notNull(userId);
		Assert.notNull(orderNum);
		Assert.notNull(code);
		PartnerCourseRecord record=null;
		PartnerCourseRecordExample example = new PartnerCourseRecordExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andPartnerUserIdEqualTo(userId);
		criteria.andCourseTypeEqualTo(courseType);
		criteria.andCourseCodeEqualTo(code);
		List<PartnerCourseRecord> records=partnerCourseRecordMapper.selectByExample(example);
		if(records.size()==0){
			//初始化培训记录
			record=initPeixunRecord(userId,PartnerPeixunCourseTypeEnum.valueof(courseType), code);
		}else{
			record=records.get(0);
		}
		if(record.getStatus().equals(PartnerPeixunStatusEnum.NEW.getCode())){
			record.setStatus(PartnerPeixunStatusEnum.PAY.getCode());
			record.setOrderNum(orderNum);
			DomainUtils.beforeUpdate(record, DomainUtils.DEFAULT_OPERATOR);
			partnerCourseRecordMapper.updateByPrimaryKey(record);
		}else{
			logger.warn("peixunRecord status :"+record.getStatus());
		}
		
	}

	private PartnerCourseRecord handleComplete(JSONObject ob,String courseType){
		Long userId=ob.getLong("buyerAliId");
		String code=ob.getString("productCode");
		Assert.notNull(userId);
		Assert.notNull(code);
		PartnerCourseRecordExample example = new PartnerCourseRecordExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andPartnerUserIdEqualTo(userId);
		criteria.andCourseTypeEqualTo(courseType);
		criteria.andCourseCodeEqualTo(code);
		List<PartnerCourseRecord> records=partnerCourseRecordMapper.selectByExample(example);
		if(records.size()==0){
			throw new RuntimeException("not find peixunRecord "+userId.toString());
		}
		PartnerCourseRecord record=records.get(0);
        record.setStatus(PartnerPeixunStatusEnum.DONE.getCode());
        record.setGmtDone(new Date());
        DomainUtils.beforeUpdate(record, DomainUtils.DEFAULT_OPERATOR);
        partnerCourseRecordMapper.updateByPrimaryKey(record);
        //更新lifecycle
        if(code.equals(appResourceService.queryAppResourceValue("PARTNER_PEIXUN_CODE",
				"APPLY_IN"))){
			try {
				partnerInstanceBO.finishCourse(userId);
			} catch (Exception e) {
				logger.error(
						"finish partnerInstance peixun error,"
								+ ob.getLong("buyerAliId"), e);
			}
        }
        return record;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
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
		List<String> codes=new ArrayList<String>();
		codes.add(courseCode);
		List<TrainingRecordDTO> trainRecords=getRecordFromPeixun(auth,codes,userId);
		for(TrainingRecordDTO dto:trainRecords){
			if(orderNum.equals(dto.getOrderItemNum())){
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
	
	private List<TrainingRecordDTO> getRecordFromPeixun(AppAuthDTO auth,List<String> codes, Long userId) {
		TrainingRecordQueryDTO query = new TrainingRecordQueryDTO();
		query.setCourseCodes(codes);
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


	@Override
	public List<PartnerPeixunDto> queryBatchPeixunRecord(List<Long> userIds,String courseType,String courseCode) {
		List<PartnerPeixunDto> result=new ArrayList<PartnerPeixunDto>();
		if(userIds==null||userIds.size()==0){
			return result;
		}
		PartnerCourseRecordExample example = new PartnerCourseRecordExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andPartnerUserIdIn(userIds);
		criteria.andCourseTypeEqualTo(courseType);
		criteria.andCourseCodeEqualTo(courseCode);
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
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
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
		List<String> codes=new ArrayList<String>();
		codes.add(courseCode);
		List<TrainingRecordDTO> trainRecords = getRecordFromPeixun(auth,codes,
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

	@Override
	public List<PartnerPeixunDto> queryBatchOnlinePeixunProcess(Long userId,
			List<String> courseCodes) {
		AppAuthDTO auth = new AppAuthDTO();
		auth.setAuthkey(peixunClientKey);
		auth.setCode(peixunClientCode);
		List<TrainingRecordDTO> trainRecords=getRecordFromPeixun(auth,courseCodes,userId);
		List<PartnerPeixunDto> result=new ArrayList<PartnerPeixunDto>();
		l1:for(TrainingRecordDTO dto:trainRecords){
			for(PartnerPeixunDto re:result){
				if(dto.getCourseCode().equals(String.valueOf(re.getCourseCode()))){
				continue l1;
			  }
			}
			PartnerPeixunDto dt=new PartnerPeixunDto();
			dt.setUserId(new Long(dto.getTrainee()));
			dt.setStatus(PartnerPeixunStatusEnum.DONE.getCode());
			dt.setStatusDesc(PartnerPeixunStatusEnum.DONE.getDesc());
			dt.setGmtDone(dto.getEndDate());
			dt.setCourseCode(dto.getCourseCode());
			result.add(dt);
		}
		return result;
	}

	@Override
	public List<PartnerPeixunStatusCountDto> queryPeixunCountByCondition(
			PartnerPeixunQueryCondition condition) {
		Assert.notNull(condition.getOrgIdPath());
		Map<String,Object> param =new HashMap<String,Object>();
		param.put("orgIdPath", condition.getOrgIdPath());
		param.put("nickName", condition.getNickName());
		param.put("phoneNum", condition.getPhoneNum());
		param.put("partnerName", condition.getPartnerName());
		param.put("courseTypes", condition.getCourseTypes());
		List<PartnerPeixunStatusCountDto> records = partnerCourseRecordMapper
				.queryPeixunCountByCondition(param);
		return records;
	}

	@Override
	public PageDto<PartnerPeixunListDetailDto> queryPeixunList(
			PartnerPeixunQueryCondition condition) {
		Assert.notNull(condition.getOrgIdPath());
		Map<String,Object> param =new HashMap<String,Object>();
		param.put("orgIdPath", condition.getOrgIdPath());
		param.put("nickName", condition.getNickName());
		param.put("phoneNum", condition.getPhoneNum());
		param.put("partnerName", condition.getPartnerName());
		if(condition.getPeixunStatus()!=null){
			param.put("courseStatus", condition.getPeixunStatus());
		}
		param.put("courseType", condition.getCourseTypes().get(0));
		param.put("pageNum", condition.getPageNum());
		param.put("pageSize", condition.getPageSize());
		int count=partnerCourseRecordMapper.queryPeixunListCount(param);
		List<PartnerPeixunListDetailDto> records = partnerCourseRecordMapper
				.queryPeixunList(param);
		fillResult(records,condition.getCourseTypes().get(0));
		PageDto<PartnerPeixunListDetailDto> result=new PageDto<PartnerPeixunListDetailDto>();
		result.setTotal(count);
		result.setItems(records);
		return result;
	}

	private void fillResult(List<PartnerPeixunListDetailDto> records,String courseType){
		if(records.size()>0){
			List<Long> userIds=new ArrayList<Long>();
			for(PartnerPeixunListDetailDto dto:records){
				userIds.add(dto.getUserId());
			}
			PartnerCourseRecordExample example = new PartnerCourseRecordExample();
			Criteria criteria = example.createCriteria();
			criteria.andIsDeletedEqualTo("n");
			criteria.andPartnerUserIdIn(userIds);
			if(PartnerPeixunCourseTypeEnum.APPLY_IN.getCode().equals(courseType)){
				criteria.andCourseTypeEqualTo(PartnerPeixunCourseTypeEnum.UPGRADE.getCode());
			}else{
				criteria.andCourseTypeEqualTo(PartnerPeixunCourseTypeEnum.APPLY_IN.getCode());
			}
			List<PartnerCourseRecord> newRecords = partnerCourseRecordMapper.selectByExample(example);
			for(PartnerPeixunListDetailDto dto:records){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				if(dto.getCourseType().equals(PartnerPeixunCourseTypeEnum.APPLY_IN.getCode())){
					dto.setQiHangStatus(dto.getCourseStatus());
					dto.setQiHangStatusDesc(PartnerPeixunStatusEnum.valueof(dto.getCourseStatus()).getDesc());
					dto.setGmtQiHangDone(dto.getGmtDone());
				    dto.setGmtQiHangDoneDesc(dto.getGmtQiHangDone()==null?null:sdf.format(dto.getGmtQiHangDone())); 
					for(PartnerCourseRecord newRecord:newRecords){
						if(dto.getUserId().compareTo(newRecord.getPartnerUserId())==0){
							dto.setChengZhangStatus(newRecord.getStatus());
							dto.setChengZhangStatusDesc(PartnerPeixunStatusEnum.valueof(newRecord.getStatus()).getDesc());
							dto.setGmtChengZhangDone(newRecord.getGmtDone());
							dto.setGmtChengZhangDoneDesc(newRecord.getGmtDone()==null?null:sdf.format(newRecord.getGmtDone()));
							break;
						}
					}
				}else{
					dto.setChengZhangStatus(dto.getCourseStatus());
					dto.setChengZhangStatusDesc(PartnerPeixunStatusEnum.valueof(dto.getCourseStatus()).getDesc());
					dto.setGmtChengZhangDone(dto.getGmtDone());
				    dto.setGmtChengZhangDoneDesc(dto.getGmtChengZhangDone()==null?null:sdf.format(dto.getGmtChengZhangDone())); 
					for(PartnerCourseRecord newRecord:newRecords){
						if(dto.getUserId().compareTo(newRecord.getPartnerUserId())==0){
							dto.setQiHangStatus(newRecord.getStatus());
							dto.setQiHangStatusDesc(PartnerPeixunStatusEnum.valueof(newRecord.getStatus()).getDesc());
							dto.setGmtQiHangDone(newRecord.getGmtDone());
							dto.setGmtQiHangDoneDesc(newRecord.getGmtDone()==null?null:sdf.format(newRecord.getGmtDone()));
							break;
						}
					}
				}
			}
		}
	}
}
